package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.notification.Notification;
import com.ch.bojbm.global.auth.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Users extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy= "users",cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "users",cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void setPassword(String password){
        this.password = password;
    }

    
    // TODO : 정적 팩토리 메서드 패턴으로 유저를 생성하는 게 좀 더 이해하기 좋은 코드가 될 듯 (생성 메서드)
    @Builder
    public Users(String email, String password, Role role) {
        this.email=email;
        this.password=password;
        this.role = role;
    }


}
