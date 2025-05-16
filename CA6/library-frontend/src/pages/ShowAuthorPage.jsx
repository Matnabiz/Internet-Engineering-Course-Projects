import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './styles/ShowAuthorPageStyle.css';
import { Container, Row, Col, Card, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';


function ShowAuthor() {
  const navigate = useNavigate();
  const { authorName } = useParams();
  const [authorData, setAuthorData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("sessionToken");
    const fetchAuthor = async () => {
      try {
        const res = await axios.get(`http://localhost:9090/authors/details/${authorName}`,{
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setAuthorData(res.data.data);
      } catch (err) {
        if (err.response && err.response.status === 401) {
          navigate("/users/login");
        }else{
        console.error("Failed to fetch author:", err);
        }
      } finally {
        setLoading(false);
      }
    };
    fetchAuthor();
  }, [authorName,navigate]);

  if (loading) {
    return <div className="text-center mt-5"><Spinner animation="border" /></div>;
  }

  return (
    <Container className="author-page">
      <div className="author-profile mb-5">
        <h2>{authorData.name}</h2>
        <p><strong>Pen Name:</strong> {authorData.penName}</p>
        <p><strong>Nationality:</strong> {authorData.nationality}</p>
        <p><strong>Born:</strong> {new Date(authorData.born).toLocaleDateString()}</p>
      </div>

      <h4 className="mb-4">📚 Books by {authorData.name}</h4>
      <Row>
        {authorData.books.map((book, idx) => (
          <Col md={6} lg={4} key={idx} className="mb-4">
            <Card className="book-card h-100">
              <Card.Body>
                <Card.Title>{book.title}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">{book.publisher} ({book.year})</Card.Subtitle>
                <Card.Text>{book.synopsis}</Card.Text>
                <div className="genres mb-2">
                  {book.genres.map((g, i) => (
                    <span key={i} className="genre-badge">{g}</span>
                  ))}
                </div>
                <strong>Price: </strong>${book.price}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
}

export default ShowAuthor;
