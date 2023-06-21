package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.notification.Notification;
import com.ch.bojbm.global.auth.entity.Authority;
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
    private Authority authority;

    @OneToMany(mappedBy= "users",cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "users",cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void setPassword(String password){
        this.password = password;
    }

    @Builder
    public Users(String email, String password, Authority authority) {
        this.email=email;
        this.password=password;
        this.authority=authority;
    }


}
