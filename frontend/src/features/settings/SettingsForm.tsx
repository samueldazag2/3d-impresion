import { useState, type FormEvent } from 'react'

import type { SettingsRequest } from './types'

interface SettingsFormProps {
  initialValues: SettingsRequest
  onSave: (payload: SettingsRequest) => void
  isSaving: boolean
  isError: boolean
  isSuccess: boolean
}

const fields: Array<{ key: keyof SettingsRequest; label: string; step: string }> = [
  { key: 'currency', label: 'Moneda (ISO, ej. COP)', step: '' },
  { key: 'electricityRatePerKwh', label: 'Tarifa eléctrica (por kWh)', step: '0.0001' },
  { key: 'printerPowerConsumptionWatts', label: 'Consumo de la impresora (W)', step: '1' },
  { key: 'printerPurchasePrice', label: 'Precio de compra de la impresora', step: '0.01' },
  { key: 'printerLifespanHours', label: 'Vida útil de la impresora (horas)', step: '1' },
  { key: 'defaultMarginPercentage', label: 'Margen por defecto (%)', step: '0.1' },
  { key: 'laborHourlyRate', label: 'Tarifa de mano de obra (por hora)', step: '0.01' },
]

export function SettingsForm({
  initialValues,
  onSave,
  isSaving,
  isError,
  isSuccess,
}: SettingsFormProps) {
  const [form, setForm] = useState(initialValues)

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    onSave(form)
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="mt-6 grid max-w-2xl grid-cols-1 gap-4 rounded-2xl border border-border/40 bg-surface p-6 sm:grid-cols-2"
    >
      {fields.map(({ key, label, step }) => (
        <div key={key}>
          <label htmlFor={key} className="block text-sm text-ink-muted">
            {label}
          </label>
          <input
            id={key}
            type={key === 'currency' ? 'text' : 'number'}
            step={step || undefined}
            required
            value={form[key]}
            onChange={(event) =>
              setForm({
                ...form,
                [key]: key === 'currency' ? event.target.value : Number(event.target.value),
              })
            }
            className="mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink"
          />
        </div>
      ))}

      {isError && (
        <p role="alert" className="col-span-full text-sm text-danger">
          No se pudo guardar la configuración.
        </p>
      )}
      {isSuccess && (
        <p role="status" className="col-span-full text-sm text-success">
          Configuración guardada.
        </p>
      )}

      <button
        type="submit"
        disabled={isSaving}
        className="col-span-full rounded-lg bg-accent-strong px-4 py-2 font-semibold text-on-accent disabled:opacity-60"
      >
        {isSaving ? 'Guardando…' : 'Guardar cambios'}
      </button>
    </form>
  )
}
