import { httpClient, type PageResponse } from '../../shared'

import type { Material, MaterialRequest } from './types'

export async function fetchMaterials(): Promise<PageResponse<Material>> {
  const { data } = await httpClient.get<PageResponse<Material>>('/materials')
  return data
}

export async function createMaterial(payload: MaterialRequest): Promise<Material> {
  const { data } = await httpClient.post<Material>('/materials', payload)
  return data
}

export async function deleteMaterial(id: string): Promise<void> {
  await httpClient.delete(`/materials/${id}`)
}
