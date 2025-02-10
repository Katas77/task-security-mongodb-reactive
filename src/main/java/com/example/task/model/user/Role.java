package com.example.task.model.user;


import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Document(collection = "authorities")
public class Role {


    @Id
    private Long id;

    private RoleType authority;

    @Field(" user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;


    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(authority.name());

    }


    public static Role from(RoleType type) {
        var role = new Role();
        role.setAuthority(type);
        return role;
    }


}
