import axios from 'axios'

import { notifySessionExpired } from './sessionEvents'
import { clearTokens, getAccessToken, getRefreshToken, setAccessToken } from './tokenStorage'

const baseURL = import.meta.env.VITE_API_BASE_URL

export const httpClient = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
})

httpClient.interceptors.request.use((config) => {
  const token = getAccessToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

let refreshPromise: Promise<string> | null = null

async function refreshAccessToken(): Promise<string> {
  const refreshToken = getRefreshToken()
  if (!refreshToken) {
    throw new Error('No refresh token available')
  }
  const response = await axios.post<{ accessToken: string }>(`${baseURL}/auth/refresh`, {
    refreshToken,
  })
  setAccessToken(response.data.accessToken)
  return response.data.accessToken
}

httpClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    if (error.response?.status !== 401 || originalRequest._retry) {
      return Promise.reject(error)
    }
    originalRequest._retry = true

    try {
      refreshPromise ??= refreshAccessToken().finally(() => {
        refreshPromise = null
      })
      const newAccessToken = await refreshPromise
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
      return httpClient(originalRequest)
    } catch (refreshError) {
      clearTokens()
      notifySessionExpired()
      return Promise.reject(refreshError)
    }
  },
)
