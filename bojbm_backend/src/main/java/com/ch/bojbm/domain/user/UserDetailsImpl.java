package com.ch.bojbm.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final Users users;

    public UserDetailsImpl(Users users){
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(()->users.getAuthority().getKey());
        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { // 계정 만료 여부 ( false => 만료 )
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠김 여부 ( false => 잠김 )
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부 ( false => 만료 )
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정 활성화 여부 ( true => 활성화 )
        return true;
    }
}
