package com.example.oauth.service;

import com.example.oauth.bean.User;
import com.example.oauth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public boolean signUp(User user) {
        String username = user.getUsername();
        User uzer = userMapper.findByName(username);
        if (uzer == null) {
            return userMapper.insertUser(user);
        }
        return false;
    }

    public boolean login(User user) {
        return userMapper.checkUser(user) != null;
    }
}
