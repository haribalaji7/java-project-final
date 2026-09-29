import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Layout } from './components/layout/Layout';
import Dashboard from './pages/Dashboard';
import RegisterDonor from './pages/RegisterDonor';
import SearchDonors from './pages/SearchDonors';
import RecordDonation from './pages/RecordDonation';
import Statistics from './pages/Statistics';
import NotFound from './pages/NotFound';
import { Toaster } from 'react-hot-toast';
import { ThemeProvider } from './components/theme-provider';

import Login from './pages/Login';
import RegisterUser from './pages/RegisterUser';
import { AuthProvider } from './contexts/AuthContext';
import { ProtectedRoute } from './components/layout/ProtectedRoute';

function App() {
  return (
    <ThemeProvider defaultTheme="system" storageKey="bloodlink-ui-theme">
      <AuthProvider>
        <BrowserRouter>
          <Toaster position="top-right" />
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<RegisterUser />} />
            
            {/* Protected routes wrapped in Layout */}
            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<Layout><Dashboard /></Layout>} />
              <Route path="/search" element={<Layout><SearchDonors /></Layout>} />
              <Route path="/register-donor" element={<Layout><RegisterDonor /></Layout>} />
              <Route path="/record" element={<Layout><RecordDonation /></Layout>} />
              <Route path="/stats" element={<Layout><Statistics /></Layout>} />
            </Route>

            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
