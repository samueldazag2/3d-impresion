import { httpClient } from '../../shared'
import type { Material } from '../materials'
import type { QuoteStatus } from '../quotes'

export interface DashboardSummary {
  currency: string
  quotesByStatus: Record<QuoteStatus, number>
  pipelineValue: number
  deliveredRevenue: number
  deliveredCost: number
  deliveredProfit: number
  lowStockThresholdGrams: number
  lowStockMaterials: Material[]
}

export async function fetchDashboardSummary(): Promise<DashboardSummary> {
  const { data } = await httpClient.get<DashboardSummary>('/dashboard')
  return data
}
