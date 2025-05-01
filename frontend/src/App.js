import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Home from './pages/Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import TestingAPI from './pages/TestingAPI';
import SignUpConfirmation from './pages/SignUpConfirmation';
import DashboardLayout from './components/dashboard/DashboardLayout';
import Dashboard from './pages/dashboard/Dashboard';
import JobListings from './pages/dashboard/JobListings';
import FavoriteJobs from './pages/dashboard/FavoriteJobs';
import Settings from './pages/dashboard/Settings';
import InterviewListings from './pages/dashboard/InterviewListings';
import './App.css';

const ProtectedRoute = () => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
        <div className="flex items-center justify-center h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
    );
  }

  if (!user) {
    localStorage.removeItem('jobListings');
    localStorage.removeItem('interviewListings');
    return <Navigate to="/signin" replace />;
  }

  return <Outlet />;
};

function App() {
  return (
      <AuthProvider>
        <Router>
          <Routes>
            {/* Public routes */}
            <Route path="/" element={<Home />} />
            <Route path="/signin" element={<SignIn />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/signup-confirmation" element={<SignUpConfirmation />} />
            <Route path="/testing-api" element={<TestingAPI />} />

            {/* Protected routes - require authentication */}
            <Route element={<ProtectedRoute />}>
              <Route path="/dashboard" element={<DashboardLayout />}>
                <Route index element={<Dashboard />} />
                <Route path="job-listings" element={<JobListings />} />
                <Route path="interview-listings" element={<InterviewListings />} />
                <Route path="favorite-jobs" element={<FavoriteJobs />} />
                <Route path="settings" element={<Settings />} />
              </Route>
            </Route>

            {/* Catch all other routes and redirect to home */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Router>
      </AuthProvider>
  );
}

export default App;