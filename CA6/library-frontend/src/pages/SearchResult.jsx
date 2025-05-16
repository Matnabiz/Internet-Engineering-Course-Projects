// Search.jsx
import React, { useState, useEffect } from 'react';
import Navbar from './Navbar';
import BookCard from './BookCard';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


function Search() {
  const navigate = useNavigate();
  const [filter, setFilter] = useState('title');
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(6); // Number of books to display per page

  useEffect(() => {
    const token = localStorage.getItem("sessionToken");
    const delay = setTimeout(() => {
      if (query.trim()) {
        axios
          .get(`http://localhost:9090/search/${filter}?${filter}=${query}`,{
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          .then((res) => {
            if (res.data.success && res.data.data.books) {
              const books = res.data.data.books.map(book => ({
                title: book.title,
                author: book.author,
                price: book.price
              }));
              setResults(books);
            }
          })
          .catch((err) => {
            if (err.response && err.response.status === 401) {
              navigate("/users/login");
            }else{
            console.error(err);
            setResults([]);
            }
          });
      } else {
        setResults([]);
      }
    }, 500); // debounce

    return () => clearTimeout(delay);
  }, [query, filter,navigate]);

  // Calculate total pages
  const totalPages = Math.ceil(results.length / itemsPerPage);

  // Get books for the current page
  const currentBooks = results.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  // Handle page change
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  return (
    <div>
      <Navbar
        onFilterChange={(val) => setFilter(val)}
        onQueryChange={(val) => setQuery(val)}
      />

      <div className="container mt-4">
        {currentBooks.length > 0 ? (
          <div className="d-flex flex-wrap justify-content-start">
            {currentBooks.map((book, i) => (
              <BookCard
                key={i}
                title={book.title}
                author={book.author}
                price={book.price}
              />
            ))}
          </div>
        ) : (
          <p>No books found.</p>
        )}
      </div>

      {/* Pagination Controls */}
      <div className="pagination d-flex justify-content-center mt-4">
        <button
          className="btn btn-light"
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        >
          {'<'}
        </button>

        {Array.from({ length: totalPages }, (_, index) => (
          <button
            key={index}
            className={`btn btn-light mx-1 ${currentPage === index + 1 ? 'active' : ''}`}
            onClick={() => handlePageChange(index + 1)}
          >
            {index + 1}
          </button>
        ))}

        <button
          className="btn btn-light"
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
        >
          {'>'}
        </button>
      </div>
    </div>
  );
}

export default Search;
