import type { Role } from './types'

const EMAIL_KEY = 'pcs.email'
const ROLE_KEY = 'pcs.role'

export interface StoredIdentity {
  email: string
  role: Role
}

export function saveIdentity(email: string, role: Role): void {
  localStorage.setItem(EMAIL_KEY, email)
  localStorage.setItem(ROLE_KEY, role)
}

export function loadIdentity(): StoredIdentity | null {
  const email = localStorage.getItem(EMAIL_KEY)
  const role = localStorage.getItem(ROLE_KEY) as Role | null
  if (!email || !role) return null
  return { email, role }
}

export function clearIdentity(): void {
  localStorage.removeItem(EMAIL_KEY)
  localStorage.removeItem(ROLE_KEY)
}
