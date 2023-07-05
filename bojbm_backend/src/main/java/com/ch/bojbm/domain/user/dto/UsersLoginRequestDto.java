package com.ch.bojbm.domain.user.dto;


import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.global.auth.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UsersLoginRequestDto {

    private String email;

    private String password;


    public Users toUsersEntity(PasswordEncoder passwordEncoder){
        return Users.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .authority(Authority.USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
