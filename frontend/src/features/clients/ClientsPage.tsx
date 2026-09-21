import { ClientForm } from './ClientForm'
import { useClients, useCreateClient, useDeleteClient } from './useClients'

export function ClientsPage() {
  const { data, isLoading, isError } = useClients()
  const createClient = useCreateClient()
  const deleteClient = useDeleteClient()

  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">Clientes</h1>
      <p className="mt-1 text-ink-muted">Mini-CRM: contactos asociados a tus cotizaciones.</p>

      <div className="mt-6">
        <ClientForm
          onSubmit={(payload) => createClient.mutate(payload)}
          isSubmitting={createClient.isPending}
        />
      </div>

      {createClient.isError && (
        <p role="alert" className="mt-3 text-sm text-danger">
          No se pudo crear el cliente. Revisa los datos e intenta de nuevo.
        </p>
      )}

      <div className="mt-6 overflow-x-auto rounded-2xl border border-border/40 bg-surface">
        {isLoading && <p className="p-6 text-ink-muted">Cargando clientes…</p>}
        {isError && <p className="p-6 text-danger">No se pudieron cargar los clientes.</p>}
        {data && data.content.length === 0 && (
          <p className="p-6 text-ink-muted">Todavía no hay clientes registrados.</p>
        )}
        {data && data.content.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="border-b border-border/40 text-ink-muted">
                <th scope="col" className="px-4 py-3">
                  Nombre
                </th>
                <th scope="col" className="px-4 py-3">
                  Email
                </th>
                <th scope="col" className="px-4 py-3">
                  Teléfono
                </th>
                <th scope="col" className="px-4 py-3">
                  <span className="sr-only">Acciones</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((client) => (
                <tr key={client.id} className="border-b border-border/20 text-ink">
                  <td className="px-4 py-3">{client.name}</td>
                  <td className="px-4 py-3">{client.email ?? '—'}</td>
                  <td className="px-4 py-3">{client.phone ?? '—'}</td>
                  <td className="px-4 py-3 text-right">
                    <button
                      type="button"
                      aria-label={`Eliminar ${client.name}`}
                      onClick={() => deleteClient.mutate(client.id)}
                      className="text-danger hover:underline"
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </main>
  )
}
