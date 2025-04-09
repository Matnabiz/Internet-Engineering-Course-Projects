import axios from 'axios';
import { useState } from 'react';

function AddAuthorPage() {
  const [formData, setFormData] = useState({
    adminUsername: '',
    authorName: '',
    penName: '',
    nationality: '',
    birthDate: '',
    deathDate: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:9090/authors/add', formData, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      

      alert(res.data.message);
    } catch (err) {
      alert(err.response?.data?.message || "An error occurred");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Add Author</h2>

      <input
        name="adminUsername"
        placeholder="Admin Username"
        value={formData.adminUsername}
        onChange={handleChange}
      /><br />

      <input
        name="authorName"
        placeholder="Author Name"
        value={formData.authorName}
        onChange={handleChange}
      /><br />

      <input
        name="penName"
        placeholder="Pen Name (optional)"
        value={formData.penName}
        onChange={handleChange}
      /><br />

      <input
        name="nationality"
        placeholder="Nationality"
        value={formData.nationality}
        onChange={handleChange}
      /><br />

      <input
        name="birthDate"
        placeholder="Birth Date (yyyy-mm-dd)"
        value={formData.birthDate}
        onChange={handleChange}
      /><br />

      <input
        name="deathDate"
        placeholder="Death Date (optional)"
        value={formData.deathDate}
        onChange={handleChange}
      /><br />

      <button type="submit">Submit</button>
    </form>
  );
}

export default AddAuthorPage;
