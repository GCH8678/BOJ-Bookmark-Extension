import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom/client';

//import Button from 'react-bootstrap/Button';
import AddBookmarkModalButton from './modules/AddBookmarkButton';
import SetBookmarkModalButton from './modules/SetBookmarkButton';

const url = document.location.href;
const problemId = url.replace(/[^0-9]/g, '');
console.log(problemId);
console.log('content script work');

const BookmarkButton = () => {
  const [isBookmarked, setIsBookmarked] = useState(false);
  const [isLoggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    chrome.runtime.sendMessage(
      {
        action: 'isBookmarked',
        data: { problemId: problemId },
      },
      (res) => {
        setIsBookmarked(res);
        //console.log('isBookmarked : ' + res);
        chrome.storage.sync.get('isLoggedIn', (res) => {
          setLoggedIn(res.isLoggedIn);
          //console.log('isLoggedIn : ' + res.isLoggedIn);
        });
      }
    );
  }, []);

  return (
    <>
      {isLoggedIn && (
        <>
          {!isBookmarked ? (
            <AddBookmarkModalButton
              problemId={problemId}
              setIsBookmarked={setIsBookmarked}
            />
          ) : (
            <SetBookmarkModalButton
              problemId={problemId}
              setIsBookmarked={setIsBookmarked}
            />
          )}
        </>
      )}
    </>
  );
};

const title = document.getElementById('problem_title');
const bookmarkBtn = document.createElement('btn');
title.after(bookmarkBtn);
//document.body.prepend(bookmarkBtn);
ReactDOM.createRoot(bookmarkBtn).render(
  <React.StrictMode>
    <BookmarkButton />
  </React.StrictMode>
);
