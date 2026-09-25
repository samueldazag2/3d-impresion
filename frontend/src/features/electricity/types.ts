export interface ElectricityRecord {
  id: string
  periodStart: string
  periodEnd: string
  kwhConsumed: number
  totalBillAmount: number
  computedRatePerKwh: number
  notes: string | null
  createdAt: string
}

export interface ElectricityRecordRequest {
  periodStart: string
  periodEnd: string
  kwhConsumed: number
  totalBillAmount: number
  notes?: string | null
}
