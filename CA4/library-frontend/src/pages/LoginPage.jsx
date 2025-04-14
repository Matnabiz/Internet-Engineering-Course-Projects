import React, { useState } from 'react';
import './Login.css';

function SignInPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Username:', username);
    console.log('Password:', password);
  };

  return (
    <div className="signin-container">
      <div className="signin-box">
        <h2>Sign In</h2>
        <p className="brand">MioBook</p>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit" disabled={!username || !password}>
            Sign In
          </button>
        </form>
        <p className="signup-link">
          Not a member yet? <a href="#">Sign Up</a>
        </p>
      </div>
      <footer>
        <p>© 2025 MioBook</p>
      </footer>
    </div>
  );
}

export default SignInPage;
