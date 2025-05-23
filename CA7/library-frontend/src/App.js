import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';

import AddAuthorPage from './pages/AddAuthorPage';
import LoginPage from './pages/LoginPage';
import Signup from './pages/Signup';
import Homepage from './pages/Homepage'

import ShowCartPage from './pages/ShowCartPage'
import BookDetails from './pages/BookDetails'
import ShowAuthorPage from './pages/ShowAuthorPage'
import NavBar from './pages/Navbar'
import Test from './pages/test'
import Search from './pages/SearchResult'
import BookCard from './pages/BookCard'
import ProtectedRoute from './ProtectedRoute';
import ShowUserDetails from './pages/ShowUserDetails';

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/users/login" element={<LoginPage />} />
        <Route path="/users/register" element={<Signup />} />
        <Route path="/users/homepage" element={<Homepage />} />
        <Route path="/cart/show/:username" element={<ShowCartPage />} />
        <Route path="/books/details/:bookTitle" element={<BookDetails />} />
        <Route path="/authors/details/:authorName" element={<ShowAuthorPage />} />
        <Route path="/users/:username" element={<ShowUserDetails />} />
        <Route path="/navbar" element={<NavBar />} />
        <Route path="/test" element={<Test />} />
        <Route path="/search" element={<Search />} />
        <Route path="/bookcard" element={<BookCard />} />
        {/* Add more routes as you go */}

        {/* Admin-only Route */}
        <Route
          path="/authors/add"
          element={
            <ProtectedRoute
              element={<AddAuthorPage />}
              allowedRole="admin"
            />
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
