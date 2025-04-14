import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';


// Import your pages/components
import AddAuthorPage from './pages/AddAuthorPage';
import LoginPage from './pages/LoginPage';
import Signup from './pages/Signup';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/authors/add" element={<AddAuthorPage />} />
        <Route path="/users/login" element={<LoginPage />} />
        <Route path="/users/register" element={<Signup />} />
        {/* Add more routes as you go */}
      </Routes>
    </Router>
  );
}

export default App;
