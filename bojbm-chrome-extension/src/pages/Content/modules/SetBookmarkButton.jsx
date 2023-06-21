import React, { useState } from 'react';

import BookmarkIcon from '@mui/icons-material/Bookmark';
import IconButton from '@mui/material/IconButton';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Box from '@mui/material/Box';
import OutlinedInput from '@mui/material/OutlinedInput';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import InputLabel from '@mui/material/InputLabel';
import Button from '@mui/material/Button';

const SetBookmarkButton = ({ problemId, setIsBookmarked }) => {
  const [afterday, setAfterDay] = useState(7);

  const handleChange = (event) => {
    setAfterDay(Number(event.target.value));
  };

  const onUpdateBookmarkHandler = () => {
    chrome.runtime.sendMessage(
      {
        action: 'updateBookmark',
        data: { problemId, afterday },
      },
      (res) => {
        //console.log(res);
        if (res === true) {
          console.log('북마크 수정 성공');
        } else {
          console.log('북마크 수정 실패');
        }
        setModalOpen(false);
      }
    );
  };

  const ondeleteBookmarkHandler = () => {
    chrome.runtime.sendMessage(
      {
        action: 'deleteBookmark',
        data: { problemId },
      },
      (res) => {
        //console.log(res);
        if (res === true) {
          console.log('북마크 제거 성공');
          setIsBookmarked(false);
        } else {
          console.log('북마크 제거 실패');
        }
        setModalOpen(false);
      }
    );
  };

  const [modalOpen, setModalOpen] = useState(false);

  const handleClickOpen = () => {
    setModalOpen(true);
  };

  const handleClose = () => {
    setModalOpen(false);
  };

  return (
    <>
      <IconButton aria-label="Bookmark" onClick={handleClickOpen} size="large">
        <BookmarkIcon fontSize="inherit" />
      </IconButton>
      <Dialog open={modalOpen} onClose={handleClose}>
        <DialogTitle sx={{ fontSize: 25 }}> 북마크 수정 </DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ fontSize: 15 }}>
            몇일 뒤 다시 알림을 받을지 선택하세요.
          </DialogContentText>
          <Box
            component="form"
            sx={{ display: 'flex', flexWrap: 'wrap', fontSize: 20 }}
          >
            <FormControl sx={{ m: 1, minWidth: 120, fontSize: 20 }}>
              <InputLabel id="afterday-select-label" sx={{ fontSize: 15 }}>
                AfterDay
              </InputLabel>
              <Select
                defaultValue={7}
                labelId="afterday-select-label"
                id="afterday-select"
                value={afterday}
                onChange={handleChange}
                sx={{ fontSize: 12 }}
                input={<OutlinedInput label="AfterDay" />}
              >
                <MenuItem value={0} sx={{ fontSize: 13 }}>
                  오늘
                </MenuItem>
                <MenuItem value={1} sx={{ fontSize: 13 }}>
                  내일
                </MenuItem>
                <MenuItem value={7} sx={{ fontSize: 13 }}>
                  1주 뒤
                </MenuItem>
                <MenuItem value={14} sx={{ fontSize: 13 }}>
                  2주 뒤
                </MenuItem>
                <MenuItem value={24} sx={{ fontSize: 13 }}>
                  4주 뒤
                </MenuItem>
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button
            variant="outlined"
            onClick={onUpdateBookmarkHandler}
            sx={{ fontSize: 15 }}
          >
            저장
          </Button>
          <Button
            color="error"
            variant="outlined"
            onClick={ondeleteBookmarkHandler}
            sx={{ fontSize: 15 }}
          >
            삭제
          </Button>
          <Button
            color="info"
            variant="outlined"
            onClick={handleClose}
            sx={{ fontSize: 15 }}
          >
            취소
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default SetBookmarkButton;
