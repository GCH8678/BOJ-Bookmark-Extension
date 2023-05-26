import React, { useState } from 'react';

const Login = ({ setLoggedIn }) => {
  const [Email, setEmail] = useState('');
  const [Password, setPassword] = useState('');

  const onEmailHandler = (event) => {
    setEmail(event.currentTarget.value);
  };
  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value);
  };

  const onSubmitHandler = (event) => {
    console.log('Email', Email);
    console.log('Password', Password);
    const email = Email;
    const password = Password;

    chrome.runtime.sendMessage(
      {
        action: 'login',
        data: { email, password },
      },
      (res) => {
        chrome.storage.sync.set({ isLoggedIn: res }, () => {
          setLoggedIn(res);
          console.log(res);
        });
      }
    );
  };

  return (
    <>
      <div className="Auth-form-container">
        <div className="Auth-form">
          <div className="Auth-form-content">
            <div className="form-group mt-3">
              <input
                type="email"
                className="form-control mt-1"
                placeholder="Enter email"
                value={Email}
                onChange={onEmailHandler}
              />
            </div>
            <div className="form-group mt-3">
              <input
                type="password"
                className="form-control mt-1"
                placeholder="Enter password"
                value={Password}
                onChange={onPasswordHandler}
              />
            </div>
            <div className="d-grid gap-2 mt-3">
              <button
                type="submit"
                className="btn btn-primary"
                onClick={onSubmitHandler}
              >
                Login
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Login;
