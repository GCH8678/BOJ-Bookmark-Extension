package com.ch.bojbm.domain.notification;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.user.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //TODO: 이후 쿼리 최적화 해줘야 함.( Notification 엔티티 - Users)
    @ManyToOne
    private Users user;

    private LocalDate notificationDate;

    //TODO : 북마크 묶음?을 날짜랑 유저를 기준으로 설정할 방법 찾아야 함.
    @OneToMany
    private List<Bookmark> bookmark;

}
