import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, logout, getUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'
import type { LoginParams, UserInfo } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(getToken() || '')
  const userInfo = ref<UserInfo | null>(null)
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  // 登录
  const loginAction = async (loginForm: LoginParams) => {
    try {
      const response = await login(loginForm)
      const { token: accessToken, user } = response.data
      
      token.value = accessToken
      userInfo.value = user
      roles.value = user.roles.map(role => role.roleCode)
      permissions.value = user.permissions
      
      setToken(accessToken)
      
      return response
    } catch (error) {
      throw error
    }
  }

  // 获取用户信息
  const getUserInfoAction = async () => {
    try {
      const response = await getUserInfo()
      const { user } = response.data
      
      userInfo.value = user
      roles.value = user.roles.map(role => role.roleCode)
      permissions.value = user.permissions
      
      return response
    } catch (error) {
      throw error
    }
  }

  // 登出
  const logoutAction = async () => {
    try {
      await logout()
    } finally {
      token.value = ''
      userInfo.value = null
      roles.value = []
      permissions.value = []
      removeToken()
    }
  }

  // 重置token
  const resetToken = () => {
    token.value = ''
    userInfo.value = null
    roles.value = []
    permissions.value = []
    removeToken()
  }

  return {
    token,
    userInfo,
    roles,
    permissions,
    loginAction,
    getUserInfoAction,
    logoutAction,
    resetToken
  }
})
