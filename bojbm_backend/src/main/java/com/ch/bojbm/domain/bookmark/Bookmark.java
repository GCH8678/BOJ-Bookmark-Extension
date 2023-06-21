package com.ch.bojbm.domain.bookmark;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.notification.Notification;
import com.ch.bojbm.domain.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="BOOKMARK")
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    private Integer problemNum;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    //private String memo;
    //private LocalDate notificationDate;
    //private Boolean isChecked;  // TODO : 이후 웹앱등에서 푼 문제인지 확인할 수 있도록

    //OneToMany
    //private List<Notification> notifications

    @Builder
    public Bookmark(Long id, Users users, Integer problemNum) {
        this.id = id;
        this.users = users;
        this.problemNum = problemNum;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    //    public void setNotificationDate(LocalDate notificationDate){
//        this.notificationDate=notificationDate;
//    }


}
