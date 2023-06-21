import React, { useState, useRef, useEffect } from 'react';
import Button from 'react-bootstrap/Button';

import Snackbar from '@mui/material/Snackbar';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

const Login = ({ setLoggedIn }) => {
  const [Email, setEmail] = useState('');
  const [Password, setPassword] = useState('');
  const [snackbarOpen, setSnackbarOpen] = useState(false);

  //useRef 호출
  const emailRef = useRef(null);
  const passwordRef = useRef(null);

  const onEmailHandler = (event) => {
    setEmail(event.currentTarget.value);
  };
  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value);
  };

  const handleCloseSnackbar = () => {
    setSnackbarOpen(false);
  };

  useEffect(() => {
    emailRef.current.focus();
  }, []);

  const onSubmitHandler = () => {
    //console.log('Email', Email);
    //console.log('Password', Password);
    const email = Email;
    const password = Password;

    chrome.runtime.sendMessage(
      {
        action: 'login',
        data: { email, password },
      },
      (res) => {
        if (res == false) {
          emailRef.current.focus();
          setPassword('');
          setSnackbarOpen(true);
        }
        chrome.storage.sync.set({ isLoggedIn: res }, () => {
          setLoggedIn(res);
          //console.log(res);
        });
      }
    );
  };

  // 엔터 키 입력시 로그인 처리
  const handleEnter = (e) => {
    if (e.key == 'Enter') {
      if (snackbarOpen) {
        setSnackbarOpen(false);
      } else {
        onSubmitHandler();
      }
    }
  };

  const snackbarAction = (
    <React.Fragment>
      <IconButton
        size="small"
        aria-label="close"
        color="inherit"
        onClick={handleCloseSnackbar}
      >
        <CloseIcon fontSize="small" />
      </IconButton>
    </React.Fragment>
  );

  return (
    <>
      <div className="Auth-form-container">
        <div className="Auth-form">
          <div className="Auth-form-content">
            <div>
              <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="로그인에 실패하였습니다. (Enter)"
                action={snackbarAction}
              />
            </div>
            <div className="form-group mt-3">
              <input
                type="email"
                className="form-control mt-1"
                placeholder="Enter email"
                value={Email}
                onChange={onEmailHandler}
                onKeyDown={handleEnter}
                ref={emailRef}
              />
            </div>
            <div className="form-group mt-3">
              <input
                type="password"
                className="form-control mt-1"
                placeholder="Enter password"
                value={Password}
                onChange={onPasswordHandler}
                onKeyDown={handleEnter}
                ref={passwordRef}
              />
            </div>
            <div className="d-grid gap-2 mt-3">
              <Button onClick={onSubmitHandler} variant="primary">
                Login
              </Button>
              <Button
                href={
                  'chrome-extension://' + chrome.runtime.id + '/signup.html'
                }
                target="_blank"
                variant="secondary"
              >
                Sign Up / Find Password
              </Button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Login;
