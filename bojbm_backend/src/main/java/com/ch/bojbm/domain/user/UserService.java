package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.user.dto.ChangeMemberPasswordResponseDto;
import com.ch.bojbm.domain.user.dto.ChangePasswordRequestDto;
import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import com.ch.bojbm.global.auth.util.SecurityUtil;
import com.ch.bojbm.global.redis.RedisService;
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
    private final RedisService redisService;

    public UsersResponseDto getMyInfoBySecurity() {
        return usersRepository.findById(SecurityUtil.getCurrentUsersId())
                .map(UsersResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public ChangeMemberPasswordResponseDto changeMemberPassword(ChangePasswordRequestDto dto) {
        Users users = usersRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다")
                //TODO : 이후 HTTP-Status-Code 제어를 위해 Exception Handing 등으로 교체
        );
//        if (!passwordEncoder.matches(exPassword, users.getPassword())) {
//            throw new RuntimeException("비밀번호가 맞지 않습니다");
//        }
        if(!redisService.getData(dto.getAuthCode()).equalsIgnoreCase(dto.getEmail())){
            throw new IllegalArgumentException("잘못된 요청 입니다.");
        }
        users.setPassword(passwordEncoder.encode((dto.getPassword())));
        usersRepository.save(users);
        return ChangeMemberPasswordResponseDto.builder().message("비밀번호가 변경되었습니다.").build();
    }
}
