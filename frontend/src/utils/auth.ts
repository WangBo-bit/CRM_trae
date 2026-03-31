import Cookies from 'js-cookie'

const TokenKey = import.meta.env.VITE_TOKEN_KEY

// 获取Token
export function getToken(): string | undefined {
  return Cookies.get(TokenKey)
}

// 设置Token
export function setToken(token: string): string | undefined {
  return Cookies.set(TokenKey, token)
}

// 移除Token
export function removeToken(): void {
  Cookies.remove(TokenKey)
}
