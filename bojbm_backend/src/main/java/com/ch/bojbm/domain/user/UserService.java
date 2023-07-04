package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.user.dto.ChangeMemberPasswordResponseDto;
import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import com.ch.bojbm.global.auth.util.SecurityUtil;
import com.ch.bojbm.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    public UsersResponseDto getMyInfoBySecurity() {
        return usersRepository.findById(SecurityUtil.getCurrentUsersId())
                .map(UsersResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public ChangeMemberPasswordResponseDto changeMemberPassword(String authCode, String email, String newPassword) {
        Users users = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다")
                //TODO : 이후 HTTP-Status-Code 제어를 위해 Exception Handing 등으로 교체
        );
//        if (!passwordEncoder.matches(exPassword, users.getPassword())) {
//            throw new RuntimeException("비밀번호가 맞지 않습니다");
//        }
        if(!redisUtil.getData(authCode).equalsIgnoreCase(email)){
            throw new IllegalArgumentException("잘못된 요청 입니다.");
        }
        users.setPassword(passwordEncoder.encode((newPassword)));
        usersRepository.save(users);
        return ChangeMemberPasswordResponseDto.builder().message("비밀번호가 변경되었습니다.").build();
    }
}
