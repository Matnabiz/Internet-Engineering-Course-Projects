import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';


// Import your pages/components
import AddAuthorPage from './pages/AddAuthorPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/authors/add" element={<AddAuthorPage />} />
        {/* Add more routes as you go */}
      </Routes>
    </Router>
  );
}

export default App;
