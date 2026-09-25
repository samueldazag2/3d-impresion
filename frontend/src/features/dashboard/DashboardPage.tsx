import { useQuery } from '@tanstack/react-query'
import { Link } from 'react-router-dom'

import { QuoteStatusBadge, useQuotes, type QuoteStatus } from '../quotes'

import { fetchDashboardSummary } from './dashboardApi'

const statusOrder: QuoteStatus[] = ['PENDING', 'APPROVED', 'PRINTING', 'DELIVERED']

function Stat({ label, value, tone }: { label: string; value: string; tone?: string }) {
  return (
    <div className="rounded-2xl border border-border/40 bg-surface p-5">
      <p className="text-sm text-ink-muted">{label}</p>
      <p className={`num mt-1 text-xl font-semibold ${tone ?? 'text-ink'}`}>{value}</p>
    </div>
  )
}

export function DashboardPage() {
  const summary = useQuery({ queryKey: ['dashboard'], queryFn: fetchDashboardSummary })
  const quotes = useQuotes()

  const data = summary.data
  const money = (value: number) => `${data?.currency ?? ''} ${value.toFixed(2)}`
  const recent = quotes.data?.content.slice(0, 5) ?? []

  return (
    <main className="px-6 py-10">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="text-2xl font-bold text-ink">Resumen del negocio</h1>
          <p className="mt-1 text-ink-muted">Lo que tienes en marcha y lo que ya entregaste.</p>
        </div>
        <Link
          to="/quotes/new"
          className="rounded-lg bg-accent-strong px-4 py-2 font-semibold text-on-accent"
        >
          Nueva cotización
        </Link>
      </div>

      {summary.isLoading && <p className="mt-6 text-ink-muted">Cargando resumen…</p>}
      {summary.isError && <p className="mt-6 text-danger">No se pudo cargar el resumen.</p>}

      {data && (
        <>
          <section
            aria-label="Finanzas"
            className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4"
          >
            <Stat label="Por cobrar (en curso)" value={money(data.pipelineValue)} />
            <Stat label="Ventas entregadas" value={money(data.deliveredRevenue)} />
            <Stat label="Costo de lo entregado" value={money(data.deliveredCost)} />
            <Stat label="Ganancia" value={money(data.deliveredProfit)} tone="text-success" />
          </section>

          <div className="mt-6 grid grid-cols-1 gap-6 lg:grid-cols-2">
            <section
              aria-label="Cotizaciones por estado"
              className="rounded-2xl border border-border/40 bg-surface p-5"
            >
              <h2 className="text-lg font-semibold text-ink">Cotizaciones por estado</h2>
              <ul className="mt-3 space-y-2">
                {statusOrder.map((status) => (
                  <li key={status} className="flex items-center justify-between text-sm">
                    <QuoteStatusBadge status={status} />
                    <span className="num text-ink">{data.quotesByStatus[status]}</span>
                  </li>
                ))}
              </ul>
            </section>

            <section
              aria-label="Filamento por reponer"
              className="rounded-2xl border border-border/40 bg-surface p-5"
            >
              <h2 className="text-lg font-semibold text-ink">Filamento por reponer</h2>
              {data.lowStockMaterials.length === 0 ? (
                <p className="mt-3 text-sm text-ink-muted">
                  Todo el filamento tiene más de {data.lowStockThresholdGrams} g.
                </p>
              ) : (
                <ul className="mt-3 space-y-2">
                  {data.lowStockMaterials.map((material) => (
                    <li key={material.id} className="flex items-center justify-between text-sm">
                      <span className="text-ink">
                        {material.name} · {material.color}
                      </span>
                      <span className="num font-medium text-danger">
                        {material.currentStockGrams} g
                      </span>
                    </li>
                  ))}
                </ul>
              )}
              <Link
                to="/materials"
                className="mt-4 inline-block text-sm font-medium text-accent hover:underline"
              >
                Ir a materiales
              </Link>
            </section>
          </div>
        </>
      )}

      <section
        aria-label="Últimas cotizaciones"
        className="mt-6 rounded-2xl border border-border/40 bg-surface p-5"
      >
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-semibold text-ink">Últimas cotizaciones</h2>
          <Link to="/quotes" className="text-sm font-medium text-accent hover:underline">
            Ver todas
          </Link>
        </div>
        {recent.length === 0 ? (
          <p className="mt-3 text-sm text-ink-muted">Todavía no hay cotizaciones.</p>
        ) : (
          <ul className="mt-3 divide-y divide-border/20">
            {recent.map((quote) => (
              <li key={quote.id} className="flex items-center justify-between gap-3 py-2 text-sm">
                <span className="truncate text-ink">{quote.title}</span>
                <span className="flex shrink-0 items-center gap-4">
                  <span className="num text-ink">
                    {quote.currency} {quote.suggestedPrice.toFixed(2)}
                  </span>
                  <QuoteStatusBadge status={quote.status} />
                </span>
              </li>
            ))}
          </ul>
        )}
      </section>
    </main>
  )
}
