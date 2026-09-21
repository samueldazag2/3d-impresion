import { BentoCard } from '../shared'

const modules = [
  { title: 'Nueva cotización', description: 'Calculadora de costo y precio sugerido.' },
  { title: 'Materiales', description: 'Inventario de filamentos.' },
  { title: 'Historial', description: 'Cotizaciones anteriores por cliente.' },
  { title: 'Electricidad', description: 'Consumo y costo por hora de impresión.' },
  { title: 'Clientes', description: 'Mini-CRM: contactos y notas.' },
  { title: 'Configuración', description: 'Moneda, tarifa eléctrica, vida útil de impresora.' },
]

export function DashboardPlaceholder() {
  return (
    <main className="min-h-screen bg-bg px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">PrintCost Studio</h1>
      <p className="mt-1 text-ink-muted">
        Fase 0 — scaffold del proyecto. Los módulos se implementan en Fase 1.
      </p>
      <div className="mt-8 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {modules.map((module) => (
          <BentoCard key={module.title} title={module.title}>
            {module.description}
          </BentoCard>
        ))}
      </div>
    </main>
  )
}
