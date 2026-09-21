export interface Material {
  id: string
  name: string
  type: string
  color: string
  pricePerKg: number
  currentStockGrams: number
  densityGramsPerCm3: number | null
  createdAt: string
  updatedAt: string
}

export interface MaterialRequest {
  name: string
  type: string
  color: string
  pricePerKg: number
  currentStockGrams: number
  densityGramsPerCm3?: number | null
}
