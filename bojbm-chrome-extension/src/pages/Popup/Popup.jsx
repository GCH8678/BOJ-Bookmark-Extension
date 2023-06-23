import React, { useState, useEffect } from 'react';
import './Popup.css';
import Login from './Login';
import App from './App';

const Popup = () => {
  const [isLoggedIn, setLoggedIn] = useState(false);
  useEffect(() => {
    chrome.storage.sync.get('isLoggedIn', (res) => setLoggedIn(res.isLoggedIn));
  }, []);

  return (
    <>
      <>
        <div className="App">
          {isLoggedIn && (
            <div className="PopupPage">
              <App setLoggedIn={setLoggedIn} />
            </div>
          )}
          {!isLoggedIn && (
            <div className="LoginPage">
              <Login setLoggedIn={setLoggedIn} />
            </div>
          )}
        </div>
      </>
    </>
  );
};

//<TodayProblem />;

export default Popup;
