import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { DashboardPlaceholder } from './DashboardPlaceholder'

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<DashboardPlaceholder />} />
      </Routes>
    </BrowserRouter>
  )
}
