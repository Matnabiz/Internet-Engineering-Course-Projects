  import axios from 'axios';
  import React, { useEffect, useState, useCallback } from 'react';
  import { useParams, useNavigate } from 'react-router-dom';
  import { Container, Row, Col, Card, Button, Alert, Spinner } from 'react-bootstrap';
  import { ToastContainer, toast } from 'react-toastify';
  //import './ShowCartPage.css';

  function ShowCartPage() {
    const { username } = useParams();
    const navigate = useNavigate();
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchCart = useCallback(async () => {
      try {
        const res = await axios.get(`http://localhost:9090/cart/show/${username}`);
        setCart(res.data.data);
      } catch (err) {
        toast.error(err.response?.data?.message || "Failed to fetch cart.");
      } finally {
        setLoading(false);
      }
    }, [username]);

    const removeItem = async (bookTitle) => {
      try {
        await axios.delete(`http://localhost:9090/cart/remove/${username}/${bookTitle}`);
        toast.success("Item removed from cart.");
        fetchCart();
      } catch (err) {
        toast.error(err.response?.data?.message || "Failed to remove item.");
      }
    };

    const handleBuy = () => {
      if (cart.totalCost > cart.balance) {
        toast.error("Insufficient balance to complete the purchase.");
        return;
      }
      // Add your buy logic here
      toast.success("Purchase completed successfully!");
    };

    useEffect(() => {
      fetchCart();
    }, [fetchCart]);

    if (loading) {
      return <div className="text-center mt-5"><Spinner animation="border" /></div>;
    }

    if (!cart || cart.items.length === 0) {
      return (
        <Container className="empty-cart text-center">
          <h2>Your cart is empty 🛒</h2>
          <img src="/images/empty-cart.png" alt="Empty cart" className="img-fluid" style={{ maxWidth: '400px' }} />
          <ToastContainer position="top-center" />
        </Container>
      );
    }

    return (
      <Container className="my-5">
        <ToastContainer position="top-center" />
        <h3 className="mb-4">🛒 Your Cart</h3>
        <Row>
          {cart.items.map((item, index) => (
            <Col md={6} lg={4} className="mb-4" key={index}>
              <Card className="h-100 cart-item">
                <Card.Body onClick={() => navigate(`/book/${item.title}`)} style={{ cursor: 'pointer' }}>
                  <Card.Title>{item.title}</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">{item.author}</Card.Subtitle>
                  <Card.Text>
                    <strong>Publisher:</strong> {item.publisher}<br />
                    <strong>Year:</strong> {item.year}<br />
                    <strong>Genres:</strong> {item.genres.join(', ')}<br />
                    {item.isBorrowed ? (
                    <>
                      <strong>Original Price:</strong>{" "}
                      <span className="text-muted text-decoration-line-through">${item.price}</span>
                      <br />
                      <strong>Borrowed Price:</strong> ${item.finalPrice} for {item.borrowDays} days
                    </>
                  ) : (
                    <>
                      <strong>Price:</strong> ${item.finalPrice}
                    </>
                  )}

                  </Card.Text>
                </Card.Body>
                <Card.Footer className="d-flex justify-content-between">
                  <Button variant="danger" size="sm" onClick={() => removeItem(item.title)}>Remove</Button>
                </Card.Footer>
              </Card>
            </Col>
          ))}
        </Row>

        <hr />
        <h4>Total Cost: ${cart.totalCost}</h4>
        {cart.totalCost > cart.balance && (
          <Alert variant="danger">
            You don’t have enough balance to complete this purchase. Please add more funds.
          </Alert>
        )}
        <div className="d-flex justify-content-end">
          <Button variant="primary" onClick={handleBuy} disabled={cart.totalCost > cart.balance}>
            Confirm Purchase
          </Button>
        </div>
      </Container>
    );
  }

  export default ShowCartPage;
