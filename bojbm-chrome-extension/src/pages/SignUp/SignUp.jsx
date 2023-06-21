import React, { useState } from 'react';
import './SignUp.css';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import PersonAddOutlinedIcon from '@mui/icons-material/PersonAddOutlined';
import KeyIcon from '@mui/icons-material/Key';
import MailOutlinedIcon from '@mui/icons-material/MailOutlined';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';

const defaultTheme = createTheme();
const ApiUrl =
  'http://ec2-3-39-95-47.ap-northeast-2.compute.amazonaws.com:8080';

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

const Signup = () => {
  const [form, setForm] = useState({
    email: '',
    password: '',
    passwordConfirm: '',
    allowSendEmail: false,
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
      //console.log('sendAuthCode method');
      const settings = {
        method: 'POST',
        body: JSON.stringify({
          email: form.email,
        }),
        headers: { 'Content-Type': 'application/json' },
      };
      fetch(ApiUrl + '/api/auth/signup/email', settings)
        .then((res) => {
          if (res.status == 409) {
            alert('이미 존재하는 계정입니다.');
            setSendEmail(false);
          } else if (res.ok) {
            //console.log('res in sendAuthCode fetch');
            setSendEmail(true);
            alert(' 인증코드를 보냈습니다.\n 이메일을 확인해 주세요. ');
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
      fetch(ApiUrl + '/api/auth/signup/code?' + query, settings)
        .then((res) => {
          if (res.ok) {
            res.json().then((res) => {
              setAuthChecked(res);
              setValid((isValid) => ({ ...isValid, authCode: res }));
              alert('인증이 완료되었습니다.');
            });
          } else if (res.status == 400) {
            setAuthChecked(false);
            alert(' 잘못된 인증 코드입니다. \n 다시 입력해주세요');
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

  const handleAllowSendEmail = (event) => {
    setForm({
      ...form,
      allowSendEmail: event.target.checked,
    });
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

    if (ve && vp && validateCheckBoxAndauthCheck()) {
      console.log('signUp method');
      const settings = {
        method: 'POST',
        body: JSON.stringify({
          email: form.email,
          password: form.password,
        }),
        headers: { 'Content-Type': 'application/json' },
      };
      fetch(ApiUrl + '/api/auth/signup', settings)
        .then((res) => {
          if (res.ok) {
            console.log('res in signUp fetch');
            console.log(res);
            res.json().then((data) => {
              console.log(data);
              alert(data.message);
            });
          } else {
            alert(' 회원가입에 실패하였습니다. ');
            throw new Error('Invalid SignUp attempt');
          }
        })
        .catch((error) => {
          console.log(error < -'<- error in getAuth');
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

  const validateCheckBoxAndauthCheck = () => {
    if (!authChecked) {
      alert('인증을 완료해주세요');
      return false;
    }
    if (!form.allowSendEmail) {
      alert('이메일 수신에 동의해주세요. ');
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
            <PersonAddOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            회원가입
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
                  disabled={sendEmail}
                  helperText={!isValid.email ? '잘못된 이메일 형식입니다.' : ''}
                  value={form.email}
                  error={!isValid.email}
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
                  label="Password"
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
                  label="Password Check"
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
              <Grid item xs={12}>
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={form.allowSendEmail}
                      onChange={handleAllowSendEmail}
                      color="primary"
                    />
                  }
                  label="알람 기능을 위한 메일 수신에 동의합니다.(필수)"
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              회원가입
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link
                  variant="body2"
                  href={
                    'chrome-extension://' +
                    chrome.runtime.id +
                    '/findpassword.html'
                  }
                >
                  비밀번호를 잊으셨나요?(비밀번호 찾기)
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 5 }} />
      </Container>
    </ThemeProvider>
  );
};

export default Signup;
