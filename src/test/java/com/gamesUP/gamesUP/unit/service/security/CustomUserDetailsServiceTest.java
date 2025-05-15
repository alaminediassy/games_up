package com.gamesUP.gamesUP.unit.service.security;

import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.service.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setRole(Role.CLIENT); // ou ADMIN selon ton enum

        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));

        // Act
        var userDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_CLIENT");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String email = "unknown@example.com";
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with username: " + email);

        verify(userRepository).findByEmail(email);
    }
}
