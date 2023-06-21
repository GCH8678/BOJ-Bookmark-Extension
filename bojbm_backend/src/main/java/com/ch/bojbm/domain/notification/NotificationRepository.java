package com.ch.bojbm.domain.notification;

import com.ch.bojbm.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Notification findNotificationByUsersAndNotificationDate(Users users, LocalDate notificationDate);

    List<Notification> findAllByNotificationDate(LocalDate notificationDate);

}
