# BOJ-Bookmark-Extension

백준 문제 풀이 북마크 알람 서비스
<br>
<br>

## 프로젝트 소개
다시 풀어보고 싶은 백준 문제를 북마크 등록 해두면 정해진 시간 이후 알람이 오는 서비스를 위한 프로젝트입니다.
<br>
<br>


## 주요 기능
 - 오늘 풀어야 하는 문제 알림 기능(Mail을 통해 알림, Mail의 링크를 통해 문제 페이지로 이동 가능)
 - 크롬 확장프로그램 POPUP 창을 통해 오늘 풀어야 하는 문제 확인 가능
 - POPUP창의 문제 클릭시 해당 페이지로 이동
<br>
<br>

## 사용방법
1. Chrome Web Store 에서 BOJ Bookmark Extension 설치합니다.
- https://chrome.google.com/webstore/detail/boj-bookmark-extension/agniolplkhialjmfebfphdlgchnikpib?hl=ko&authuser=0
<br>

2. 크롬 확장을 설치 후 크롬 확장 프로그램을 고정해 줍니다.<br>
![extensionPin](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/305dd061-6464-4112-a372-5882c37362ac)

3. 고정이 완료되면 Popup창을 통해 로그인을 합니다.<br>
![login](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/80347961-1bb0-4f49-be2b-cb4bdd043d0f)

4. 회원이 아니라면 Sign Up/Find Password 버튼을 클릭하여 회원가입을 합니다. (새탭을 통해 열림)<br>
![signUp](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/cfacbc15-b3e3-48c2-bed8-e515dab0ae00)
<br>
- 비밀번호를 찾으려면 위 사진의 빨간색 화살표가 가리키는 링크를 클릭하면 비밀번호 찾기 페이지로 이동합니다. <br> 회원가입과 유사한 방법으로 비밀번호를 재설정 합니다. <br>
![findPw](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/abf826b7-38bd-4192-a0a9-cc2f70926d33)

5. 로그인 후 당일 풀어야하는 문제가 없다면 다음과 같이 표시됩니다.
<br> ![popupempty](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/84b621ab-cadc-49dd-b779-595043789344)

6. 문제를 북마크 하려면 백준 사이트의 풀려는 문제의 페이지로 이동합니다.<br>
북마크 되지 않은 문제는 아래 사진의 빨간 화살표와 같이 빈 북마크 표시로 표시됩니다.<br>
![nobookmarked](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/9cea75bf-299b-48a5-b3e1-0ad5a42a0b3f)

7. 빈 북마크 표시를 클릭하면 다음과 같이 팝업이 나타납니다.<br>
AfterDay를 선택하거나 또는 캘린더 표시를 클릭하여 알림을 받을 일자를 선택한 뒤 '저장' 버튼을 클릭합니다.<br>
![bookmarkAdd](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/ee019efb-3d92-42b6-a6a1-7ca70bec2f62)

8. 북마크가 완료되면 다음과 같이 비어있던 북마크 표시가 채워집니다.<br>
![bookmarked](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/e57266fd-8b3b-484a-9885-efb842079370)

9. 채워진 북마크 표시를 다시 클릭하면 다음과 같이 알림일자를 수정하거나 북마크를 취소할 수 있는 팝업이 나타납니다.<br>
![bookmarkDel](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/a984b638-9d94-41fb-b01b-77fdee285089)

10. 오늘 풀어야 하는 문제가 있다면 Popup창에서 다음과 같이 나타납니다.<br>
클릭하면 해당 백준 문제 페이지로 이동합니다.<br>
![popup](https://github.com/GCH8678/boj-bookmark-extension/assets/95828987/6c3b7f26-ef73-4fa2-aff5-1608f69f6499)

<br>
<br>
<br>


### ⚙️ 개발 환경

# Backend
- `Java 17`
- `corretto-17.0.6`
- **IDE** : intellij
- **Framework** : Spring Boot(3.0)
- **Database** : MySQL
- **ORM** : Jpa
- **Dependencies** : Spring Security, Jwt

# Frontend

- `html, css, javascript`
- **IDE** : Visual Studio Code
- **Framework** : React
- **section** : chrome extension, frontend
- <del>**plugin** : webpack,</del>
<br><br>