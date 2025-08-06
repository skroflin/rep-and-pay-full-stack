import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter } from 'react-router'
import { UserProvider } from './user-context/User.tsx'
import { AllRoutes } from './routes.tsx'

const queryClient = new QueryClient()

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <UserProvider>
          <AllRoutes />
        </UserProvider>
      </QueryClientProvider>
    </BrowserRouter>
  </StrictMode>,
)
