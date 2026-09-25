import { httpClient, type PageResponse } from '../../shared'

import type { ElectricityRecord, ElectricityRecordRequest } from './types'

export async function fetchElectricityRecords(): Promise<PageResponse<ElectricityRecord>> {
  const { data } = await httpClient.get<PageResponse<ElectricityRecord>>('/electricity-records')
  return data
}

export async function createElectricityRecord(
  payload: ElectricityRecordRequest,
): Promise<ElectricityRecord> {
  const { data } = await httpClient.post<ElectricityRecord>('/electricity-records', payload)
  return data
}

export async function deleteElectricityRecord(id: string): Promise<void> {
  await httpClient.delete(`/electricity-records/${id}`)
}
