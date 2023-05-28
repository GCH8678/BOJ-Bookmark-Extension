import React from 'react';
import ReactDOM from 'react-dom/client';
import Button from 'react-bootstrap/Button';

console.log('content script work');

const url = document.location.href;
const problemId = url.replace(/[^0-9]/g, '');
console.log(problemId);

//const [isBookmarked,setIsBookmarked] = useState(false);
//const [isLoggedIn,setLoggedIn] = useState(false);

//useEffect(해당 페이지 문제가 등록되있는지 서버와 통신해서) useState로 선언했던 값을 변환하여 Bookmark버튼 반환값 변환
// useEffect(() => {
//   chrome.runtime.sendMessage(
  //   {
  //     action: 'isBookmarked',
  //     data: { problemId: problemId },
  //   },
  //   (res) => {
  //     setIsBookmarked(res);
  //     console.log(res);
  //   }
  // chrome.storage.sync.get('isLoggedIn', (res) => setLoggedIn(res.isLoggedIn));
// });


const onAddBookmarkHandler = () => {

  chrome.runtime.sendMessage(
    {
      action: 'addBookmark',
      data: { problemId: problemId },
    },
    (res) => {
      console.log(res);
      //
    }
  );
};

// const onDeleteBookmarkHandler = () =>{
//   chrome.runtime.sendMessage(
//     {
//       action: 'deleteBookmark',
//       data: { problemId: problemId },
//     },
//     (res) => {
//       console.log(res);
//     }
//   );
// };


// Popup.jsx 에서 사용했던 방법

// {isLoggedIn && (
//   <>
//     <Navigation setLoggedIn={setLoggedIn} />
//     <TodayProblem />
//   </>
// )}
// {!isLoggedIn && <Login setLoggedIn={setLoggedIn} />}

// => {isBookmarked ? ★ : ☆ }

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
