package com.ch.bojbm.domain.notification;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.user.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate notificationDate;


    //TODO: 이후 쿼리 최적화 해줘야 함.( Notification 엔티티 - Users)
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    //TODO : 북마크 묶음?을 날짜랑 유저를 기준으로 설정할 방법 찾아야 함.
    @OneToMany(fetch = FetchType.LAZY, mappedBy="notification")
    private Set<Bookmark> bookmarks = new HashSet<>();

    public Notification(Users users, LocalDate notificationDate){
        this.users = users;
        this.notificationDate = notificationDate;
    }
    public void addBookmark (Bookmark bookmark){
        this.bookmarks.add(bookmark);
    }

}
