// BookCard.jsx
import React from 'react';
import './styles/BookCardStyle.css';

function BookCard({ title, author, price }) {
  const imagePath = `/images/${title}.jpg`;

  return (
    <div className="book-card card shadow-sm">
      <img
        src={imagePath}
        alt={title}
        className="card-img-top"
        onError={(e) => e.target.style.display = 'none'} // fallback if image doesn't exist
      />
      <div className="card-body">
        <h5 className="card-title">{title}</h5>
        <p className="card-text">Author: {author}</p>
        <p className="card-text fw-bold">${price}</p>
      </div>
    </div>
  );
}

export default BookCard;
