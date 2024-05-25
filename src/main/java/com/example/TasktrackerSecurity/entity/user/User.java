package com.example.TasktrackerSecurity.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Users")
@Builder
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;

    @Field("roles")
    private Set<Role> roles = new HashSet<>();
}
