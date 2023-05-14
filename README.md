# BOJ-Bookmark-Extension
백준 문제 풀이 북마크 알람 서비스
<br>
<br>
<br>

## 🖥️ 프로젝트 소개
다시 풀어보고 싶은 백준 문제를 북마크 등록 해두면 정해진 시간 이후 알람이 오는 서비스를 위한 프로젝트입니다. 

<br>
<br>



### ⚙️ 개발 환경
# Backend
- `Java 17`
- `corretto-17.0.6`
- **IDE** : intellij
- **Framework** : Spring Boot(3.x)
- **Database** : H2 DB -> 이후 MySQL로 바꿀예정
- **ORM** : Jpa
- **Dependencies** : Spring Security, Jwt

# Frontend 
- `html, css, javascript`
- **IDE** : Visual Studio Code
- **Framework** : React
- **section** : chrome extension, frontend
- <del>**plugin** : webpack,</del> 

<br><br>

## 📌 구현 중인 주요 기능들
#### 로그인 - <del><a href="https://github.com/GCH8678/BOJ-Bookmark-Extension-/wiki/%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C-(-Login-)" > 상세보기(Wiki 링크) </a></del>
- Spring Security
- 로그인 시 AccessToken 생성 <del>RefreshToken은 이후 구현</del>
- <del>OAuth2.0, jwt을 이용한 Google 계정으로 로그인</del>
