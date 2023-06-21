import React, { useState, useEffect } from 'react';

import List from '@mui/material/List';
import ListItemText from '@mui/material/ListItemText';
import ListItemButton from '@mui/material/ListItemButton';
import Box from '@mui/material/Box';
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

  const problemList = todayProblemList.map((problem) => (
    <ListItemButton
      divider="true"
      href={`https://www.acmicpc.net/problem/${problem}`}
      target="_blank"
    >
      <ListItemText primary={problem} />
    </ListItemButton>
  ));

  return (
    <div className="problemList">
      <Box>
        <List dense="false">{problemList}</List>
      </Box>
    </div>
  );
};

export default TodayProblems;
