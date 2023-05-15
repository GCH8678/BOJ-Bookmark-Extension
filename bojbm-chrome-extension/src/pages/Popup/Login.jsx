import React, { useState } from 'react';

const Login = () => {
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
    let body = {
      email: Email,
      password: Password,
    };
    console.log(body);
    chrome.runtime.sendMessage(
      {
        action: 'login',
        data: body,
      },
      (res) => {
        console.log(res);
      }
    );
  };

  return (
    <>
      <div className="Auth-form-container">
        <form className="Auth-form" onSubmit={onSubmitHandler}>
          <div className="Auth-form-content">
            <h3 className="Auth-form-title">Sign In</h3>
            <div className="form-group mt-3">
              <label>Email address</label>
              <input
                type="email"
                className="form-control mt-1"
                placeholder="Enter email"
                value={Email}
                onChange={onEmailHandler}
              />
            </div>
            <div className="form-group mt-3">
              <label>Password</label>
              <input
                type="password"
                className="form-control mt-1"
                placeholder="Enter password"
                value={Password}
                onChange={onPasswordHandler}
              />
            </div>
            <div className="d-grid gap-2 mt-3">
              <button type="submit" className="btn btn-primary">
                Login
              </button>
            </div>
          </div>
        </form>
      </div>
    </>
  );
};

export default Login;
