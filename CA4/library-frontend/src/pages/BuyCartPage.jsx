import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './BuyCartPage.css'; // style as you wish
import { useNavigate } from 'react-router-dom';

function BuyCartPage() {
  const [userDetails, setUserDetails] = useState(null);
  const [cart, setCart] = useState([]);
  const [balance, setBalance] = useState(0);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:9090/users/details', {
      params: { username: localStorage.getItem('username') }
    })
      .then(res => {
        const data = res.data.data;
        setUserDetails(data);
        setCart(data.cart);
        setBalance(data.balance);
      })
      .catch(() => {
        setErrorMessage('Failed to fetch user details.');
      });
  }, []);

  const getTotal = () => {
    return cart.reduce((acc, item) => acc + (item.isBorrowed ? item.borrowedPrice : item.originalPrice), 0);
  };

  const handleRemove = (title) => {
    setCart(prev => prev.filter(item => item.title !== title));
    // Optionally, make backend update here
  };

  const handlePurchase = () => {
    const total = getTotal();
    if (total > balance) {
      setErrorMessage('Insufficient balance to complete this purchase.');
      return;
    }
    setErrorMessage('');
    // Proceed to call backend purchase endpoint
    alert("Purchase successful!");
  };

  const handleItemClick = (title) => {
    navigate(`/books/${encodeURIComponent(title)}`);
  };

  return (
    <div className="cart-container">
      <h2>🛒 Your Cart</h2>
      {errorMessage && <p className="error">{errorMessage}</p>}

      {cart.length === 0 ? (
        <div className="empty-cart">
          <img src="/images/NoCart.jpg" alt="Empty Cart" />
          <p>Your cart is empty.</p>
        </div>
      ) : (
        <>
          <ul className="cart-list">
            {cart.map(item => (
              <li key={item.title} onClick={() => handleItemClick(item.title)} className="cart-item">
                <div className="cart-item-content">
                  <strong>{item.title}</strong>
                  <p>Price: ${item.originalPrice}</p>
                  {item.isBorrowed && <p>Borrowed Price: ${item.borrowedPrice}</p>}
                </div>
                <button onClick={(e) => { e.stopPropagation(); handleRemove(item.title); }} className="remove-btn">Remove</button>
              </li>
            ))}
          </ul>
          <div className="cart-summary">
            <p><strong>Total:</strong> ${getTotal()}</p>
            <p><strong>Balance:</strong> ${balance}</p>
            <button onClick={handlePurchase} className="checkout-btn">Buy Now</button>
          </div>
        </>
      )}
    </div>
  );
}

export default BuyCartPage;
