import React from 'react'

const NavBar = () => {
  return (
    <div>
    <div className="logo">Mci<span>Book</span></div>
    <div className="search-bar">
        <select>
            <option>Author</option>
            <option>Title</option>
        </select>
        {/* <input type="text" placeholder="Search..."> */}
        <button>🔍</button>
    </div>
    <button className="buy-now">Buy now</button>
</div>
  )
}

export default NavBar
