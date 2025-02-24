import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Home from './pages/Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import TestingAPI from './pages/TestingAPI';
import SignUpConfirmation from './pages/SignUpConfirmation';
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
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App; 