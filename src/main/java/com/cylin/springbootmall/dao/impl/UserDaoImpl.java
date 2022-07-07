package com.cylin.springbootmall.dao.impl;

import com.cylin.springbootmall.dao.UserDao;
import com.cylin.springbootmall.dto.UserRegisterRequest;
import com.cylin.springbootmall.model.User;
import com.cylin.springbootmall.rowMapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(int id) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM user " +
                "WHERE user_id = :id";

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        List<User> list = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(list.size() == 0){
            return null;
        } else{
            return list.get(0);
        }
    }

    @Override
    public int createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user (email, password, created_date, last_modified_date) " +
                "VALUES (:email, :password, :created_date, :last_modified_date)";

        Map<String, Object> map = new HashMap<>();

        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date cur = new Date();

        map.put("created_date", cur);
        map.put("last_modified_date", cur);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();
    }
}
