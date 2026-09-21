export type Role = 'ADMIN' | 'CLIENT'

export interface LoginRequest {
  email: string
  password: string
}

export interface TokenResponse {
  accessToken: string
  refreshToken: string
  email: string
  role: Role
}
