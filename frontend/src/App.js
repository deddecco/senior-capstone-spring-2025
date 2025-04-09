import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Home from './pages/Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import TestingAPI from './pages/TestingAPI';
import SignUpConfirmation from './pages/SignUpConfirmation';
import DashboardLayout from './components/dashboard/DashboardLayout';
import Dashboard from './pages/dashboard/Dashboard';
import JobListings from './pages/dashboard/JobListings';
import SavedJobs from './pages/dashboard/SavedJobs';
import Settings from './pages/dashboard/Settings';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/signup-confirmation" element={<SignUpConfirmation />} />
          <Route path="/testingapi" element={<TestingAPI />} />
          
          {/* Dashboard Routes */}
          <Route path="/dashboard" element={<DashboardLayout />}>
            <Route index element={<Dashboard />} />
            <Route path="job-listings" element={<JobListings />} />
            <Route path="saved-jobs" element={<SavedJobs />} />
            <Route path="settings" element={<Settings />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App; 