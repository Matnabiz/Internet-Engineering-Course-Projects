import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {
  Container, Row, Col, Card, Badge, Button, Modal,
  Form, Pagination
} from 'react-bootstrap';
import axios from 'axios';
import './styles/BookDetailsStyle.css';

function BookDetailsPage() {
  const { title } = useParams();
  const username = 'mat123'; // Replace with actual logged-in user
  const [book, setBook] = useState(null);
  const [status, setStatus] = useState('Available'); // Available, Owned, Borrowed
  const [userCanReview, setUserCanReview] = useState(false);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [showCartModal, setShowCartModal] = useState(false);

  const [rating, setRating] = useState('');
  const [comment, setComment] = useState('');
  const [borrowChecked, setBorrowChecked] = useState(false);
  const [borrowDays, setBorrowDays] = useState(null);
  const [finalPrice, setFinalPrice] = useState(0);

  const [currentPage, setCurrentPage] = useState(1);
  const reviewsPerPage = 4;

  const fetchBookDetails = async () => {
    try {
      const res = await axios.get(`http://localhost:9090/books/details/${title}`);
      setBook(res.data.data);
      setFinalPrice(res.data.data.price);

      // Simulate ownership (replace with actual backend logic)
      const ownershipRes = await axios.get(`http://localhost:9090/book/status/${username}/${title}`);
      setStatus(ownershipRes.data.status); // Owned | Borrowed | Available
      setUserCanReview(ownershipRes.data.canReview);
    } catch (err) {
      toast.error("Failed to load book data.");
    }
  };

  useEffect(() => {
    fetchBookDetails();
  }, [title]);

  const handleSubmitReview = async () => {
    try {
      const res = await axios.post(`http://localhost:9090/review/${title}`, {
        username,
        rate: parseInt(rating),
        comment
      });
      toast.success("Review added successfully!");
      setBook(prev => ({
        ...prev,
        reviews: [...prev.reviews, res.data.review],
        averageRating: res.data.updatedAverage
      }));
      setShowReviewModal(false);
      setRating('');
      setComment('');
    } catch (err) {
      toast.error("Failed to add review.");
    }
  };

  const handleAddToCart = async () => {
    try {
      await axios.post(`http://localhost:9090/cart/add`, {
        username,
        title,
        borrow: borrowChecked,
        borrowDays
      });
      toast.success("Book added to cart!");
      setShowCartModal(false);
    } catch (err) {
      toast.error("Failed to add book to cart.");
    }
  };

  const updateFinalPrice = (days) => {
    const basePrice = book.price;
    setFinalPrice(Math.round(basePrice * (days / 10))); // Simple logic
  };

  if (!book) return <div className="text-center mt-5">Loading...</div>;

  const startIndex = (currentPage - 1) * reviewsPerPage;
  const currentReviews = book.reviews.slice(startIndex, startIndex + reviewsPerPage);

  return (
    <Container className="book-details-container my-4">
      <ToastContainer position="top-center" />
      <Row>
        <Col md={4}>
          <Card>
            <div className="position-relative">
              <Card.Img src={`/images/${title}.jpg`} alt="Book" />
              <Badge bg={status === 'Owned' ? 'success' : status === 'Borrowed' ? 'warning' : 'secondary'}
                className="position-absolute top-0 start-0 m-2">
                {status}
              </Badge>
            </div>
          </Card>
        </Col>
        <Col md={8}>
          <h2>{title}</h2>
          <p><strong>Author:</strong> {book.author}</p>
          <p><strong>Publisher:</strong> {book.publisher} ({book.year})</p>
          <p><strong>Genres:</strong> {book.genres.join(', ')}</p>
          <p><strong>Price:</strong> ${book.price}</p>
          <p><strong>Summary:</strong> Lorem ipsum dolor sit amet, consectetur adipiscing elit...</p>
          <p><strong>Avg. Rating:</strong> ⭐ {book.averageRating.toFixed(1)}</p>
          <div className="mt-3">
            <Button className="alaki text-black border-2 rounded-2 me-5" onClick={() => setShowCartModal(true)}
              disabled={status !== 'Available'}>
              Add to Cart
            </Button>
            <Button variant="dark" onClick={() => setShowReviewModal(true)}
              disabled={!userCanReview}>
              Add Review
            </Button>
            {!userCanReview && (
              <p className="text-danger mt-2">
                You must purchase or borrow the book to review it.
              </p>
            )}
          </div>
        </Col>
      </Row>

      <hr />
      <h4>Reviews ({book.reviews.length})</h4>
      {currentReviews.map((rev, i) => (
        <Card key={i} className="mb-2">
          <Card.Body>
            <Card.Title>{rev.username}  ⭐ {rev.rate}</Card.Title>
            <Card.Text>{rev.comment}</Card.Text>
          </Card.Body>
        </Card>
      ))}

      {book.reviews.length > reviewsPerPage && (
        <Pagination>
          {[...Array(Math.ceil(book.reviews.length / reviewsPerPage))].map((_, i) => (
            <Pagination.Item
              key={i + 1}
              active={i + 1 === currentPage}
              onClick={() => setCurrentPage(i + 1)}
            >
              {i + 1}
            </Pagination.Item>
          ))}
        </Pagination>
      )}

      {/* Review Modal */}
      <Modal show={showReviewModal} onHide={() => setShowReviewModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Submit Review</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Rating</Form.Label>
              <Form.Select value={rating} onChange={e => setRating(e.target.value)}>
                <option value="">Select</option>
                {[1, 2, 3, 4, 5].map(n => <option key={n} value={n}>{n}</option>)}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Comment</Form.Label>
              <Form.Control as="textarea" value={comment}
                onChange={e => setComment(e.target.value)} />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowReviewModal(false)}>Cancel</Button>
          <Button variant="primary" onClick={handleSubmitReview} disabled={!rating || !comment}>
            Submit
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Cart Modal */}
      <Modal show={showCartModal} onHide={() => setShowCartModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add to Cart</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p><strong>Price:</strong> ${book.price}</p>
          <Form.Check
            type="checkbox"
            label="Borrow instead of buy"
            checked={borrowChecked}
            onChange={e => setBorrowChecked(e.target.checked)}
          />
          {borrowChecked && (
            <div className="mt-2">
              <Form.Check
                inline label="2 days" type="radio" name="borrowDays"
                onChange={() => { setBorrowDays(2); updateFinalPrice(2); }}
              />
              <Form.Check
                inline label="4 days" type="radio" name="borrowDays"
                onChange={() => { setBorrowDays(4); updateFinalPrice(4); }}
              />
              <Form.Check
                inline label="6 days" type="radio" name="borrowDays"
                onChange={() => { setBorrowDays(6); updateFinalPrice(6); }}
              />
              
              <Form.Check
                inline label="8 days" type="radio" name="borrowDays"
                onChange={() => { setBorrowDays(8); updateFinalPrice(8); }}
              />

             <Form.Check
                inline label="10 days" type="radio" name="borrowDays"
                onChange={() => { setBorrowDays(10); updateFinalPrice(10); }}
              />

              <p className="mt-2">Final Price: ${finalPrice}</p>
            </div>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowCartModal(false)}>Cancel</Button>
          <Button variant="success" onClick={handleAddToCart}>
            Add to Cart
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default BookDetailsPage;
