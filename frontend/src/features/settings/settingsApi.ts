import { httpClient } from '../../shared'

import type { Settings, SettingsRequest } from './types'

export async function fetchSettings(): Promise<Settings> {
  const { data } = await httpClient.get<Settings>('/settings')
  return data
}

export async function updateSettings(payload: SettingsRequest): Promise<Settings> {
  const { data } = await httpClient.put<Settings>('/settings', payload)
  return data
}
