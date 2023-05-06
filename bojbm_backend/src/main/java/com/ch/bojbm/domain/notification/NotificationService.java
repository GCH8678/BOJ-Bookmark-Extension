package com.ch.bojbm.domain.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class NotificationService {

//    @Scheduled(cron="0 0 09 * * ?") // 매일 9시 마다 알림 보내기
    public LocalDate sendNotification(){

        LocalDate date =  LocalDate.now();
        log.info(date.toString());
        log.info(date.plusDays(10).toString());
        return date;
    }
}
