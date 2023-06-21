import React, { useEffect } from 'react';
import Navigation from './Navigation';
import TodayProblems from './TodayProblems';

const App = ({ setLoggedIn }) => {
  //   useEffect(() => {
  //     chrome.runtime.sendMessage(
  //       {
  //         action: 'checkAccessToken',
  //       },
  //       (res) => {
  //         setLoggedIn(res);
  //       }
  //     );
  //   }, []);

  return (
    <>
      <Navigation setLoggedIn={setLoggedIn} />
      <TodayProblems />
    </>
  );
};

export default App;
