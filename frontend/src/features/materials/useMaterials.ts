import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createMaterial, deleteMaterial, fetchMaterials } from './materialsApi'

const MATERIALS_QUERY_KEY = ['materials']
const DASHBOARD_QUERY_KEY = ['dashboard']

export function useMaterials() {
  return useQuery({ queryKey: MATERIALS_QUERY_KEY, queryFn: fetchMaterials })
}

export function useCreateMaterial() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: createMaterial,
    onSuccess: () => {
      void queryClient.invalidateQueries({ queryKey: MATERIALS_QUERY_KEY })
      void queryClient.invalidateQueries({ queryKey: DASHBOARD_QUERY_KEY })
    },
  })
}

export function useDeleteMaterial() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: deleteMaterial,
    onSuccess: () => {
      void queryClient.invalidateQueries({ queryKey: MATERIALS_QUERY_KEY })
      void queryClient.invalidateQueries({ queryKey: DASHBOARD_QUERY_KEY })
    },
  })
}
