package com.gamesUP.gamesUP.unit.mapper;

import com.gamesUP.gamesUP.dto.request.UserRegisterDTO;
import com.gamesUP.gamesUP.mapper.UserMapper;
import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void shouldConvertUserRegisterDTOToEntity() {
        UserRegisterDTO registerDTO = new UserRegisterDTO(
                "John Doe",
                "john.doe@example.com",
                "password123"
        );

        User user = userMapper.toEntity(registerDTO);

        assertThat(user).isNotNull();
        assertThat(user.getNom()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getRole()).isEqualTo(Role.CLIENT);
    }
}
