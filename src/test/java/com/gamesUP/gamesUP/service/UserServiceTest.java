package com.gamesUP.gamesUP.service;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO("Alioune", "alioune@example.com", "password123");

        User user = new User();
        user.setNom(dto.nom());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(Role.CLIENT); // Ajouter manuellement car UserRegisterDTO ne contient pas de role

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setNom(dto.nom());
        savedUser.setEmail(dto.email());
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.CLIENT);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponseDTO response = userService.register(dto);

        // Assert
        assertNotNull(response);
        assertEquals("Alioune", response.nom());
        assertEquals("alioune@example.com", response.email());
        assertEquals("CLIENT", response.role());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyUsed() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO("Alioune", "alioune@example.com", "password123");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EmailAlreadyUsedException.class, () -> userService.register(dto));

        verify(userRepository, never()).save(any());
    }
}
