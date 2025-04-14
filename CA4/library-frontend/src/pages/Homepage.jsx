import React from 'react';
import './HomepageStyle.css';

function UserPage() {
  return (
    <div className="user-page-container">
      <header>
        <div className="nav">
          <input type="text" placeholder="Search" />
          <button className="buy-now">Buy now</button>
        </div>
      </header>

      <main>
        <section className="balance-section">
          <div className="balance-box">
            <h2>$1,000</h2>
            <input type="text" placeholder="Amount" />
            <button className="add-credit">Add more credit</button>
          </div>
          <div className="user-info">
            <p><strong>Sana Navaei</strong></p>
            <p>📧 sana.sarinavaei@gmail.com</p>
            <button className="logout">Logout</button>
          </div>
        </section>

        <section className="cart-section">
          <h3>🛒 Cart</h3>
          <div className="empty-state">
            <img src="/assets/NoCart.jpg" alt="Empty Cart" />
            <p>No Product</p>
          </div>
        </section>

        <section className="history-section">
          <h3>📜 History</h3>
          <div className="empty-state">
            <img src="/assets/NoResult.jpg" alt="No History" />
            <p>No Result</p>
          </div>
        </section>
      </main>

      <footer>
        <p>© 2025 - MioBook</p>
      </footer>
    </div>
  );
}

export default UserPage;
