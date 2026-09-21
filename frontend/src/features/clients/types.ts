export interface Client {
  id: string
  name: string
  email: string | null
  phone: string | null
  notes: string | null
  createdAt: string
  updatedAt: string
}

export interface ClientRequest {
  name: string
  email?: string | null
  phone?: string | null
  notes?: string | null
}
