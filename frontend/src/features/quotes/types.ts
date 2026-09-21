export type QuoteStatus = 'PENDING' | 'APPROVED' | 'PRINTING' | 'DELIVERED'

export interface Quote {
  id: string
  clientId: string | null
  materialId: string
  title: string
  materialGramsUsed: number
  printTimeHours: number
  laborHours: number
  packagingCost: number
  materialCost: number
  electricityCost: number
  printerWearCost: number
  laborCost: number
  marginPercentage: number
  totalCost: number
  suggestedPrice: number
  currency: string
  status: QuoteStatus
  createdAt: string
  updatedAt: string
}

export interface QuoteRequest {
  clientId?: string | null
  materialId: string
  title: string
  materialGramsUsed: number
  printTimeHours: number
  laborHours: number
  packagingCost: number
  marginPercentage?: number | null
}
