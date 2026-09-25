import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import {
  createElectricityRecord,
  deleteElectricityRecord,
  fetchElectricityRecords,
} from './electricityApi'

const ELECTRICITY_QUERY_KEY = ['electricity-records']

export function useElectricityRecords() {
  return useQuery({ queryKey: ELECTRICITY_QUERY_KEY, queryFn: fetchElectricityRecords })
}

export function useCreateElectricityRecord() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: createElectricityRecord,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ELECTRICITY_QUERY_KEY }),
  })
}

export function useDeleteElectricityRecord() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: deleteElectricityRecord,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ELECTRICITY_QUERY_KEY }),
  })
}
