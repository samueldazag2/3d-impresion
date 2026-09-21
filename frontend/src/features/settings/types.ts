export interface Settings {
  currency: string
  electricityRatePerKwh: number
  printerPowerConsumptionWatts: number
  printerPurchasePrice: number
  printerLifespanHours: number
  defaultMarginPercentage: number
  laborHourlyRate: number
  updatedAt: string
}

export type SettingsRequest = Omit<Settings, 'updatedAt'>
