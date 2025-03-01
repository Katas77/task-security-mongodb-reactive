package com.example.task.mapper.impl;

import com.example.task.model.user.User;
import com.example.task.mapper.UserMapper;
import com.example.task.dto.UserModel;
import org.springframework.stereotype.Component;

import lombok.Builder;

@Component
public class UserMapAllField implements UserMapper {

    @Override
    public User modelToUser(UserModel model) {
        if (model == null) {
            return null;
        }
        return User.builder()
                .email(model.getEmail())
                .username(model.getUsername())
                .password(model.getPassword())
                .build();
    }

    @Override
    public UserModel userToModel(User user) {
        if (user == null) {
            return null;
        }
        return UserModel.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}