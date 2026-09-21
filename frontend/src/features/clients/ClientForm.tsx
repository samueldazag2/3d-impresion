import { useState, type FormEvent } from 'react'

import type { ClientRequest } from './types'

interface ClientFormProps {
  onSubmit: (payload: ClientRequest) => void
  isSubmitting: boolean
}

const emptyForm = { name: '', email: '', phone: '', notes: '' }

export function ClientForm({ onSubmit, isSubmitting }: ClientFormProps) {
  const [form, setForm] = useState(emptyForm)

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    onSubmit({
      name: form.name,
      email: form.email || null,
      phone: form.phone || null,
      notes: form.notes || null,
    })
    setForm(emptyForm)
  }

  return (
    <form onSubmit={handleSubmit} className="rounded-2xl border border-border/40 bg-surface p-6">
      <div className="grid grid-cols-1 gap-3 sm:grid-cols-4">
        <div className="sm:col-span-2">
          <label htmlFor="client-name" className="block text-sm text-ink-muted">
            Nombre
          </label>
          <input
            id="client-name"
            required
            value={form.name}
            onChange={(event) => setForm({ ...form, name: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>
        <div>
          <label htmlFor="client-email" className="block text-sm text-ink-muted">
            Email
          </label>
          <input
            id="client-email"
            type="email"
            value={form.email}
            onChange={(event) => setForm({ ...form, email: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>
        <div>
          <label htmlFor="client-phone" className="block text-sm text-ink-muted">
            Teléfono
          </label>
          <input
            id="client-phone"
            value={form.phone}
            onChange={(event) => setForm({ ...form, phone: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>
        <div className="sm:col-span-4">
          <label htmlFor="client-notes" className="block text-sm text-ink-muted">
            Notas
          </label>
          <textarea
            id="client-notes"
            rows={2}
            value={form.notes}
            onChange={(event) => setForm({ ...form, notes: event.target.value })}
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>
      </div>

      <p className="mt-3 text-xs text-ink-faint">
        Los datos de contacto se usan únicamente para gestionar cotizaciones y pedidos de este
        cliente, conforme a la Ley 1581 de 2012 de protección de datos personales. El cliente puede
        solicitar consultar, corregir o eliminar su información en cualquier momento.
      </p>

      <button
        type="submit"
        disabled={isSubmitting}
        className="mt-4 rounded-lg bg-accent-strong px-4 py-2 font-semibold text-on-accent disabled:opacity-60"
      >
        {isSubmitting ? 'Guardando…' : 'Agregar cliente'}
      </button>
    </form>
  )
}
