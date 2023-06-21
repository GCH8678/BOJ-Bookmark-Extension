package com.ch.bojbm.domain.notification;

import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.mail.EmailMessageDto;
import com.ch.bojbm.domain.mail.EmailService;
import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.domain.user.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;

    // TODO: 이후 인터페이스로 바꿔서 사용자의 설정값 따라 -> 메일 or 푸시알림 선택할 수 있게 하려고 함
    private final EmailService emailService;


    @Transactional(readOnly = true)
    public void checkAndDeleteNotification(Notification notification){
        Set<Bookmark> bookmarksAtNotificationDate = notification.getBookmarks();
        if(bookmarksAtNotificationDate.size()==1){
            notificationRepository.deleteById(notification.getId());
        }
    }

    @Transactional(readOnly = true)
    public Notification getTodayNotification(User user){
        LocalDate today = LocalDate.now();
        Users currentUsers = getUsers(user.getUsername());
        return notificationRepository.findNotificationByUsersAndNotificationDate(currentUsers,today);
    }

    @Transactional
    public Notification addBookmarkInNotification(Users user, Bookmark bookmark, LocalDate notificationDate ){
        Notification savedNotification = notificationRepository.findNotificationByUsersAndNotificationDate(user,notificationDate);

        if(savedNotification == null){
            Notification newNotification = new Notification(user,notificationDate);
            newNotification.addBookmark(bookmark);
            return notificationRepository.save(newNotification);
        }else {
            savedNotification.addBookmark(bookmark);
            return notificationRepository.save(savedNotification);
        }
    }


    //TODO : 해당 notification이 갱신될때 todayList도 갱신하게 할지 고민해 봐야 함
    @Scheduled(cron="0 50 01 * * ?")  // 매일 9시 마다 알림 초 분 시간 일 월 요일
    public void sendNotificationWithMail() throws MessagingException { // Mail 알림
        LocalDate today = LocalDate.now();
        List<Notification> notifications = getTodayNotifications(today);

        for(Notification notification : notifications){
            HashMap<String, Set<Bookmark>> emailValues = new HashMap<>();
            Set<Bookmark> bookmarks = notification.getBookmarks();
            emailValues.put("bookmarks", bookmarks);
            EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                    .templateName("mail")
                    .subject(notification.getNotificationDate().toString() + " 일자 북마크 알림")
                    .to(notification.getUsers().getEmail())
                    .build();
            emailService.sendMailWithBookmarks(emailMessageDto,emailValues);
        }

    }

    private List<Notification> getTodayNotifications(LocalDate today){
        List<Notification> notifications =  notificationRepository.findAllByNotificationDate(today);
        if(notifications.isEmpty()){
            throw new RuntimeException("오늘 등록된 알람이 존재하지 않습니다.");
        }else{
            return notifications;
        }
    }

    private Users getUsers(String email) {
        Optional<Users> member = usersRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get();
        } else throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
    }

    @Transactional
    public void deleteNotification(Notification notification) {

        notificationRepository.delete(notification);
    }
}
