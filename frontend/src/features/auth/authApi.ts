import { httpClient } from '../../shared'

import type { LoginRequest, TokenResponse } from './types'

export async function login(payload: LoginRequest): Promise<TokenResponse> {
  const { data } = await httpClient.post<TokenResponse>('/auth/login', payload)
  return data
}
