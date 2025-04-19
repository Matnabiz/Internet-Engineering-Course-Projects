import React, { useState } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
//import './Login.css';


function SignInPage() {
  //const [username, setUsername] = useState('');
  //const [password, setPassword] = useState('');
  const [formData, setFormData] = useState({
      username: '',
      password: '',
    });


  const handleChange = (e) => {
    const { name, value } = e.target;

      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    
  };

  const isFormValid = () => {
    return (
      formData.username.trim() !== '' &&
      formData.password.trim() !== ''
    );
  };


  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid()) {
      toast.error("Please fill out all fields.");
      return;
    }

    try {
      console.log(formData);
      const res = await axios.post('http://localhost:9090/users/login', formData);
      toast.success(res.data.message || 'Successfully login!');
      setFormData({
        username: '',
        password: ''
      });

    } catch (err) {
      toast.error(err.response?.data?.message || 'Something went wrong.');
    }
  };


  //const handleSubmit = (e) => {
    //e.preventDefault();
    //console.log('Username:', username);
  //  console.log('Password:', password);
 // };

  return (
    <div className="signin-container">
      <ToastContainer position="top-center" />
      <div className="signin-box">
        <h2>Sign In</h2>
        <p className="brand">MioBook</p>
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
          <button type="submit" disabled={!isFormValid()}>
            Sign In
          </button>
        </form>
        <p className="signup-link">
          Not a member yet? <a href="http://localhost:3000/users/register">Sign Up</a>
        </p>
      </div>
      <footer>
        <p>© 2025 MioBook</p>
      </footer>
    </div>
  );
}

export default SignInPage;
