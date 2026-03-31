package com.xindongli.crm.system.aspect;

import com.xindongli.crm.common.core.exception.BusinessException;
import com.xindongli.crm.common.core.result.ResultCode;
import com.xindongli.crm.system.annotation.RequirePermission;
import com.xindongli.crm.system.entity.Permission;
import com.xindongli.crm.system.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限校验切面
 * 
 * @author 芯动力科技
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final PermissionService permissionService;

    @Around("@annotation(com.xindongli.crm.system.annotation.RequirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取注解
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);
        if (annotation == null) {
            return joinPoint.proceed();
        }
        
        // 获取请求上下文
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("无法获取请求上下文");
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        // 获取请求中的用户ID
        HttpServletRequest request = attributes.getRequest();
        String userId = (String) request.getAttribute("userId");
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("用户未登录或会话已过期");
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        // 获取用户权限列表
        List<Permission> userPermissions = permissionService.getPermissionsByUserId(userId);
        if (userPermissions == null || userPermissions.isEmpty()) {
            log.warn("用户无任何权限, 用户ID: {}", userId);
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        Set<String> userPermissionCodes = userPermissions.stream()
                .map(Permission::getPermissionCode)
                .filter(code -> code != null && !code.trim().isEmpty())
                .collect(Collectors.toSet());
        
        // 获取需要的权限
        String[] requiredPermissions = annotation.value();
        if (requiredPermissions == null || requiredPermissions.length == 0) {
            log.warn("权限注解未配置权限标识");
            return joinPoint.proceed();
        }
        
        boolean requireAll = annotation.requireAll();
        
        // 校验权限
        boolean hasPermission;
        if (requireAll) {
            // 需要满足所有权限
            hasPermission = userPermissionCodes.containsAll(Set.of(requiredPermissions));
        } else {
            // 满足其中一个即可
            hasPermission = false;
            for (String permission : requiredPermissions) {
                if (permission != null && userPermissionCodes.contains(permission)) {
                    hasPermission = true;
                    break;
                }
            }
        }
        
        if (!hasPermission) {
            log.warn("用户无权限访问, 用户ID: {}, 需要权限: {}, 用户权限: {}", userId, 
                    String.join(",", requiredPermissions), String.join(",", userPermissionCodes));
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        
        log.debug("权限校验通过, 用户ID: {}, 权限: {}", userId, String.join(",", requiredPermissions));
        
        // 执行方法
        return joinPoint.proceed();
    }

}
