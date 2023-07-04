import React, { useEffect, useState } from 'react';
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

  const [state, setState] = useState('today'); // 오늘풀 문제, 이후로 풀 문제, 지나간 문제 구별 previous, today, after
  useEffect(() => {
    chrome.runtime.sendMessage(
      chrome.runtime.sendMessage({
        action: 'bookmark',
      }),
      (res) => {
        setState();
      }
    );
  });

  return (
    <>
      <Navigation setLoggedIn={setLoggedIn} />
      <TodayProblems />
    </>
  );
};

export default App;
