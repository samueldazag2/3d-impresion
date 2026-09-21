import type { Quote } from './types'

const lines: Array<{ key: keyof Quote; label: string }> = [
  { key: 'materialCost', label: 'Material' },
  { key: 'electricityCost', label: 'Electricidad' },
  { key: 'printerWearCost', label: 'Desgaste de impresora' },
  { key: 'laborCost', label: 'Mano de obra' },
  { key: 'packagingCost', label: 'Empaque' },
]

export function QuoteResultCard({ quote }: { quote: Quote }) {
  return (
    <div className="mt-6 rounded-2xl border border-accent/40 bg-surface p-6">
      <h2 className="text-lg font-semibold text-ink">{quote.title}</h2>
      <dl className="mt-4 space-y-1 text-sm">
        {lines.map(({ key, label }) => (
          <div key={key} className="flex justify-between text-ink-muted">
            <dt>{label}</dt>
            <dd>
              {quote.currency} {Number(quote[key]).toFixed(2)}
            </dd>
          </div>
        ))}
        <div className="flex justify-between border-t border-border/40 pt-2 font-medium text-ink">
          <dt>Costo total</dt>
          <dd>
            {quote.currency} {quote.totalCost.toFixed(2)}
          </dd>
        </div>
        <div className="flex justify-between text-lg font-bold text-accent">
          <dt>Precio sugerido</dt>
          <dd>
            {quote.currency} {quote.suggestedPrice.toFixed(2)}
          </dd>
        </div>
      </dl>
    </div>
  )
}
