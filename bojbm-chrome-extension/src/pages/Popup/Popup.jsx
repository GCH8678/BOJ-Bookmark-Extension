import React, { useState, useEffect } from 'react';
import './Popup.css';
import 'bootstrap/dist/css/bootstrap.css';
import Navigation from './navigation';
import Login from './Login';
import TodayProblem from './TodayProblem';

const Popup = () => {
  const [isLoggedIn, setLoggedIn] = useState(false);

  chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    //로그인
    if (request.action == 'logoutInBackground') {
      setLoggedIn[false];
    }
    if (request.action == 'loginInBackground') {
      setLoggedIn[true];
    }
  });

  return (
    <>
      <div className="App">
        {isLoggedIn && (
          <>
            <Navigation />
            <TodayProblem />
          </>
        )}
        {!isLoggedIn && <Login />}
      </div>
    </>
  );
};

//<TodayProblem />;

export default Popup;
