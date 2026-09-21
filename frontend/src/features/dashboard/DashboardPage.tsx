import { Link } from 'react-router-dom'

import { BentoCard } from '../../shared'

const modules = [
  {
    title: 'Materiales',
    description: 'Inventario de filamentos y su precio por kilo.',
    to: '/materials',
  },
  {
    title: 'Configuración',
    description: 'Moneda, tarifa eléctrica, vida útil de impresora.',
    to: '/settings',
  },
  {
    title: 'Nueva cotización',
    description: 'Calculadora de costo y precio sugerido. Próximamente.',
    to: null,
  },
  { title: 'Clientes', description: 'Mini-CRM: contactos y notas. Próximamente.', to: null },
  {
    title: 'Historial',
    description: 'Cotizaciones anteriores por cliente. Próximamente.',
    to: null,
  },
  {
    title: 'Electricidad',
    description: 'Consumo y costo por hora de impresión. Próximamente.',
    to: null,
  },
]

export function DashboardPage() {
  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">PrintCost Studio</h1>
      <p className="mt-1 text-ink-muted">Panel de administrador.</p>
      <div className="mt-8 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {modules.map((module) =>
          module.to ? (
            <Link
              key={module.title}
              to={module.to}
              className="block rounded-2xl focus-visible:outline-none"
            >
              <BentoCard title={module.title}>{module.description}</BentoCard>
            </Link>
          ) : (
            <BentoCard key={module.title} title={module.title}>
              {module.description}
            </BentoCard>
          ),
        )}
      </div>
    </main>
  )
}
