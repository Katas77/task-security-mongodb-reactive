package com.example.task.mapper;

import com.example.task.model.user.User;
import com.example.task.dto.UserModel;

import java.util.Collections;

import java.util.Set;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;

public interface UserMapper {

    User modelToUser(UserModel model);

    UserModel userToModel(User user);

    default Flux<UserModel> fluxUserModels(Flux<User> userFlux) {
        return userFlux.map(this::userToModel);
    }

    default Flux<User> fluxUsers(Flux<UserModel> userModelFlux) {
        return userModelFlux.map(this::modelToUser);
    }

    default Set<UserModel> setUserModels(Set<User> userSet) {
        if (userSet == null) {
            return Collections.emptySet();
        }
        return userSet.stream().map(this::userToModel).collect(Collectors.toSet());
    }

    default Set<User> setUsers(Set<UserModel> userModelSet) {
        if (userModelSet == null) {
            return Collections.emptySet();
        }
        return userModelSet.stream().map(this::modelToUser).collect(Collectors.toSet());
    }
}

