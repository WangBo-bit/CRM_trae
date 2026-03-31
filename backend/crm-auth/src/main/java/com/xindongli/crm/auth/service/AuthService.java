package com.xindongli.crm.auth.service;

import com.xindongli.crm.auth.dto.LoginRequest;
import com.xindongli.crm.auth.dto.LoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 登录
     * 
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 登出
     */
    void logout();

    /**
     * 刷新Token
     * 
     * @param refreshToken 刷新令牌
     * @return 登录响应
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 获取当前用户信息
     * 
     * @return 用户信息
     */
    LoginResponse.UserInfo getCurrentUserInfo();

}
