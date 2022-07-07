package com.cylin.springbootmall.service.impl;

import com.cylin.springbootmall.dao.UserDao;
import com.cylin.springbootmall.dao.impl.UserDaoImpl;
import com.cylin.springbootmall.dto.UserRegisterRequest;
import com.cylin.springbootmall.model.User;
import com.cylin.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public int register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
