import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createClient, deleteClient, fetchClients } from './clientsApi'

const CLIENTS_QUERY_KEY = ['clients']

export function useClients() {
  return useQuery({ queryKey: CLIENTS_QUERY_KEY, queryFn: fetchClients })
}

export function useCreateClient() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: createClient,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: CLIENTS_QUERY_KEY }),
  })
}

export function useDeleteClient() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: deleteClient,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: CLIENTS_QUERY_KEY }),
  })
}
