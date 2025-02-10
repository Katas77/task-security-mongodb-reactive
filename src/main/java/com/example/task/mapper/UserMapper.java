package com.example.task.mapper;



import com.example.task.model.user.User;
import com.example.task.dto.UserModel;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface UserMapper {

    User modelToUser(UserModel model);

    UserModel userToModel(User user);


    default Set<UserModel> setUserModels(Set<User> userSet) {
        Set<UserModel> userModelSet = new HashSet<>();
        if (!(userSet == null)) {
            userModelSet = userSet.stream().map(this::userToModel).collect(Collectors.toSet());
            return userModelSet;
        } else return null;
    }

    default Set<User> setUsers(Set<UserModel> userModelSet) {
        Set<User> userSet = new HashSet<>();
        if (!(userModelSet == null)) {
            userSet = userModelSet.stream().map(this::modelToUser).collect(Collectors.toSet());
            return userSet;
        } else return null;
    }


}
