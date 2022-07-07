package com.cylin.springbootmall.controller;

import com.cylin.springbootmall.dto.UserRegisterRequest;
import com.cylin.springbootmall.model.User;
import com.cylin.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        int id = userService.register(userRegisterRequest);

        User user = userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
