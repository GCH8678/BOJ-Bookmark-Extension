import React, { useState } from 'react';
import AddBookmarkIcon from '@mui/icons-material/BookmarkBorderSharp';
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

// DatePicker용 import
import Modal from '@mui/material/Modal';
import Paper from '@mui/material/Paper';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { DateCalendar } from '@mui/x-date-pickers/DateCalendar';
import dayjs from 'dayjs';
import CalendarIcon from '@mui/icons-material/EventAvailableSharp';

const AddBookmarkButton = ({ problemId, setIsBookmarked }) => {
  const [afterday, setAfterDay] = useState(7);

  const handleChange = (event) => {
    setAfterDay(Number(event.target.value));
  };

  const onAddBookmarkHandler = () => {
    chrome.runtime.sendMessage(
      {
        action: 'addBookmark',
        data: { problemId, afterday },
      },
      (res) => {
        //console.log(res);
        if (res === true) {
          //console.log('북마크 추가 성공');
          setIsBookmarked(true);
        } else {
          //console.log('북마크 추가 실패');
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

  const AfterDayDatePicker = () => {
    const [useDatePicker, setUseDatePicker] = useState(false);
    const [selectDay, setSelectDay] = useState(dayjs());

    const handleDatePickerOpen = () => {
      setUseDatePicker(true);
    };
    const handleDatePickerClose = () => {
      setUseDatePicker(false);
    };

    const now = dayjs();
    const today = dayjs(now.format('YYYY-MM-DD'));

    const setCalcuateAfterDay = () => {
      // console.log(selectDay);
      setAfterDay(selectDay.diff(today, 'd'));
      handleDatePickerClose();
    };

    return (
      <>
        <IconButton
          aria-label="datePicker"
          onClick={handleDatePickerOpen}
          size="large"
        >
          <CalendarIcon
            fontSize="large"
            sx={{ width: '3rem', height: '3rem' }}
          />
        </IconButton>
        <Box sx={{ fontSize: 20 }}>
          <Modal
            sx={{
              position: 'fixed',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
            open={useDatePicker}
            onClose={handleDatePickerClose}
            aria-labelledby="child-modal-title"
            aria-describedby="child-modal-description"
          >
            <Paper sx={{ minWidth: 100, minHeight: 50 }}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DemoContainer components={['DateCalendar']}>
                  <DateCalendar
                    defaultValue={today}
                    onChange={(selectDay) => setSelectDay(selectDay)}
                    disablePast
                    styled={{ fontSize: 15 }}
                  />
                </DemoContainer>
              </LocalizationProvider>
              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  paddingBottom: '10px',
                  justifyContent: 'center',
                }}
              >
                <Button
                  onClick={setCalcuateAfterDay}
                  variant="outlined"
                  sx={{ fontSize: 13 }}
                >
                  확인
                </Button>
              </Box>
            </Paper>
          </Modal>
        </Box>
      </>
    );
  };

  const AfterDaySelect = () => {
    return (
      <>
        <FormControl sx={{ m: 1, minWidth: 150, fontSize: 20 }}>
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
            renderValue={(value) => {
              switch (value) {
                case 0:
                  return '오늘';
                case 1:
                  return '내일';
                case 7:
                  return '1주 뒤';
                case 14:
                  return '2주 뒤';
                case 28:
                  return '4주 뒤';
                default:
                  return <em>{afterday}일 뒤</em>;
              }
            }}
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
            <MenuItem value={28} sx={{ fontSize: 13 }}>
              4주 뒤
            </MenuItem>
          </Select>
        </FormControl>
      </>
    );
  };

  return (
    <>
      <IconButton
        aria-label="addBookmark"
        onClick={handleClickOpen}
        size="large"
      >
        <AddBookmarkIcon
          fontSize="inherit"
          sx={{ width: '3rem', height: '3rem' }}
        />
      </IconButton>
      <Dialog open={modalOpen} onClose={handleClose}>
        <DialogTitle sx={{ fontSize: 25 }}> 북마크 추가 </DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ fontSize: 15 }}>
            며칠 뒤 알람을 받을지 선택하세요.
          </DialogContentText>
          <Box
            component="form"
            sx={{ display: 'flex', flexWrap: 'wrap', fontSize: 20 }}
          >
            <AfterDaySelect />
            <AfterDayDatePicker />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={onAddBookmarkHandler}
            sx={{ fontSize: 15 }}
            variant="outlined"
          >
            저장
          </Button>
          <Button
            onClick={handleClose}
            sx={{ fontSize: 15 }}
            variant="outlined"
            color="info"
          >
            취소
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};
export default AddBookmarkButton;
