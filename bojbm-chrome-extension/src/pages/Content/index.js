import React from 'react';
import ReactDOM from 'react-dom/client';
import Button from 'react-bootstrap/Button';

console.log('content script work');

const url = document.location.href;
const problemId = url.replace(/[^0-9]/g, '');
console.log(problemId);

const onAddBookmarkHandler = () => {
  // chrome.runtime.sendMessage(
  //     {
  //       action: 'login',
  //       data: { email, password },
  //     },
  //     (res) => {
  //       chrome.storage.sync.set({ isLoggedIn: res }, () => {
  //         setLoggedIn(res);
  //         console.log(res);
  //       });
  //     }
  //   );

  chrome.runtime.sendMessage(
    {
      action: 'addBookmark',
      data: { problemId: problemId },
    },
    (res) => {
      console.log(res);
    }
  );
};

const BookmarkButton = () => {
  return (
    <>
      <Button variant="outline-secondary" onClick={onAddBookmarkHandler}>
        ☆
      </Button>{' '}
    </>
  );
};
// 해당 문제 들어왔을 때
// backgrond로 해당 url을 보낸뒤 problemID를 가지고 bookmark등록된 문제인지 백엔드와 api 통신
// TODO: 등록 상태에 따라 아이콘 다르게 (bootstrap 아이콘 사용)

const title = document.getElementById('problem_title');
const bookmarkBtn = document.createElement('btn');
title.after(bookmarkBtn);
//document.body.prepend(bookmarkBtn);
ReactDOM.createRoot(bookmarkBtn).render(
  <React.StrictMode>
    <BookmarkButton />
  </React.StrictMode>
);
