package com.cylin.springbootmall.service;

import com.cylin.springbootmall.dto.UserLoginRequest;
import com.cylin.springbootmall.dto.UserRegisterRequest;
import com.cylin.springbootmall.model.User;

public interface UserService {

    User getUserById(int id);

    int register(UserRegisterRequest userRegisterRequest);

    User login(UserLoginRequest userLoginRequest);
}
