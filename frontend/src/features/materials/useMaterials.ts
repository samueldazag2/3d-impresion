import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createMaterial, deleteMaterial, fetchMaterials } from './materialsApi'

const MATERIALS_QUERY_KEY = ['materials']

export function useMaterials() {
  return useQuery({ queryKey: MATERIALS_QUERY_KEY, queryFn: fetchMaterials })
}

export function useCreateMaterial() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: createMaterial,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: MATERIALS_QUERY_KEY }),
  })
}

export function useDeleteMaterial() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: deleteMaterial,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: MATERIALS_QUERY_KEY }),
  })
}
