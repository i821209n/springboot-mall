package com.cylin.springbootmall.dao;

import com.cylin.springbootmall.dto.UserRegisterRequest;
import com.cylin.springbootmall.model.User;

public interface UserDao {

    User getUserById(int id);

    int createUser(UserRegisterRequest userRegisterRequest);

    User getUserByEmail(String email);
}
