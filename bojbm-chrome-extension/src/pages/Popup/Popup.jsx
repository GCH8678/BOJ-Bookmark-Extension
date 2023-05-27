import React, { useState, useEffect } from 'react';
import './Popup.css';
import 'bootstrap/dist/css/bootstrap.css';
import Navigation from './navigation';
import Login from './Login';
import TodayProblem from './TodayProblem';

const Popup = () => {
  const [isLoggedIn, setLoggedIn] = useState(false);
  useEffect(() => {
    chrome.storage.sync.get('isLoggedIn', (res) => setLoggedIn(res.isLoggedIn));
  });
  // chrome.storage.sync.get('isLoggedIn', (res) => {
  //   setLoggedIn(res);
  //   console.log('setLoggedIn : ' + res);
  // });
  return (
    <>
      <div className="App">
        {isLoggedIn && (
          <>
            <Navigation setLoggedIn={setLoggedIn} />
            <TodayProblem />
          </>
        )}
        {!isLoggedIn && <Login setLoggedIn={setLoggedIn} />}
      </div>
    </>
  );
};

//<TodayProblem />;

export default Popup;
