import React from 'react';
import ReactDOM from 'react-dom/client';
import Button from 'react-bootstrap/Button';




const BookmarkButton = ()=>{
    return(
        <>
            <Button variant="outline-secondary">☆</Button>{' '}
        </>
    )
}
// 해당 문제 들어왔을 때
// backgrond로 해당 url을 보낸뒤 problemID를 가지고 bookmark등록된 문제인지 백엔드와 api 통신
// => 등록 상태에 따라 아이콘 다르게 (bootstrap 아이콘 사용)
// ex) https://www.acmicpc.net/problem/2839

console.log("content script work")

const url = document.location.href
const problemId = url.replace(/[^0-9]/g,"");
console.log(problemId)



const title = document.getElementById("problem_title");
const bookmarkBtn = document.createElement('btn');
title.after(bookmarkBtn)

//document.body.prepend(bookmarkBtn);
ReactDOM.createRoot(bookmarkBtn).render(
    <React.StrictMode>
        <BookmarkButton/>
    </React.StrictMode>
)
