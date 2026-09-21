import { useClients } from '../clients'
import { useMaterials } from '../materials'

import { NewQuoteForm } from './NewQuoteForm'
import { QuoteResultCard } from './QuoteResultCard'
import { useCreateQuote } from './useQuotes'

export function NewQuotePage() {
  const materialsQuery = useMaterials()
  const clientsQuery = useClients()
  const createQuote = useCreateQuote()

  const materials = materialsQuery.data?.content ?? []
  const clients = clientsQuery.data?.content ?? []

  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">Nueva cotización</h1>
      <p className="mt-1 text-ink-muted">
        Calcula el costo real de la impresión y el precio de venta sugerido.
      </p>

      {materials.length === 0 && !materialsQuery.isLoading ? (
        <p className="mt-6 text-ink-muted">
          Registra al menos un material en la sección Materiales antes de cotizar.
        </p>
      ) : (
        <div className="mt-6">
          <NewQuoteForm
            materials={materials}
            clients={clients}
            onSubmit={(payload) => createQuote.mutate(payload)}
            isSubmitting={createQuote.isPending}
          />
        </div>
      )}

      {createQuote.isError && (
        <p role="alert" className="mt-3 text-sm text-danger">
          No se pudo calcular la cotización. Revisa los datos e intenta de nuevo.
        </p>
      )}

      {createQuote.data && <QuoteResultCard quote={createQuote.data} />}
    </main>
  )
}
