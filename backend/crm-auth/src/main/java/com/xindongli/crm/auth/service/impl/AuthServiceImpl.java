package com.xindongli.crm.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.xindongli.crm.auth.dto.LoginRequest;
import com.xindongli.crm.auth.dto.LoginResponse;
import com.xindongli.crm.auth.entity.User;
import com.xindongli.crm.auth.mapper.UserMapper;
import com.xindongli.crm.auth.service.AuthService;
import com.xindongli.crm.common.core.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private static final String TOKEN_PREFIX = "token:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    @Override
    public LoginResponse login(LoginRequest request) {
        // 参数校验
        if (request == null || StrUtil.isBlank(request.getUsername()) || StrUtil.isBlank(request.getPassword())) {
            log.warn("登录参数不完整");
            throw new BusinessException("用户名或密码不能为空");
        }
        
        // 1. 查询用户
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername().trim())
        );

        if (user == null) {
            log.warn("登录失败, 用户不存在: {}", request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            log.warn("登录失败, 密码错误, 用户: {}", request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() == 1) {
            log.warn("登录失败, 账号已停用, 用户: {}", request.getUsername());
            throw new BusinessException("账号已被停用");
        }

        // 4. 生成Token
        String token = generateToken(user.getId());
        String refreshToken = generateRefreshToken(user.getId());

        // 5. 存储Token到Redis
        String tokenKey = TOKEN_PREFIX + user.getId();
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + user.getId();
        redisTemplate.opsForValue().set(tokenKey, token, expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, expiration * 7, TimeUnit.MILLISECONDS);

        log.info("用户登录成功, 用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
        
        // 6. 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(expiration / 1000);
        
        // 7. 构建用户信息
        LoginResponse.UserInfo userInfo = buildUserInfo(user);
        response.setUser(userInfo);

        return response;
    }

    @Override
    public void logout() {
        // 从Security上下文获取当前用户ID
        // TODO: 需要实现JWT过滤器,从Token中解析用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            log.warn("登出失败, 无法获取当前用户ID");
            return;
        }
        
        // 删除Redis中的Token
        String tokenKey = TOKEN_PREFIX + userId;
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.delete(tokenKey);
        redisTemplate.delete(refreshTokenKey);
        
        log.info("用户登出成功, 用户ID: {}", userId);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 参数校验
        if (StrUtil.isBlank(refreshToken)) {
            throw new BusinessException("刷新令牌不能为空");
        }
        
        // 1. 验证刷新令牌
        Claims claims = parseToken(refreshToken);
        if (claims == null) {
            log.warn("刷新令牌无效");
            throw new BusinessException("刷新令牌无效");
        }

        Long userId;
        try {
            userId = Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            log.error("刷新令牌中的用户ID格式错误: {}", claims.getSubject());
            throw new BusinessException("刷新令牌无效");
        }

        // 2. 检查Redis中的刷新令牌
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + userId;
        String storedRefreshToken = redisTemplate.opsForValue().get(refreshTokenKey);
        
        if (storedRefreshToken == null) {
            log.warn("刷新令牌已过期, 用户ID: {}", userId);
            throw new BusinessException("刷新令牌已过期,请重新登录");
        }
        
        if (!refreshToken.equals(storedRefreshToken)) {
            log.warn("刷新令牌不匹配, 用户ID: {}", userId);
            throw new BusinessException("刷新令牌已失效");
        }

        // 3. 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("用户不存在, 用户ID: {}", userId);
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户状态
        if (user.getStatus() == 1) {
            throw new BusinessException("账号已被停用");
        }

        // 4. 生成新的Token
        String newToken = generateToken(user.getId());
        String newRefreshToken = generateRefreshToken(user.getId());

        // 5. 更新Redis中的Token
        String tokenKey = TOKEN_PREFIX + user.getId();
        redisTemplate.opsForValue().set(tokenKey, newToken, expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(refreshTokenKey, newRefreshToken, expiration * 7, TimeUnit.MILLISECONDS);

        log.info("刷新Token成功, 用户ID: {}", user.getId());
        
        // 6. 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(newToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(expiration / 1000);
        
        LoginResponse.UserInfo userInfo = buildUserInfo(user);
        response.setUser(userInfo);

        return response;
    }

    @Override
    public LoginResponse.UserInfo getCurrentUserInfo() {
        // 从Security上下文获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("用户未登录");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("用户不存在, 用户ID: {}", userId);
            throw new BusinessException("用户不存在");
        }

        return buildUserInfo(user);
    }

    /**
     * 生成Token
     */
    private String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, userId.toString(), expiration);
    }

    /**
     * 生成刷新令牌
     */
    private String generateRefreshToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userId.toString(), expiration * 7);
    }

    /**
     * 创建Token
     */
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * 解析Token
     */
    private Claims parseToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("解析Token失败", e);
            return null;
        }
    }

    /**
     * 构建用户信息
     */
    private LoginResponse.UserInfo buildUserInfo(User user) {
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setPhone(user.getPhone());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setDeptId(user.getDeptId());
        
        // TODO: 查询部门名称 - 需要实现部门服务
        userInfo.setDeptName("研发部");
        
        // TODO: 查询角色列表 - 需要实现角色查询
        List<LoginResponse.RoleInfo> roles = List.of(
            new LoginResponse.RoleInfo() {{
                setId(1L);
                setRoleName("管理员");
                setRoleCode("admin");
            }}
        );
        userInfo.setRoles(roles);
        
        // TODO: 查询权限列表 - 需要实现权限查询
        userInfo.setPermissions(List.of("system:user:list", "system:user:add"));

        return userInfo;
    }
    
    /**
     * 获取当前用户ID
     * TODO: 需要实现JWT过滤器,从Token中解析用户ID
     */
    private Long getCurrentUserId() {
        // 临时返回null,需要实现JWT过滤器后从Security上下文获取
        return null;
    }

}
