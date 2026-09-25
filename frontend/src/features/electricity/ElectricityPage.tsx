import { useState, type FormEvent } from 'react'

import { useSettings, useUpdateSettings } from '../settings'

import {
  useCreateElectricityRecord,
  useDeleteElectricityRecord,
  useElectricityRecords,
} from './useElectricity'

const emptyForm = {
  periodStart: '',
  periodEnd: '',
  kwhConsumed: '',
  totalBillAmount: '',
  notes: '',
}

const inputClass = 'mt-1 w-full rounded-lg border border-border bg-bg px-2 py-1.5 text-ink'

export function ElectricityPage() {
  const records = useElectricityRecords()
  const settings = useSettings()
  const updateSettings = useUpdateSettings()
  const createRecord = useCreateElectricityRecord()
  const deleteRecord = useDeleteElectricityRecord()
  const [form, setForm] = useState(emptyForm)

  const kwh = Number(form.kwhConsumed)
  const bill = Number(form.totalBillAmount)
  const previewRate = kwh > 0 && bill > 0 ? bill / kwh : null

  const current = settings.data
  const currency = current?.currency ?? ''
  const costPerPrintHour = current
    ? (current.printerPowerConsumptionWatts / 1000) * current.electricityRatePerKwh
    : null
  const latest = records.data?.content[0]
  const rateDiffers =
    current &&
    latest &&
    Math.abs(latest.computedRatePerKwh - current.electricityRatePerKwh) > 0.0001

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    createRecord.mutate(
      {
        periodStart: form.periodStart,
        periodEnd: form.periodEnd,
        kwhConsumed: kwh,
        totalBillAmount: bill,
        notes: form.notes || null,
      },
      { onSuccess: () => setForm(emptyForm) },
    )
  }

  const applyLatestRate = () => {
    if (!current || !latest) return
    updateSettings.mutate({
      currency: current.currency,
      electricityRatePerKwh: latest.computedRatePerKwh,
      printerPowerConsumptionWatts: current.printerPowerConsumptionWatts,
      printerPurchasePrice: current.printerPurchasePrice,
      printerLifespanHours: current.printerLifespanHours,
      defaultMarginPercentage: current.defaultMarginPercentage,
      laborHourlyRate: current.laborHourlyRate,
    })
  }

  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">Electricidad</h1>
      <p className="mt-1 text-ink-muted">
        Registra tus facturas para mantener al día la tarifa por kWh que usan las cotizaciones.
      </p>

      {current && (
        <section
          aria-label="Tarifa vigente"
          className="mt-6 grid max-w-3xl grid-cols-1 gap-4 sm:grid-cols-3"
        >
          <div className="rounded-2xl border border-border/40 bg-surface p-5">
            <p className="text-sm text-ink-muted">Tarifa vigente</p>
            <p className="num mt-1 text-xl font-semibold text-ink">
              {currency} {current.electricityRatePerKwh.toFixed(4)}
              <span className="text-sm font-normal text-ink-muted"> / kWh</span>
            </p>
          </div>
          <div className="rounded-2xl border border-border/40 bg-surface p-5">
            <p className="text-sm text-ink-muted">Consumo de la impresora</p>
            <p className="num mt-1 text-xl font-semibold text-ink">
              {current.printerPowerConsumptionWatts} W
            </p>
          </div>
          <div className="rounded-2xl border border-border/40 bg-surface p-5">
            <p className="text-sm text-ink-muted">Costo por hora de impresión</p>
            <p className="num mt-1 text-xl font-semibold text-accent">
              {currency} {costPerPrintHour?.toFixed(4)}
            </p>
          </div>
        </section>
      )}

      {rateDiffers && latest && (
        <div
          role="status"
          className="mt-4 flex max-w-3xl flex-wrap items-center justify-between gap-3 rounded-2xl border border-warning/50 bg-surface p-4 text-sm"
        >
          <p className="text-ink">
            Tu última factura sale a{' '}
            <span className="num font-semibold">
              {currency} {latest.computedRatePerKwh.toFixed(4)}
            </span>{' '}
            por kWh, distinto a la tarifa vigente.
          </p>
          <button
            type="button"
            onClick={applyLatestRate}
            disabled={updateSettings.isPending}
            className="rounded-lg bg-accent-strong px-3 py-1.5 font-semibold text-on-accent disabled:opacity-60"
          >
            Usar esta tarifa
          </button>
        </div>
      )}
      {updateSettings.isError && (
        <p role="alert" className="mt-3 text-sm text-danger">
          No se pudo actualizar la tarifa.
        </p>
      )}

      <form
        onSubmit={handleSubmit}
        className="mt-6 max-w-3xl rounded-2xl border border-border/40 bg-surface p-6"
      >
        <h2 className="text-lg font-semibold text-ink">Registrar factura</h2>
        <div className="mt-3 grid grid-cols-1 gap-3 sm:grid-cols-4">
          <div>
            <label htmlFor="period-start" className="block text-sm text-ink-muted">
              Desde
            </label>
            <input
              id="period-start"
              type="date"
              required
              value={form.periodStart}
              onChange={(event) => setForm({ ...form, periodStart: event.target.value })}
              className={inputClass}
            />
          </div>
          <div>
            <label htmlFor="period-end" className="block text-sm text-ink-muted">
              Hasta
            </label>
            <input
              id="period-end"
              type="date"
              required
              value={form.periodEnd}
              onChange={(event) => setForm({ ...form, periodEnd: event.target.value })}
              className={inputClass}
            />
          </div>
          <div>
            <label htmlFor="kwh" className="block text-sm text-ink-muted">
              kWh consumidos
            </label>
            <input
              id="kwh"
              type="number"
              step="0.001"
              min="0.001"
              required
              value={form.kwhConsumed}
              onChange={(event) => setForm({ ...form, kwhConsumed: event.target.value })}
              className={inputClass}
            />
          </div>
          <div>
            <label htmlFor="bill" className="block text-sm text-ink-muted">
              Total facturado
            </label>
            <input
              id="bill"
              type="number"
              step="0.01"
              min="0.01"
              required
              value={form.totalBillAmount}
              onChange={(event) => setForm({ ...form, totalBillAmount: event.target.value })}
              className={inputClass}
            />
          </div>
          <div className="sm:col-span-4">
            <label htmlFor="electricity-notes" className="block text-sm text-ink-muted">
              Notas
            </label>
            <input
              id="electricity-notes"
              value={form.notes}
              onChange={(event) => setForm({ ...form, notes: event.target.value })}
              className={inputClass}
            />
          </div>
        </div>

        {previewRate !== null && (
          <p className="mt-3 text-sm text-ink-muted">
            Tarifa resultante:{' '}
            <span className="num font-semibold text-ink">
              {currency} {previewRate.toFixed(4)}
            </span>{' '}
            por kWh
          </p>
        )}
        {createRecord.isError && (
          <p role="alert" className="mt-3 text-sm text-danger">
            No se pudo guardar. Revisa que las fechas y los valores sean correctos.
          </p>
        )}

        <button
          type="submit"
          disabled={createRecord.isPending}
          className="mt-4 rounded-lg bg-accent-strong px-4 py-2 font-semibold text-on-accent disabled:opacity-60"
        >
          {createRecord.isPending ? 'Guardando…' : 'Guardar factura'}
        </button>
      </form>

      <div className="mt-6 max-w-3xl overflow-x-auto rounded-2xl border border-border/40 bg-surface">
        {records.isLoading && <p className="p-6 text-ink-muted">Cargando facturas…</p>}
        {records.isError && <p className="p-6 text-danger">No se pudieron cargar las facturas.</p>}
        {records.data && records.data.content.length === 0 && (
          <p className="p-6 text-ink-muted">Todavía no hay facturas registradas.</p>
        )}
        {records.data && records.data.content.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="border-b border-border/40 text-ink-muted">
                <th scope="col" className="px-4 py-3">
                  Período
                </th>
                <th scope="col" className="px-4 py-3">
                  kWh
                </th>
                <th scope="col" className="px-4 py-3">
                  Total
                </th>
                <th scope="col" className="px-4 py-3">
                  Tarifa / kWh
                </th>
                <th scope="col" className="px-4 py-3">
                  <span className="sr-only">Acciones</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {records.data.content.map((record) => (
                <tr key={record.id} className="border-b border-border/20 text-ink">
                  <td className="num px-4 py-3">
                    {record.periodStart} → {record.periodEnd}
                  </td>
                  <td className="num px-4 py-3">{record.kwhConsumed}</td>
                  <td className="num px-4 py-3">{record.totalBillAmount.toFixed(2)}</td>
                  <td className="num px-4 py-3">{record.computedRatePerKwh.toFixed(4)}</td>
                  <td className="px-4 py-3 text-right">
                    <button
                      type="button"
                      aria-label={`Eliminar factura ${record.periodStart}`}
                      onClick={() => deleteRecord.mutate(record.id)}
                      className="text-danger hover:underline"
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </main>
  )
}
