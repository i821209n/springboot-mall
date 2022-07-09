package com.cylin.springbootmall.service.impl;

import com.cylin.springbootmall.dao.UserDao;
import com.cylin.springbootmall.dao.impl.UserDaoImpl;
import com.cylin.springbootmall.dto.UserRegisterRequest;
import com.cylin.springbootmall.model.User;
import com.cylin.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public int register(UserRegisterRequest userRegisterRequest) {
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if(user != null){
            log.warn("email : \"{}\" is exist", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return userDao.createUser(userRegisterRequest);
    }
}
