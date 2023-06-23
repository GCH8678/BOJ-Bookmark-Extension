import React, { useState } from 'react';
import './FindPassword.css';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import PasswordOutlinedIcon from '@mui/icons-material/PasswordOutlined';
import KeyIcon from '@mui/icons-material/Key';
import MailOutlinedIcon from '@mui/icons-material/MailOutlined';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import getApiUrl from '../../getApiUrl';

const defaultTheme = createTheme();
const ApiUrl = getApiUrl();

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {'Copyright © '}
      <Link
        color="inherit"
        href="https://github.com/GCH8678/boj-bookmark-extension"
        target="_blank"
      >
        GCH8678
      </Link>{' '}
      {'.'}
    </Typography>
  );
}

const FindPassword = () => {
  const [form, setForm] = useState({
    email: '',
    password: '',
    passwordConfirm: '',
  });

  const [authCode, setAuthCode] = useState('');
  const [authChecked, setAuthChecked] = useState(false);
  const [sendEmail, setSendEmail] = useState(false);

  const [isValid, setValid] = useState({
    email: true,
    password: true,
    passwordPresent: true,
    authCode: true,
  });

  const exp = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9]+/;

  const sendAuthCode = () => {
    if (validateEmail(form.email)) {
      setSendEmail(true);
      const settings = {
        method: 'POST',
        body: JSON.stringify({
          email: form.email,
        }),
        headers: { 'Content-Type': 'application/json' },
      };
      fetch(ApiUrl + '/api/auth/password/email', settings)
        .then((res) => {
          if (res.ok) {
            setSendEmail(true);
            alert(' 인증코드를 보냈습니다.\n 이메일을 확인해 주세요. ');
          }
          if (res.status == 404) {
            alert('존재하지 않는 계정입니다.');
            setSendEmail(false);
          }
        })
        .catch((error) => {
          setSendEmail(false);
          console.log(error);
        });
    }
  };

  const checkAuthCode = () => {
    if (sendEmail) {
      const params = {
        authCode: authCode,
        email: form.email,
      };
      const query = Object.keys(params)
        .map((k) => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
        .join('&');
      const settings = {
        method: 'GET',
      };
      fetch(ApiUrl + '/api/auth/password/code?' + query, settings)
        .then((res) => {
          if (res.ok) {
            res.json().then((res) => {
              setAuthChecked(res);
              setValid((isValid) => ({ ...isValid, authCode: res }));
              alert('인증이 완료되었습니다.');
            });
          } else if (res.status == 400) {
            alert(' 잘못된 인증 코드입니다. \n 다시 입력해주세요.');
          } else {
            alert(' 인증코드 확인에 실패하였습니다.\n 다시 시도해주세요. ');
            throw new Error('Invalid sendAuthCodeMethod attempt');
          }
        })
        .catch((error) => {
          console.log(error);
          alert('서버와 연결이 원활하지 않습니다.');
        });
    } else {
      alert('이메일을 입력후 인증코드를 발송해 주세요.');
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    // console.log({
    //   email: form.email,
    //   password: form.password,
    //   passwordConfirm: form.passwordConfirm,
    // });
    const ve = validateEmail(form.email);
    const vp = validatePassword(form.password, form.passwordConfirm);

    if (ve && vp && validateAuthCheck()) {
      const settings = {
        method: 'POST',
        body: JSON.stringify({
          authCode: authCode,
          email: form.email,
          password: form.password,
        }),
        headers: { 'Content-Type': 'application/json' },
      };
      fetch(ApiUrl + '/api/auth/password/change', settings)
        .then((res) => {
          if (res.ok) {
            //console.log(res);
            res.json().then((data) => {
              //console.log(data);
              alert(data.message);
              window.close();
            });
          } else if (res.status == 401) {
            alert('인증을 제대로 진행해주세요.');
          } else {
            alert(' 비밀번호 찾기에 실패하였습니다. ');
            throw new Error('Invalid SignUp attempt');
          }
        })
        .catch((error) => {
          console.log(error);
          alert('서버와 연결이 원활하지 않습니다.');
        });
    }
  };

  const validatePassword = (pw1, pw2) => {
    if (pw1 == '') {
      setValid((isValid) => ({ ...isValid, passwordPresent: false }));
    } else {
      setValid((isValid) => ({ ...isValid, passwordPresent: true }));
    }
    if (pw1 && pw2) {
      if (pw1 == pw2) {
        setValid((isValid) => ({ ...isValid, password: true }));
        return true;
      }
    }
    setValid((isValid) => ({ ...isValid, password: false }));
    return false;
  };

  const validateEmail = (email) => {
    if (exp.test(email)) {
      setValid((isValid) => ({ ...isValid, email: true }));
      return true;
    }
    setValid((isValid) => ({ ...isValid, email: false }));
    return false;
  };

  const validateAuthCheck = () => {
    if (!authChecked) {
      alert('인증을 완료해주세요');
      return false;
    }
    return true;
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary.light' }}>
            <PasswordOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            비밀번호 찾기
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  autoFocus
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  helperText={!isValid.email ? '잘못된 이메일 형식입니다.' : ''}
                  value={form.email}
                  error={!isValid.email}
                  disabled={sendEmail}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                  autoComplete="email"
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <MailOutlinedIcon />
                      </InputAdornment>
                    ),
                    endAdornment: (
                      <InputAdornment position="end">
                        <Button onClick={sendAuthCode}> 인증코드 발송 </Button>
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>

              <Grid item xs={12}>
                <FormControl fullWidth variant="outlined">
                  <InputLabel htmlFor="authCode">인증번호</InputLabel>
                  <OutlinedInput
                    id="authCode"
                    disabled={authChecked}
                    value={authCode}
                    error={!isValid.authCode}
                    onChange={(e) => setAuthCode(e.target.value)}
                    endAdornment={
                      <InputAdornment position="end">
                        <Button onClick={checkAuthCode}> 인증코드 확인 </Button>
                      </InputAdornment>
                    }
                    label="authCode"
                  />
                </FormControl>
              </Grid>

              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="new Password"
                  type="password"
                  id="password"
                  helperText={
                    !isValid.passwordPresent ? '비밀번호를 입력해 주세요.' : ''
                  }
                  value={form.password}
                  error={!isValid.passwordPresent}
                  onChange={(e) =>
                    setForm({ ...form, password: e.target.value })
                  }
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <KeyIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="passwordConfirm"
                  label="new Password Check"
                  type="password"
                  id="passwordConfirm"
                  helperText={
                    !isValid.password ? '비밀번호가 일치하지 않습니다.' : ''
                  }
                  error={!isValid.password}
                  value={form.passwordConfirm}
                  onChange={(e) =>
                    setForm({ ...form, passwordConfirm: e.target.value })
                  }
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <KeyIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              비밀번호 변경
            </Button>
          </Box>
        </Box>
        <Copyright sx={{ mt: 5 }} />
      </Container>
    </ThemeProvider>
  );
};

export default FindPassword;
