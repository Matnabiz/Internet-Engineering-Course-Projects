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
import NavBar from './Navbar';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar as solidStar, faStarHalfAlt } from '@fortawesome/free-solid-svg-icons';
import { faStar as regularStar } from '@fortawesome/free-regular-svg-icons';


function BookDetailsPage() {
  const { bookTitle } = useParams();
  const username = 'mat123'; // Replace with actual logged-in user
  const [book, setBook] = useState(null);
  const [status, setStatus] = useState('Available');
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
      const res = await axios.get(`http://localhost:9090/books/details/${bookTitle}`);
      setBook(res.data.data);
      setFinalPrice(res.data.data.price);

      const ownershipRes = await axios.get(`http://localhost:9090/book/status/${username}/${bookTitle}`);
      setStatus(ownershipRes.data.status);
      setUserCanReview(ownershipRes.data.canReview);
    } catch (err) {
      toast.error("Failed to load book data.");
    }
  };

  useEffect(() => {
    fetchBookDetails();
  }, [bookTitle]);

  const handleSubmitReview = async () => {
    try {
      const res = await axios.post(`http://localhost:9090/review/${bookTitle}`, {
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
      await axios.post(`http://localhost:9090/cart/buy/add`, {
        username,
        bookTitle
      });
      toast.success("Book added to cart!");
      setShowCartModal(false);
    } catch (err) {
      toast.error("Failed to add book to cart.");
    }
  };

  const updateFinalPrice = (days) => {
    const basePrice = book.price;
    setFinalPrice(Math.round(basePrice * (days / 10)));
  };

  if (!book) return <div className="text-center mt-5">Loading...</div>;

  const startIndex = (currentPage - 1) * reviewsPerPage;
  const currentReviews = book.reviews.slice(startIndex, startIndex + reviewsPerPage);

  const renderStars = (rating) => {
    const stars = [];
    const fullStars = Math.floor(rating);
    const hasHalf = rating - fullStars >= 0.5;
  
    for (let i = 0; i < fullStars; i++) {
      stars.push(<FontAwesomeIcon key={`full-${i}`} icon={solidStar} className="text-warning me-1" />);
    }
  
    if (hasHalf) {
      stars.push(<FontAwesomeIcon key="half" icon={faStarHalfAlt} className="text-warning me-1" />);
    }
  
    const emptyStars = 5 - stars.length;
    for (let i = 0; i < emptyStars; i++) {
      stars.push(<FontAwesomeIcon key={`empty-${i}`} icon={regularStar} className="text-warning me-1" />);
    }
  
    return stars;
  };
  
  return (
    <>
      <NavBar />
      <Container className="book-details-container my-4">
        <ToastContainer position="top-center" />
        <Row>
          <Col md={3}>
            <Card>
              <div className="position-relative">
                <Card.Img src={`/images/${bookTitle}.jpg`} alt="Book" />
                <Badge
                  bg={
                    status === 'Owned'
                      ? 'success'
                      : status === 'Borrowed'
                      ? 'warning'
                      : 'secondary'
                  }
                  className="position-absolute top-0 start-0 m-2"
                >
                  {status}
                </Badge>
              </div>
            </Card>
          </Col>

          <Col md={8}>
            <h2>{bookTitle}</h2>
            <div className="mb-3">
              {renderStars(book.averageRating)} <span className="text-muted">({book.averageRating.toFixed(1)})</span>
            </div>
            <Row>
              <Col><strong>Author</strong></Col>
              <Col><strong>Publisher</strong></Col>
              <Col><strong>Year</strong></Col>
            </Row>
            <Row className="mb-3">
              <Col>{book.author}</Col>
              <Col>{book.publisher}</Col>
              <Col>{book.year}</Col>
            </Row>

            <Row>
              <Col>
                <strong>About:</strong>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit...</p>
              </Col>
            </Row>

            <div className="mt-3 mb-4">
              <Button
                className="alaki text-black border-2 rounded-2 me-5"
                onClick={() => setShowCartModal(true)}
                disabled={status !== 'Available'}
              >
                Add to Cart
              </Button>
            </div>
          </Col>
        </Row>

        <hr />
        <div className="d-flex justify-content-between align-items-center">
          <h4>Reviews ({book.reviews.length})</h4>
          <Button
            variant="dark"
            onClick={() => setShowReviewModal(true)}
            disabled={!userCanReview}
          >
            Add Review
          </Button>
        </div>

        {!userCanReview && (
          <p className="text-danger mt-2">
            You must purchase or borrow the book to review it.
          </p>
        )}

        {currentReviews.map((rev, i) => (
          <Card key={i} className="mb-2">
            <Card.Body>
              <Card.Title>{rev.username} ⭐ {rev.rate}</Card.Title>
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
                <Form.Control as="textarea" value={comment} onChange={e => setComment(e.target.value)} />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowReviewModal(false)}>Cancel</Button>
            <Button variant="primary" onClick={handleSubmitReview} disabled={!rating || !comment}>Submit</Button>
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
                {[2, 4, 6, 8, 10].map(days => (
                  <Form.Check
                    key={days}
                    inline
                    label={`${days} days`}
                    type="radio"
                    name="borrowDays"
                    onChange={() => {
                      setBorrowDays(days);
                      updateFinalPrice(days);
                    }}
                  />
                ))}
                <p className="mt-2">Final Price: ${finalPrice}</p>
              </div>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowCartModal(false)}>Cancel</Button>
            <Button variant="success" onClick={handleAddToCart}>Add to Cart</Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </>
  );
}
export default BookDetailsPage;
