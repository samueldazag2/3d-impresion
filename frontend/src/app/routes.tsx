import type { ReactNode } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { LoginPage, ProtectedRoute } from '../features/auth'
import { ClientsPage } from '../features/clients'
import { DashboardPage } from '../features/dashboard'
import { ElectricityPage } from '../features/electricity'
import { MaterialsPage } from '../features/materials'
import { NewQuotePage, QuotesPage } from '../features/quotes'
import { SettingsPage } from '../features/settings'

import { AppLayout } from './AppLayout'

function protectedPage(page: ReactNode) {
  return (
    <ProtectedRoute>
      <AppLayout>{page}</AppLayout>
    </ProtectedRoute>
  )
}

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={protectedPage(<DashboardPage />)} />
        <Route path="/materials" element={protectedPage(<MaterialsPage />)} />
        <Route path="/settings" element={protectedPage(<SettingsPage />)} />
        <Route path="/electricity" element={protectedPage(<ElectricityPage />)} />
        <Route path="/clients" element={protectedPage(<ClientsPage />)} />
        <Route path="/quotes/new" element={protectedPage(<NewQuotePage />)} />
        <Route path="/quotes" element={protectedPage(<QuotesPage />)} />
      </Routes>
    </BrowserRouter>
  )
}
