package com.example.TasktrackerSecurity.repository;


import com.example.TasktrackerSecurity.entity.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

   Mono<User> findByUsername(String name);



}
