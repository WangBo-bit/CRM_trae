import request from '@/utils/request'
import type { LoginParams } from '@/types/user'

// 登录
export function login(data: LoginParams) {
  return request.post('/auth/login', data)
}

// 登出
export function logout() {
  return request.post('/auth/logout')
}

// 获取用户信息
export function getUserInfo() {
  return request.get('/auth/user-info')
}

// 刷新Token
export function refreshToken() {
  return request.post('/auth/refresh')
}

// 修改密码
export function updatePassword(data: { oldPassword: string; newPassword: string }) {
  return request.put('/auth/password', data)
}
