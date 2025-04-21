// Navbar.jsx
import React from 'react';
import './styles/NavbarStyle.css';

function Navbar({ onFilterChange, onQueryChange }) {
  return (
    <header className="navbar">
      <div className="container-fluid d-flex align-items-center justify-content-between">
        <div className="flex-grow-1" />

        <div className="d-flex justify-content-start" style={{ width: '80vw' }}>
          <div className="search-section d-flex align-items-start col-6">
            <select
              className="my-search-select col"
              onChange={(e) => onFilterChange(e.target.value)}
            >
              <option value="author">Author</option>
              <option value="title">Title</option>
              <option value="genre">Genre</option>
            </select>
            <input
              type="text"
              className="search-input col-11"
              placeholder="Search"
              onChange={(e) => onQueryChange(e.target.value)}
            />
          </div>
        </div>

        <div className="navbar-right" style={{ width: '8vw' }}>
          <button className="my-btn">Buy now</button>
        </div>
      </div>
    </header>
  );
}

export default Navbar;
