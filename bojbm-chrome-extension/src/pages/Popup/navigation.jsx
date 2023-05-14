import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Button from 'react-bootstrap/Button';

const Navigation = () => {
  return (
    <>
      <Navbar bg="primary" variant="dark">
        <Container>
          <Navbar.Brand href="#home">B</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link>BOJ Bookmark</Nav.Link>
            <Button
              variant="outline-light"
              style={{ position: 'absolute', right: 0, marginRight: '10px' }}
            >
              Logout
            </Button>{' '}
          </Nav>
        </Container>{' '}
      </Navbar>
    </>
  );
};
export default Navigation;
