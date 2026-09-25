import type { PropsWithChildren } from 'react'
import { NavLink } from 'react-router-dom'

import { useAuth } from '../features/auth'

const navItems = [
  { to: '/', label: 'Resumen' },
  { to: '/quotes/new', label: 'Nueva cotización' },
  { to: '/quotes', label: 'Cotizaciones' },
  { to: '/clients', label: 'Clientes' },
  { to: '/materials', label: 'Materiales' },
  { to: '/electricity', label: 'Electricidad' },
  { to: '/settings', label: 'Configuración' },
]

export function AppLayout({ children }: PropsWithChildren) {
  const { identity, logout } = useAuth()

  return (
    <div className="min-h-screen bg-bg md:flex">
      <aside className="sidebar bg-sidebar px-4 py-4 md:sticky md:top-0 md:flex md:h-screen md:w-56 md:shrink-0 md:flex-col md:py-6">
        <p className="px-2 text-base font-bold tracking-tight text-sidebar-ink">PrintCost Studio</p>
        <nav
          aria-label="Navegación principal"
          className="mt-3 flex flex-wrap gap-1 md:mt-6 md:flex-col"
        >
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) =>
                `rounded-md px-2 py-1.5 text-sm font-medium ${
                  isActive
                    ? 'bg-sidebar-active text-sidebar-ink'
                    : 'text-sidebar-muted hover:text-sidebar-ink'
                }`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
        <div className="mt-3 flex items-center gap-3 px-2 md:mt-auto md:flex-col md:items-start md:gap-1">
          <span className="truncate text-xs text-sidebar-muted">{identity?.email}</span>
          <button
            type="button"
            onClick={logout}
            className="text-sm font-medium text-sidebar-ink hover:underline"
          >
            Salir
          </button>
        </div>
      </aside>
      <div className="min-w-0 flex-1">{children}</div>
    </div>
  )
}
