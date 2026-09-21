import type { PropsWithChildren } from 'react'

type BentoCardProps = PropsWithChildren<{
  title: string
}>

export function BentoCard({ title, children }: BentoCardProps) {
  return (
    <article className="rounded-2xl border border-border/40 bg-surface p-6">
      <h2 className="text-lg font-semibold text-ink">{title}</h2>
      <div className="mt-2 text-ink-muted">{children}</div>
    </article>
  )
}
