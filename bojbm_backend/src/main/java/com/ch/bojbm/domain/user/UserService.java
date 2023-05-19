package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import com.ch.bojbm.global.auth.util.SecurityUtil;
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

    public UsersResponseDto getMyInfoBySecurity() {
        return usersRepository.findById(SecurityUtil.getCurrentUsersId())
                .map(UsersResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public UsersResponseDto changeMemberPassword(String email, String exPassword, String newPassword) {
        Users users = usersRepository.findById(SecurityUtil.getCurrentUsersId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, users.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        users.setPassword(passwordEncoder.encode((newPassword)));
        return UsersResponseDto.of(usersRepository.save(users));
    }
}
