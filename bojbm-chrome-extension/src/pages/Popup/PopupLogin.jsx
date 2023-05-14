import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

export default function Login() {
  const [user, setUser] = useState({});
  const [errMsg, setErrMsg] = useState();
  const [inputs, setInputs] = useState({
    email: '',
    password: '',
    //,autoLogin: false,
  });

  // background에 저장된 유저정보가 있다면 불러옴 (로그인 유지)
  useEffect(() => {
    chrome.storage.sync.get('user', function (res) {
      setUser(res.user);
    });
  }, []);

  const onChangeHandler = (e) => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const onLogin = () => {
    const { email, password } = inputs;
    chrome.runtime.sendMessage(
      {
        action: 'login', // background에 보내는 메세지 제목
        data: {
          email,
          password,
        },
      },
      (response) => {
        // 로그인 성공 => 유저정보 불러옴
        if (!response.length) {
          chrome.storage.sync.get('user', function (res) {
            setUser(res.user);
          });
          return;
        }
        // 실패 => 에러 메세지 출력
        setErrMsg('아이디와 비밀번호를 확인해주세요.');
      }
    );
  };

  if (!user) {
    return (
      <Popup>
        <Logo src={`${process.env.PUBLIC_URL}/assets/logo-pink.png`} />
        {errMsg ? <Err> {errMsg}</Err> : null}
        <InputWrapper>
          <Input
            type="email"
            name="email"
            onChange={onChangeHandler}
            placeholder="아이디"
          />
          <Input
            type="password"
            name="password"
            onChange={onChangeHandler}
            placeholder="비밀번호"
          />
        </InputWrapper>
        <IconWrapper>
          로그인 <HiArrowRightCircle onClick={onLogin} />
        </IconWrapper>
        <Text
          onClick={() => {
            window.open('http://shabit.site/');
          }}
        >
          홈페이지로 이동하기
        </Text>
      </Popup>
    );
  } else {
    return <Tracking user={user} />;
  }
}
