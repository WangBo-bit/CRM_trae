// 用户信息
export interface UserInfo {
  id: string
  username: string
  realName: string
  phone?: string
  email?: string
  avatar?: string
  deptId?: string
  deptName?: string
  roles: Role[]
  permissions: string[]
}

// 角色信息
export interface Role {
  id: string
  roleName: string
  roleCode: string
}

// 登录参数
export interface LoginParams {
  username: string
  password: string
  captcha?: string
  captchaKey?: string
}

// 登录响应
export interface LoginResponse {
  token: string
  refreshToken: string
  expiresIn: number
  user: UserInfo
}
