# Alcance funcional — PrintCost Studio

Proyecto nuevo, independiente de la versión anterior en producción
(printcost-studio.netlify.app). Esa versión solo se usa como referencia funcional
y visual; no se reutiliza código ni datos.

## Objetivo del producto

Calcular el costo real de una impresión 3D (material, tiempo de impresión,
electricidad, desgaste de impresora, mano de obra, empaque) y sugerir un precio
de venta con margen configurable, con backend propio, base de datos persistente
y control de acceso por roles desde el primer día.

## Fases

- **Fase 0** (este documento): scaffold del repositorio, sin lógica de negocio.
- **Fase 1**: backend + base de datos + auth ADMIN + CRUD completo + mini-CRM de
  clientes + panel de administrador. Incluye el módulo Electricidad y el resumen
  del negocio (por cobrar, ventas entregadas, ganancia, filamento por reponer).
- **Fase 2**: despliegue (Neon + Render + Netlify) y ajustes de operación diaria.

**Herramienta de un solo operador.** La usa únicamente el dueño del negocio para
cotizar y organizarse. No hay clientes externos con acceso: no existe login,
panel ni rol de cliente. Los clientes del mini-CRM son solo contactos
que el admin registra.

## Roles

| Rol      | Puede |
|----------|-------|
| `ADMIN`  | Todo: materiales, cotizaciones, clientes, electricidad, configuración; auditoría. Es el único rol. |

## Módulos y forma de los datos

Los tipos siguientes son la referencia para las entidades JPA (backend) y los
tipos TypeScript (frontend). Los nombres de campos van en inglés según el
estándar de código del proyecto.

### User

```ts
{
  id: string            // UUID
  email: string
  passwordHash: string  // nunca se expone en la API
  role: 'ADMIN'
  createdAt: string
}
```

### Client (mini-CRM)

```ts
{
  id: string
  name: string
  email?: string
  phone?: string
  notes?: string
  createdAt: string
  updatedAt: string
}
```

### Material (inventario de filamentos)

```ts
{
  id: string
  name: string
  type: string            // PLA, PETG, ABS, TPU, ...
  color: string
  pricePerKg: number
  currentStockGrams: number
  densityGramsPerCm3?: number
  createdAt: string
  updatedAt: string
}
```

### Settings (configuración global, un único registro por instancia)

```ts
{
  id: string
  currency: string                    // ISO 4217, ej. COP, USD
  electricityRatePerKwh: number
  printerPowerConsumptionWatts: number
  printerPurchasePrice: number
  printerLifespanHours: number
  defaultMarginPercentage: number
  laborHourlyRate: number
  updatedAt: string
}
```

### ElectricityConsumptionRecord (módulo Electricidad)

Historial de facturas/consumo real, usado para validar o recalcular
`electricityRatePerKwh` en Settings.

```ts
{
  id: string
  periodStart: string     // date
  periodEnd: string       // date
  kwhConsumed: number
  totalBillAmount: number
  computedRatePerKwh: number   // totalBillAmount / kwhConsumed
  notes?: string
  createdAt: string
}
```

### Quote (cotización)

Los costos se **calculan y se guardan como snapshot** en el momento de crear la
cotización, para que cambios posteriores en Settings o en el precio de un
material no alteren cotizaciones históricas.

```ts
{
  id: string
  clientId?: string
  title: string
  materialId: string
  materialGramsUsed: number
  printTimeHours: number

  // snapshot de costos, calculado en el backend al crear/recalcular
  materialCost: number
  electricityCost: number
  printerWearCost: number
  laborCost: number
  packagingCost: number
  marginPercentage: number
  totalCost: number
  suggestedPrice: number
  currency: string

  status: 'PENDING' | 'APPROVED' | 'PRINTING' | 'DELIVERED'
  createdAt: string
  updatedAt: string
}
```

**Fórmula de costeo** (implementada y testeada en el backend, es la parte más
crítica del sistema):

```
materialCost      = (materialGramsUsed / 1000) * material.pricePerKg
electricityCost   = (printerPowerConsumptionWatts / 1000) * printTimeHours * electricityRatePerKwh
printerWearCost   = (printerPurchasePrice / printerLifespanHours) * printTimeHours
laborCost         = laborHours * laborHourlyRate          // laborHours es input de la cotización
totalCost         = materialCost + electricityCost + printerWearCost + laborCost + packagingCost
suggestedPrice    = totalCost * (1 + marginPercentage / 100)
```

### AuditLog (requisito de seguridad)

```ts
{
  id: string
  userId: string
  action: string          // LOGIN, UPDATE_SETTINGS, DELETE_MATERIAL, ...
  entityType?: string
  entityId?: string
  createdAt: string
}
```

## Fuera de alcance por ahora

- Login, panel o cualquier acceso para clientes externos (la herramienta es de un solo operador).
- Notificaciones por email.
- Cualquier dato o código de la versión anterior en producción — no se migra nada.
