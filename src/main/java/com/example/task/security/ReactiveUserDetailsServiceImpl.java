package com.example.task.security;


import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")
//Аннотация используется для условного создания bean-компонента Spring в зависимости от конфигурации свойства. В использовании, которое вы показали в вопросе, компонент будет создан только в том случае, если db свойство существует и имеет значение, отличное от false. Э
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> userRepository.findByUsername(username).toFuture().get())
                .flatMap(Mono::just)
                .map(AppUserPrincipal::new);

    }
}
