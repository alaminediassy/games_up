package com.gamesUP.gamesUP.common;

import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminInitializerTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private AdminInitializer adminInitializer;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        adminInitializer = new AdminInitializer(userRepository, passwordEncoder);
    }

    @Test
    void shouldCreateAdminIfNotExists() throws Exception {
        when(userRepository.findByEmail("admin@gamesup.com")).thenReturn(Optional.empty());

        adminInitializer.run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("admin@gamesup.com");
        assertThat(savedUser.getNom()).isEqualTo("Administrateur GamesUP");
        assertThat(savedUser.getRole()).isEqualTo(Role.ADMIN);
        assertThat(passwordEncoder.matches("Admin123@", savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldNotCreateAdminIfExists() throws Exception {
        when(userRepository.findByEmail("admin@gamesup.com")).thenReturn(Optional.of(new User()));

        adminInitializer.run();

        verify(userRepository, never()).save(any());
    }
}
