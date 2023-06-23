import React, { useState, useEffect } from 'react';

import Box from '@mui/material/Box';
//import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
//import Stack from '@mui/material/Stack';
const TodayProblems = () => {
  const [todayProblemList, setTodayProblemList] = useState([]);
  //const bojUrl = 'https://www.acmicpc.net/problem/';
  useEffect(() => {
    chrome.runtime.sendMessage(
      {
        action: 'getTodayProblemList',
      },
      (res) => {
        setTodayProblemList(res);
      }
    );
  }, []);

  const Item = styled(Button)(() => ({
    backgroundColor: '#fff',
  }));

  const problemList = todayProblemList.map((problem) => (
    <Box px="0px" sx={{ flexGrow: 1, overflow: 'hidden' }}>
      <Item
        variant="outlined"
        href={`https://www.acmicpc.net/problem/${problem}`}
        target="_blank"
        fullWidth={true}
        sx={{
          my: '3px',
          mx: 'auto',
          py: 1,
        }}
      >
        <Stack spacing={2} direction="column" alignItems="flex-start" fullWidth>
          <Typography align="left">{problem}</Typography>
        </Stack>
      </Item>
    </Box>
  ));

  const emptyAnnounce = (
    <Item
      href="https://www.acmicpc.net/problemset"
      target="_blank"
      backgroundColor="#eae9e9"
    >
      <Typography align="center" fontSize="15px" color="">
        오늘 풀어야 할 문제가 존재하지 않습니다.
        <br />
        (클릭시 백준 사이트로 이동합니다.)
      </Typography>
    </Item>
  );

  return (
    <>
      {todayProblemList.length > 0 ? (
        <Box sx={{ px: '0px', minHeight: '350px' }}>{problemList}</Box>
      ) : (
        <Box sx={{ minHeight: '350px' }}>{emptyAnnounce}</Box>
      )}
    </>
  );
};

export default TodayProblems;
