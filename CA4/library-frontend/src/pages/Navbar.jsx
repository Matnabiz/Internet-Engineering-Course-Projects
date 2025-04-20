import React from 'react';
import './styles/NavbarStyle.css';

function Navbar() {
  return (
    <header className="navbar">
      <div className="container-fluid d-flex align-items-center justify-content-between">

        {/* Left (optional spacer or logo) */}
        <div className="flex-grow-1" />

        {/* Center: Search */}
        <div className="d-flex justify-content-center" style={{ width: '70vw' }}>
            <div className="search-section d-flex align-items-start">
              <select className="search-select">
                <option>Author</option>
                <option>Title</option>
                <option>Genre</option>
              </select>
            <input type="text" className="search-input" placeholder="Search" />
         </div>
        </div>


        {/* Right: Button */}
        <div className="d-flex justify-content-end" style={{ width: '8vw' }}>
            <button className="btn btn-dark w-100">Buy now</button>
        </div>

      </div>
    </header>
  );
}

export default Navbar;

