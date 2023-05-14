package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.global.auth.entity.Authority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Users extends BaseEntity{   //TODO : User Entity가 스프링 시큐리티에서 사용자 정보를 주는 역할까지 해서 책임이 너무 많음 => 이후 리팩토링 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user")
    private Set<Bookmark> bookmarks;

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
