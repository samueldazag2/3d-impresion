import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { fetchSettings, updateSettings } from './settingsApi'

const SETTINGS_QUERY_KEY = ['settings']

export function useSettings() {
  return useQuery({ queryKey: SETTINGS_QUERY_KEY, queryFn: fetchSettings })
}

export function useUpdateSettings() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: updateSettings,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: SETTINGS_QUERY_KEY }),
  })
}
