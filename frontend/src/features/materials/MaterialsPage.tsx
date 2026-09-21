import { MaterialForm } from './MaterialForm'
import { useCreateMaterial, useDeleteMaterial, useMaterials } from './useMaterials'

export function MaterialsPage() {
  const { data, isLoading, isError } = useMaterials()
  const createMaterial = useCreateMaterial()
  const deleteMaterial = useDeleteMaterial()

  return (
    <main className="px-6 py-10">
      <h1 className="text-2xl font-bold text-ink">Materiales</h1>
      <p className="mt-1 text-ink-muted">Inventario de filamentos y su precio por kilo.</p>

      <div className="mt-6">
        <MaterialForm
          onSubmit={(payload) => createMaterial.mutate(payload)}
          isSubmitting={createMaterial.isPending}
        />
      </div>

      {createMaterial.isError && (
        <p role="alert" className="mt-3 text-sm text-danger">
          No se pudo crear el material. Revisa los datos e intenta de nuevo.
        </p>
      )}

      <div className="mt-6 overflow-x-auto rounded-2xl border border-border/40 bg-surface">
        {isLoading && <p className="p-6 text-ink-muted">Cargando materiales…</p>}
        {isError && <p className="p-6 text-danger">No se pudieron cargar los materiales.</p>}
        {data && data.content.length === 0 && (
          <p className="p-6 text-ink-muted">Todavía no hay materiales registrados.</p>
        )}
        {data && data.content.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="border-b border-border/40 text-ink-muted">
                <th scope="col" className="px-4 py-3">
                  Nombre
                </th>
                <th scope="col" className="px-4 py-3">
                  Tipo
                </th>
                <th scope="col" className="px-4 py-3">
                  Color
                </th>
                <th scope="col" className="px-4 py-3">
                  Precio/kg
                </th>
                <th scope="col" className="px-4 py-3">
                  Stock (g)
                </th>
                <th scope="col" className="px-4 py-3">
                  <span className="sr-only">Acciones</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((material) => (
                <tr key={material.id} className="border-b border-border/20 text-ink">
                  <td className="px-4 py-3">{material.name}</td>
                  <td className="px-4 py-3">{material.type}</td>
                  <td className="px-4 py-3">{material.color}</td>
                  <td className="px-4 py-3">{material.pricePerKg.toFixed(2)}</td>
                  <td className="px-4 py-3">{material.currentStockGrams}</td>
                  <td className="px-4 py-3 text-right">
                    <button
                      type="button"
                      aria-label={`Eliminar ${material.name}`}
                      onClick={() => deleteMaterial.mutate(material.id)}
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
