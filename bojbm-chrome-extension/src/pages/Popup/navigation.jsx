import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

const Navigation = () => {
  return (
    <>
      <Navbar bg="primary" variant="dark">
        <Container>
          <Navbar.Brand href="#home">B</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="#home">오늘의 문제</Nav.Link>
            <Nav.Link href="#features">logout</Nav.Link>
          </Nav>
        </Container>{' '}
      </Navbar>
    </>
  );
};
export default Navigation;
