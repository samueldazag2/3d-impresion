import type { QuoteStatus } from './types'

const labels: Record<QuoteStatus, string> = {
  PENDING: 'Pendiente',
  APPROVED: 'Aprobada',
  PRINTING: 'En impresión',
  DELIVERED: 'Entregada',
}

const colors: Record<QuoteStatus, string> = {
  PENDING: 'text-warning',
  APPROVED: 'text-info',
  PRINTING: 'text-accent',
  DELIVERED: 'text-success',
}

export function QuoteStatusBadge({ status }: { status: QuoteStatus }) {
  return <span className={`text-sm font-medium ${colors[status]}`}>{labels[status]}</span>
}
