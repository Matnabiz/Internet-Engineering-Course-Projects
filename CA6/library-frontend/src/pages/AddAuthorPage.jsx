import axios from 'axios';
import { useState } from 'react';
import { Form, Button, Container, Row, Col, Card } from 'react-bootstrap';
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

//import './AddAuthorPageStyle.css';


function AddAuthorPage() {
  const [formData, setFormData] = useState({
    adminUsername: '',
    authorName: '',
    penName: '',
    nationality: '',
    birthDate: '',
    deathDate: ''
  });
  const navigate = useNavigate();
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setErrors({
      ...errors,
      [e.target.name]: ''
    });
  };

  const validateForm = () => {
    const newErrors = {};
    ['adminUsername', 'authorName', 'nationality', 'birthDate'].forEach(field => {
      if (!formData[field]) {
        newErrors[field] = 'Required';
      }
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    const token = localStorage.getItem("sessionToken");
    e.preventDefault();
    if (!validateForm()) {
      toast.error("Please fill in all required fields");
      return;
    }

    try {
      const res = await axios.post('http://localhost:9090/authors/add', formData, {
        headers: { 'Content-Type': 'application/json' , Authorization: `Bearer ${token}`}
      });
      toast.success(res.data.message);
    } catch (err) {
      if (err.response && err.response.status === 401) {
        navigate("/users/login");
      }else{
      toast.error(err.response?.data?.message || "An error occurred");
      }
    }
  };

  return (
    <Container className="mt-5">
      <ToastContainer position="top-center" />
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card>
            <Card.Body>
              <Card.Title className="text-center mb-4">Add New Author</Card.Title>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Admin Username *</Form.Label>
                  <Form.Control
                    name="adminUsername"
                    value={formData.adminUsername}
                    onChange={handleChange}
                    isInvalid={!!errors.adminUsername}
                  />
                  <Form.Control.Feedback type="invalid">{errors.adminUsername}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Author Name *</Form.Label>
                  <Form.Control
                    name="authorName"
                    value={formData.authorName}
                    onChange={handleChange}
                    isInvalid={!!errors.authorName}
                  />
                  <Form.Control.Feedback type="invalid">{errors.authorName}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Pen Name</Form.Label>
                  <Form.Control
                    name="penName"
                    value={formData.penName}
                    onChange={handleChange}
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Nationality *</Form.Label>
                  <Form.Control
                    name="nationality"
                    value={formData.nationality}
                    onChange={handleChange}
                    isInvalid={!!errors.nationality}
                  />
                  <Form.Control.Feedback type="invalid">{errors.nationality}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Birth Date (yyyy-mm-dd) *</Form.Label>
                  <Form.Control
                    name="birthDate"
                    value={formData.birthDate}
                    onChange={handleChange}
                    isInvalid={!!errors.birthDate}
                  />
                  <Form.Control.Feedback type="invalid">{errors.birthDate}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>Death Date</Form.Label>
                  <Form.Control
                    name="deathDate"
                    value={formData.deathDate}
                    onChange={handleChange}
                  />
                </Form.Group>

                <Button variant="primary" type="submit" className="w-100">
                  Submit
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default AddAuthorPage;