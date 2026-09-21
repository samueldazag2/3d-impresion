import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { SettingsForm } from './SettingsForm'
import { fetchSettings, updateSettings } from './settingsApi'
import type { SettingsRequest } from './types'

function toRequest(settings: { updatedAt: string } & SettingsRequest): SettingsRequest {
  return {
    currency: settings.currency,
    electricityRatePerKwh: settings.electricityRatePerKwh,
    printerPowerConsumptionWatts: settings.printerPowerConsumptionWatts,
    printerPurchasePrice: settings.printerPurchasePrice,
    printerLifespanHours: settings.printerLifespanHours,
    defaultMarginPercentage: settings.defaultMarginPercentage,
    laborHourlyRate: settings.laborHourlyRate,
  }
}

export function SettingsPage() {
  const { data, isLoading, isError } = useQuery({ queryKey: ['settings'], queryFn: fetchSettings })
  const queryClient = useQueryClient()
  const mutation = useMutation({
    mutationFn: updateSettings,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['settings'] }),
  })

  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">Configuración</h1>
      <p className="mt-1 text-ink-muted">
        Estos valores se usan para calcular el costo de cada cotización nueva.
      </p>

      {isLoading && <p className="mt-6 text-ink-muted">Cargando configuración…</p>}
      {isError && <p className="mt-6 text-danger">No se pudo cargar la configuración.</p>}

      {data && (
        <SettingsForm
          key={data.updatedAt}
          initialValues={toRequest(data)}
          onSave={(payload) => mutation.mutate(payload)}
          isSaving={mutation.isPending}
          isError={mutation.isError}
          isSuccess={mutation.isSuccess}
        />
      )}
    </main>
  )
}
