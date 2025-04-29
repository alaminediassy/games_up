package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan("com.gamesUP.gamesUP.model")
@EnableJpaRepositories("com.gamesUP.gamesUP.repository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setRole(Role.valueOf("CLIENT"));

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("test@email.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@email.com");

        Optional<User> notFound = userRepository.findByEmail("unknown@email.com");
        assertThat(notFound).isNotPresent();
    }
}
