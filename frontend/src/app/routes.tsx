import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { LoginPage, ProtectedRoute } from '../features/auth'
import { DashboardPage } from '../features/dashboard'
import { MaterialsPage } from '../features/materials'
import { SettingsPage } from '../features/settings'

import { AppLayout } from './AppLayout'

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <AppLayout>
                <DashboardPage />
              </AppLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/materials"
          element={
            <ProtectedRoute>
              <AppLayout>
                <MaterialsPage />
              </AppLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/settings"
          element={
            <ProtectedRoute>
              <AppLayout>
                <SettingsPage />
              </AppLayout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  )
}
