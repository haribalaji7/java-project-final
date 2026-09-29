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

function App() {
  return (
    <ThemeProvider defaultTheme="system" storageKey="bloodlink-ui-theme">
      <BrowserRouter>
        <Toaster position="top-right" />
        <Layout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/search" element={<SearchDonors />} />
            <Route path="/register" element={<RegisterDonor />} />
            <Route path="/record" element={<RecordDonation />} />
            <Route path="/stats" element={<Statistics />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Layout>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
