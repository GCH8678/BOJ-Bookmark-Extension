package com.ch.bojbm.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UsersController {
    private final UserService userService;


    @GetMapping("/memberLoginForm")
    public String login(){
        return "test";
    }

}
