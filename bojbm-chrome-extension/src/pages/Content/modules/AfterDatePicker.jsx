import React, { useState } from 'react';

import Modal from '@mui/material/Modal';
import Paper from '@mui/material/Paper';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { DateCalendar } from '@mui/x-date-pickers/DateCalendar';
import dayjs from 'dayjs';
import CalendarIcon from '@mui/icons-material/EventAvailableSharp';
import IconButton from '@mui/material/IconButton';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

const AfterDayDatePicker = ({ setAfterDay }) => {
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
        <CalendarIcon fontSize="large" />
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
                  sx={{
                    '& .MuiDateCalendar-viewTransitionContainer': {
                      fontSize: '1.5rem',
                    },
                    '& .MuiPickersDay-root': { fontSize: '1.5rem' },
                  }}
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

export default AfterDayDatePicker;
