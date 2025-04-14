import React, { useState } from 'react';
import axios from 'axios';
import './SignupStyle.css';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function SignupPage() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    country: '',
    city: '',
    role: 'Customer'
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleRoleSelect = (selectedRole) => {
    setFormData(prev => ({ ...prev, role: selectedRole }));
  };

  const isFormValid = () => {
    return Object.values(formData).every(val => val.trim() !== '');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid()) {
      toast.error("Please fill out all fields.");
      return;
    }

    try {
      const res = await axios.post('http://localhost:9090/users/register', formData);
      toast.success(res.data.message || 'Successfully signed up!');
      setFormData({
        username: '',
        password: '',
        email: '',
        country: '',
        city: '',
        role: 'Customer'
      });
    } catch (err) {
      toast.error(err.response?.data?.message || 'Something went wrong.');
    }
  };

  return (
    <div className="signup-container">
      <ToastContainer position="top-center" />
      <div className="signup-box">
        <h2>Sign Up</h2>
        <p className="branding">MioBook</p>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={formData.username}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          <div className="location">
            <input
              type="text"
              name="country"
              placeholder="Country"
              value={formData.country}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="city"
              placeholder="City"
              value={formData.city}
              onChange={handleChange}
              required
            />
          </div>
          <p className="role-label">I am</p>
          <div className="role-selection">
            {['Customer', 'Manager'].map(role => (
              <button
                key={role}
                type="button"
                className={formData.role === role ? 'selected' : ''}
                onClick={() => handleRoleSelect(role)}
              >
                {role}
              </button>
            ))}
          </div>
          <button
            type="submit"
            className="signup-btn"
            disabled={!isFormValid()}
          >
            Sign Up
          </button>
        </form>
        <p className="signin-link">Already have an account? <a href="#">Sign in</a></p>
      </div>
      <footer>
        <p>© 2025 - MioBook</p>
      </footer>
    </div>
  );
}

export default SignupPage;
