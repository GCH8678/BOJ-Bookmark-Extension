import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Button from 'react-bootstrap/Button';

const Navigation = ({ setLoggedIn }) => {
  const logout = () => {
    chrome.runtime.sendMessage(
      {
        action: 'logout',
      },
      (res) => {
        //props.setLoggedIn(false);
        //console.log(res);
        chrome.storage.sync.set({ isLoggedIn: false }, (res) => {
          setLoggedIn(false);
        });
      }
    );
  };
  // 이후 Nav.Link 대신 => 다른 일자를 선택할 수 있게 ( 기본 값은 오늘 )
  return (
    <>
      <Navbar bg="primary" variant="dark">
        <Container>
          <Navbar.Brand href="#home">B</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="https://www.acmicpc.net/problemset" target="_blank">
              Go to BOJ
            </Nav.Link>
            <Button
              variant="outline-light"
              style={{ position: 'absolute', right: 0, marginRight: '10px' }}
              onClick={logout}
            >
              Logout
            </Button>
          </Nav>
        </Container>
      </Navbar>
    </>
  );
};
export default Navigation;
