package com.rest.oauth2.controller;

import com.rest.oauth2.entity.User;
import com.rest.oauth2.repo.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;

    @GetMapping(value = "/users")
    public List<User> findAllUser() {

        System.out.println("???");
        return userJpaRepo.findAll();
    }

    @PostMapping(value = "/user")
    public List<User> findAllUser1() {

        System.out.println("?");
        return userJpaRepo.findAll();
    }

    @GetMapping(value="/test")
    public void Test() {
        System.out.println("sample");
    }
}
