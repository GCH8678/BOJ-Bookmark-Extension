import React from 'react';
import './Popup.css';
import 'bootstrap/dist/css/bootstrap.css';
import Navigation from './navigation';

const Popup = () => {
  return (
    <div className="App">
      <Navigation />
      <button id="#sign-in"></button>
    </div>
  );
};

export default Popup;
