import { httpClient, type PageResponse } from '../../shared'

import type { Client, ClientRequest } from './types'

export async function fetchClients(): Promise<PageResponse<Client>> {
  const { data } = await httpClient.get<PageResponse<Client>>('/clients')
  return data
}

export async function createClient(payload: ClientRequest): Promise<Client> {
  const { data } = await httpClient.post<Client>('/clients', payload)
  return data
}

export async function deleteClient(id: string): Promise<void> {
  await httpClient.delete(`/clients/${id}`)
}
