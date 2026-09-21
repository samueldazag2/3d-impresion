import type { PropsWithChildren } from 'react'
import { NavLink } from 'react-router-dom'

import { useAuth } from '../features/auth'

const navItems = [
  { to: '/', label: 'Dashboard' },
  { to: '/quotes/new', label: 'Nueva cotización' },
  { to: '/quotes', label: 'Historial' },
  { to: '/materials', label: 'Materiales' },
  { to: '/clients', label: 'Clientes' },
  { to: '/settings', label: 'Configuración' },
]

export function AppLayout({ children }: PropsWithChildren) {
  const { identity, logout } = useAuth()

  return (
    <div className="min-h-screen bg-bg">
      <header className="flex flex-wrap items-center justify-between gap-3 border-b border-border/40 bg-surface px-6 py-4">
        <nav aria-label="Navegación principal" className="flex flex-wrap gap-4">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) =>
                `text-sm font-medium ${isActive ? 'text-accent' : 'text-ink-muted hover:text-ink'}`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
        <div className="flex items-center gap-4">
          <span className="text-sm text-ink-muted">{identity?.email}</span>
          <button
            type="button"
            onClick={logout}
            className="text-sm font-medium text-accent hover:underline"
          >
            Salir
          </button>
        </div>
      </header>
      {children}
    </div>
  )
}
