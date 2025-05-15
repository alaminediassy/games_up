package com.gamesUP.gamesUP.unit.service;

import com.gamesUP.gamesUP.dto.request.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.response.UserResponseDTO;
import com.gamesUP.gamesUP.exception.EmailAlreadyUsedException;
import com.gamesUP.gamesUP.mapper.UserMapper;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterNewUser() {
        // Given
        UserRegisterDTO dto = new UserRegisterDTO("Alice", "alice@example.com", "password123");

        User user = new User();
        user.setNom(dto.nom());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(Role.CLIENT);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setNom(dto.nom());
        savedUser.setEmail(dto.email());
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.CLIENT);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserResponseDTO response = userService.register(dto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.nom()).isEqualTo(dto.nom());
        assertThat(response.email()).isEqualTo(dto.email());
        assertThat(response.role()).isEqualTo("CLIENT");

        verify(userRepository).findByEmail(dto.email());
        verify(userMapper).toEntity(dto);
        verify(passwordEncoder).encode(dto.password());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyUsed() {
        // Given
        UserRegisterDTO dto = new UserRegisterDTO("Bob", "bob@example.com", "securePass");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        // Then
        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(EmailAlreadyUsedException.class);

        verify(userRepository).findByEmail(dto.email());
        verifyNoMoreInteractions(userMapper, passwordEncoder, userRepository);
    }
}
