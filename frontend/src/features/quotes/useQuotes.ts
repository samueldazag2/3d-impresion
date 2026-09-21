import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { createQuote, fetchQuotes, updateQuoteStatus } from './quotesApi'
import type { QuoteStatus } from './types'

const QUOTES_QUERY_KEY = ['quotes']

export function useQuotes() {
  return useQuery({ queryKey: QUOTES_QUERY_KEY, queryFn: fetchQuotes })
}

export function useCreateQuote() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: createQuote,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUOTES_QUERY_KEY }),
  })
}

export function useUpdateQuoteStatus() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, status }: { id: string; status: QuoteStatus }) =>
      updateQuoteStatus(id, status),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUOTES_QUERY_KEY }),
  })
}
