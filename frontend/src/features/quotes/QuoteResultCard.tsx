import { useState } from 'react'

import type { Quote } from './types'

const lines: Array<{ key: keyof Quote; label: string }> = [
  { key: 'materialCost', label: 'Material' },
  { key: 'electricityCost', label: 'Electricidad' },
  { key: 'printerWearCost', label: 'Desgaste de impresora' },
  { key: 'laborCost', label: 'Mano de obra' },
  { key: 'packagingCost', label: 'Empaque' },
]

/** Short text the owner can paste into WhatsApp or an email for the customer. */
function quoteAsText(quote: Quote): string {
  return `${quote.title}\nPrecio: ${quote.currency} ${quote.suggestedPrice.toFixed(2)}`
}

export function QuoteResultCard({ quote }: { quote: Quote }) {
  const [copied, setCopied] = useState(false)

  const copy = async () => {
    try {
      await navigator.clipboard.writeText(quoteAsText(quote))
      setCopied(true)
    } catch {
      setCopied(false)
    }
  }

  return (
    <div className="mt-6 max-w-xl rounded-2xl border border-accent/40 bg-surface p-6">
      <h2 className="text-lg font-semibold text-ink">{quote.title}</h2>
      <dl className="mt-4 space-y-1 text-sm">
        {lines.map(({ key, label }) => (
          <div key={key} className="flex justify-between text-ink-muted">
            <dt>{label}</dt>
            <dd className="num">
              {quote.currency} {Number(quote[key]).toFixed(2)}
            </dd>
          </div>
        ))}
        <div className="flex justify-between border-t border-border/40 pt-2 font-medium text-ink">
          <dt>Costo total</dt>
          <dd className="num">
            {quote.currency} {quote.totalCost.toFixed(2)}
          </dd>
        </div>
        <div className="flex justify-between text-lg font-bold text-accent">
          <dt>Precio sugerido</dt>
          <dd className="num">
            {quote.currency} {quote.suggestedPrice.toFixed(2)}
          </dd>
        </div>
      </dl>
      <button
        type="button"
        onClick={copy}
        className="mt-4 rounded-lg border border-accent px-3 py-1.5 text-sm font-medium text-accent hover:bg-surface-raised"
      >
        {copied ? 'Copiado' : 'Copiar precio para el cliente'}
      </button>
    </div>
  )
}
