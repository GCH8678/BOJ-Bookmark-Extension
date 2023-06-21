package com.ch.bojbm.domain.user;

import com.ch.bojbm.domain.user.dto.ChangePasswordRequestDto;
import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UsersController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UsersResponseDto> getMyMemberInfo() {
        UsersResponseDto myInfoBySecurity = userService.getMyInfoBySecurity();
        return ResponseEntity.ok((myInfoBySecurity));
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }



}
