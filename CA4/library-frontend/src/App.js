import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';


// Import your pages/components
import AddAuthorPage from './pages/AddAuthorPage';
import LoginPage from './pages/LoginPage';
import Signup from './pages/Signup';
import Homepage from './pages/Homepage'
import ShowCartPage from './pages/ShowCartPage'
import BookDetails from './pages/BookDetails'


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/authors/add" element={<AddAuthorPage />} />
        <Route path="/users/login" element={<LoginPage />} />
        <Route path="/users/register" element={<Signup />} />
        <Route path="/users/homepage" element={<Homepage />} />
        <Route path="/cart/show/:username" element={<ShowCartPage />} />
        <Route path="/books/details/:title" element={<BookDetails />} />
        {/* Add more routes as you go */}
      </Routes>
    </Router>
  );
}

export default App;
