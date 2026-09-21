import { useState, type FormEvent } from 'react'

import type { MaterialRequest } from './types'

interface MaterialFormProps {
  onSubmit: (payload: MaterialRequest) => void
  isSubmitting: boolean
}

const emptyForm = { name: '', type: '', color: '', pricePerKg: '', currentStockGrams: '' }

export function MaterialForm({ onSubmit, isSubmitting }: MaterialFormProps) {
  const [form, setForm] = useState(emptyForm)

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    onSubmit({
      name: form.name,
      type: form.type,
      color: form.color,
      pricePerKg: Number(form.pricePerKg),
      currentStockGrams: Number(form.currentStockGrams),
    })
    setForm(emptyForm)
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="grid grid-cols-2 gap-3 rounded-2xl border border-border/40 bg-surface p-6 sm:grid-cols-5"
    >
      <div className="col-span-2 sm:col-span-1">
        <label htmlFor="material-name" className="block text-sm text-ink-muted">
          Nombre
        </label>
        <input
          id="material-name"
          required
          value={form.name}
          onChange={(event) => setForm({ ...form, name: event.target.value })}
          className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
        />
      </div>
      <div>
        <label htmlFor="material-type" className="block text-sm text-ink-muted">
          Tipo
        </label>
        <input
          id="material-type"
          required
          placeholder="PLA"
          value={form.type}
          onChange={(event) => setForm({ ...form, type: event.target.value })}
          className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
        />
      </div>
      <div>
        <label htmlFor="material-color" className="block text-sm text-ink-muted">
          Color
        </label>
        <input
          id="material-color"
          required
          value={form.color}
          onChange={(event) => setForm({ ...form, color: event.target.value })}
          className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
        />
      </div>
      <div>
        <label htmlFor="material-price" className="block text-sm text-ink-muted">
          Precio/kg
        </label>
        <input
          id="material-price"
          type="number"
          min="0.01"
          step="0.01"
          required
          value={form.pricePerKg}
          onChange={(event) => setForm({ ...form, pricePerKg: event.target.value })}
          className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
        />
      </div>
      <div>
        <label htmlFor="material-stock" className="block text-sm text-ink-muted">
          Stock (g)
        </label>
        <input
          id="material-stock"
          type="number"
          min="0"
          step="1"
          required
          value={form.currentStockGrams}
          onChange={(event) => setForm({ ...form, currentStockGrams: event.target.value })}
          className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
        />
      </div>
      <button
        type="submit"
        disabled={isSubmitting}
        className="col-span-2 rounded-lg bg-accent-strong px-4 py-2 font-semibold text-on-accent disabled:opacity-60 sm:col-span-5"
      >
        {isSubmitting ? 'Guardando…' : 'Agregar material'}
      </button>
    </form>
  )
}
