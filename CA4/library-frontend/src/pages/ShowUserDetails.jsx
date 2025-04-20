import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button, Container, Row, Col, Card, Spinner } from 'react-bootstrap';

function UserPage() {
  const [userData, setUserData] = useState(null);
  const navigate = useNavigate();

  const username = 'mat123'; // Replace with session-based logic

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const res = await axios.get(`http://localhost:9090/users/${username}`);
        setUserData(res.data.data);
      } catch (err) {
        console.error("Failed to fetch user data.", err);
      }
    };
    fetchUserData();
  }, [username]);

  const handleLogout = () => {
    navigate('/users/login');
  };

  const handleRead = (title) => {
    navigate(`/books/read/${title}`);
  };

  const handleBookClick = (title) => {
    navigate(`/books/details/${title}`);
  };

  const handleAuthorClick = (author) => {
    navigate(`/authors/details/${author}`);
  };

  return (
    <Container className="py-4">
      {userData ? (
        <>
          {/* User Info Section */}
          <Row className="mb-4">
            <Col md={8}>
              <h2>{userData.username}</h2>
              <p><strong>Email:</strong> {userData.email}</p>
              <p><strong>Balance:</strong> ${userData.balance}</p>
              <p><strong>Location:</strong> {userData.address?.userCity}, {userData.address?.userCountry}</p>
            </Col>
            <Col md={4} className="text-md-end">
              <Button variant="outline-danger" onClick={handleLogout}>
                Logout
              </Button>
            </Col>
          </Row>

          {/* Books Section */}
          <h3 className="mb-3">My Books</h3>
          {userData.books.length > 0 ? (
            <Row xs={1} sm={2} md={3} lg={4} className="g-4">
              {userData.books.map((book, idx) => (
                <Col key={idx}>
                  <Card className="h-100">
                    <Card.Img
                      variant="top"
                      src={`/images/${book.title}.jpg`}
                      alt={book.title}
                      style={{ cursor: 'pointer', height: '250px', objectFit: 'cover' }}
                      onClick={() => handleBookClick(book.title)}
                    />
                    <Card.Body>
                      <Card.Title
                        style={{ cursor: 'pointer' }}
                        onClick={() => handleBookClick(book.title)}
                      >
                        {book.title}
                      </Card.Title>
                      <Card.Subtitle
                        className="mb-2 text-muted"
                        style={{ cursor: 'pointer' }}
                        onClick={() => handleAuthorClick(book.author)}
                      >
                        {book.author}
                      </Card.Subtitle>
                      <Card.Text>
                        <strong>Publisher:</strong> {book.publisher} <br />
                        <strong>Year:</strong> {book.year} <br />
                        <strong>Genres:</strong> {book.category.join(', ')} <br />
                        <strong>Price:</strong> ${book.price} <br />
                        <strong>Status:</strong> {book.isBorrowed ? 'Borrowed (return due in X days)' : 'Owned'}
                      </Card.Text>
                      <Button variant="primary" onClick={() => handleRead(book.title)}>
                        Read
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
          ) : (
            <div className="text-center mt-4">
              <p className="text-muted">You don't have any books yet!</p>
              <img
                src="/images/empty-bookshelf.png"
                alt="No books"
                style={{ maxWidth: '250px', opacity: 0.7 }}
              />
            </div>
          )}
        </>
      ) : (
        <div className="text-center my-5">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3">Loading user data...</p>
        </div>
      )}
    </Container>
  );
}

export default UserPage;
