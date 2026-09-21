import { useState, type FormEvent } from 'react'

import type { Client } from '../clients'
import type { Material } from '../materials'

import type { QuoteRequest } from './types'

interface NewQuoteFormProps {
  materials: Material[]
  clients: Client[]
  onSubmit: (payload: QuoteRequest) => void
  isSubmitting: boolean
}

const emptyForm = {
  clientId: '',
  materialId: '',
  title: '',
  materialGramsUsed: '',
  printTimeHours: '',
  laborHours: '',
  packagingCost: '',
  marginPercentage: '',
}

export function NewQuoteForm({ materials, clients, onSubmit, isSubmitting }: NewQuoteFormProps) {
  const [form, setForm] = useState(emptyForm)

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    onSubmit({
      clientId: form.clientId || null,
      materialId: form.materialId,
      title: form.title,
      materialGramsUsed: Number(form.materialGramsUsed),
      printTimeHours: Number(form.printTimeHours),
      laborHours: Number(form.laborHours || 0),
      packagingCost: Number(form.packagingCost || 0),
      marginPercentage: form.marginPercentage ? Number(form.marginPercentage) : null,
    })
  }

  return (
    <form onSubmit={handleSubmit} className="rounded-2xl border border-border/40 bg-surface p-6">
      <div className="grid grid-cols-1 gap-3 sm:grid-cols-3">
        <div className="sm:col-span-2">
          <label htmlFor="quote-title" className="block text-sm text-ink-muted">
            Descripción
          </label>
          <input
            id="quote-title"
            required
            value={form.title}
            onChange={(event) => setForm({ ...form, title: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>

        <div>
          <label htmlFor="quote-client" className="block text-sm text-ink-muted">
            Cliente (opcional)
          </label>
          <select
            id="quote-client"
            value={form.clientId}
            onChange={(event) => setForm({ ...form, clientId: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          >
            <option value="">Sin cliente</option>
            {clients.map((client) => (
              <option key={client.id} value={client.id}>
                {client.name}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label htmlFor="quote-material" className="block text-sm text-ink-muted">
            Material
          </label>
          <select
            id="quote-material"
            required
            value={form.materialId}
            onChange={(event) => setForm({ ...form, materialId: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          >
            <option value="" disabled>
              Selecciona un material
            </option>
            {materials.map((material) => (
              <option key={material.id} value={material.id}>
                {material.name} ({material.color})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label htmlFor="quote-grams" className="block text-sm text-ink-muted">
            Material usado (g)
          </label>
          <input
            id="quote-grams"
            type="number"
            min="0.01"
            step="0.01"
            required
            value={form.materialGramsUsed}
            onChange={(event) => setForm({ ...form, materialGramsUsed: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>

        <div>
          <label htmlFor="quote-print-hours" className="block text-sm text-ink-muted">
            Tiempo de impresión (h)
          </label>
          <input
            id="quote-print-hours"
            type="number"
            min="0.01"
            step="0.01"
            required
            value={form.printTimeHours}
            onChange={(event) => setForm({ ...form, printTimeHours: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>

        <div>
          <label htmlFor="quote-labor-hours" className="block text-sm text-ink-muted">
            Mano de obra (h)
          </label>
          <input
            id="quote-labor-hours"
            type="number"
            min="0"
            step="0.01"
            value={form.laborHours}
            onChange={(event) => setForm({ ...form, laborHours: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>

        <div>
          <label htmlFor="quote-packaging" className="block text-sm text-ink-muted">
            Empaque
          </label>
          <input
            id="quote-packaging"
            type="number"
            min="0"
            step="0.01"
            value={form.packagingCost}
            onChange={(event) => setForm({ ...form, packagingCost: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>

        <div>
          <label htmlFor="quote-margin" className="block text-sm text-ink-muted">
            Margen % (opcional)
          </label>
          <input
            id="quote-margin"
            type="number"
            min="0"
            step="0.1"
            placeholder="Usa el margen por defecto"
            value={form.marginPercentage}
            onChange={(event) => setForm({ ...form, marginPercentage: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="mt-4 rounded-lg bg-accent-strong px-4 py-2 font-semibold text-on-accent disabled:opacity-60"
      >
        {isSubmitting ? 'Calculando…' : 'Calcular cotización'}
      </button>
    </form>
  )
}
