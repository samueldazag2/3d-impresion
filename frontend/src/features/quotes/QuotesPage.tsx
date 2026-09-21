import { QuoteStatusBadge } from './QuoteStatusBadge'
import type { QuoteStatus } from './types'
import { useQuotes, useUpdateQuoteStatus } from './useQuotes'

const nextStatus: Record<QuoteStatus, QuoteStatus | null> = {
  PENDING: 'APPROVED',
  APPROVED: 'PRINTING',
  PRINTING: 'DELIVERED',
  DELIVERED: null,
}

const nextStatusLabel: Record<QuoteStatus, string> = {
  PENDING: 'Aprobar',
  APPROVED: 'Marcar en impresión',
  PRINTING: 'Marcar entregada',
  DELIVERED: '',
}

export function QuotesPage() {
  const { data, isLoading, isError } = useQuotes()
  const updateStatus = useUpdateQuoteStatus()

  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">Historial de cotizaciones</h1>
      <p className="mt-1 text-ink-muted">Cotizaciones creadas y su estado actual.</p>

      <div className="mt-6 overflow-x-auto rounded-2xl border border-border/40 bg-surface">
        {isLoading && <p className="p-6 text-ink-muted">Cargando cotizaciones…</p>}
        {isError && <p className="p-6 text-danger">No se pudieron cargar las cotizaciones.</p>}
        {data && data.content.length === 0 && (
          <p className="p-6 text-ink-muted">Todavía no hay cotizaciones.</p>
        )}
        {data && data.content.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="border-b border-border/40 text-ink-muted">
                <th scope="col" className="px-4 py-3">
                  Descripción
                </th>
                <th scope="col" className="px-4 py-3">
                  Precio sugerido
                </th>
                <th scope="col" className="px-4 py-3">
                  Estado
                </th>
                <th scope="col" className="px-4 py-3">
                  <span className="sr-only">Acciones</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((quote) => {
                const upcoming = nextStatus[quote.status]
                return (
                  <tr key={quote.id} className="border-b border-border/20 text-ink">
                    <td className="px-4 py-3">{quote.title}</td>
                    <td className="px-4 py-3">
                      {quote.currency} {quote.suggestedPrice.toFixed(2)}
                    </td>
                    <td className="px-4 py-3">
                      <QuoteStatusBadge status={quote.status} />
                    </td>
                    <td className="px-4 py-3 text-right">
                      {upcoming && (
                        <button
                          type="button"
                          onClick={() => updateStatus.mutate({ id: quote.id, status: upcoming })}
                          className="text-accent hover:underline"
                        >
                          {nextStatusLabel[quote.status]}
                        </button>
                      )}
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        )}
      </div>
    </main>
  )
}
