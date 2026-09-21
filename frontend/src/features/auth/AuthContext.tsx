import { createContext, useContext, useEffect, useState, type PropsWithChildren } from 'react'

import { SESSION_EXPIRED_EVENT, clearTokens, getAccessToken, setTokens } from '../../shared'

import * as authApi from './authApi'
import { clearIdentity, loadIdentity, saveIdentity, type StoredIdentity } from './authSession'

interface AuthContextValue {
  identity: StoredIdentity | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export function AuthProvider({ children }: PropsWithChildren) {
  const [identity, setIdentity] = useState<StoredIdentity | null>(() =>
    getAccessToken() ? loadIdentity() : null,
  )

  useEffect(() => {
    const handleSessionExpired = () => setIdentity(null)
    window.addEventListener(SESSION_EXPIRED_EVENT, handleSessionExpired)
    return () => window.removeEventListener(SESSION_EXPIRED_EVENT, handleSessionExpired)
  }, [])

  const login = async (email: string, password: string) => {
    const tokens = await authApi.login({ email, password })
    setTokens(tokens.accessToken, tokens.refreshToken)
    saveIdentity(tokens.email, tokens.role)
    setIdentity({ email: tokens.email, role: tokens.role })
  }

  const logout = () => {
    clearTokens()
    clearIdentity()
    setIdentity(null)
  }

  return (
    <AuthContext.Provider value={{ identity, isAuthenticated: identity !== null, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
