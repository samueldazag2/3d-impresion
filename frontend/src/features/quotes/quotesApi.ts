import { httpClient, type PageResponse } from '../../shared'

import type { Quote, QuoteRequest, QuoteStatus } from './types'

export async function fetchQuotes(): Promise<PageResponse<Quote>> {
  const { data } = await httpClient.get<PageResponse<Quote>>('/quotes')
  return data
}

export async function createQuote(payload: QuoteRequest): Promise<Quote> {
  const { data } = await httpClient.post<Quote>('/quotes', payload)
  return data
}

export async function updateQuoteStatus(id: string, status: QuoteStatus): Promise<Quote> {
  const { data } = await httpClient.patch<Quote>(`/quotes/${id}/status`, { status })
  return data
}
