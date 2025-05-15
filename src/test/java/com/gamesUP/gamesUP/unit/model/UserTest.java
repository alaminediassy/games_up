package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        User user = new User(1L, "Mamadou", "mamadou@example.com", "securepassword", Role.CLIENT);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getNom()).isEqualTo("Mamadou");
        assertThat(user.getEmail()).isEqualTo("mamadou@example.com");
        assertThat(user.getPassword()).isEqualTo("securepassword");
        assertThat(user.getRole()).isEqualTo(Role.CLIENT);
    }

    @Test
    void shouldUseSettersCorrectly() {
        User user = new User();
        user.setId(2L);
        user.setNom("Diassy");
        user.setEmail("diassy@example.com");
        user.setPassword("12345");
        user.setRole(Role.ADMIN);

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getNom()).isEqualTo("Diassy");
        assertThat(user.getEmail()).isEqualTo("diassy@example.com");
        assertThat(user.getPassword()).isEqualTo("12345");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void shouldCheckEquality() {
        User user1 = new User(1L, "Ali", "ali@example.com", "pwd", Role.CLIENT);
        User user2 = new User(1L, "Ali", "ali@example.com", "pwd", Role.CLIENT);

        assertThat(user1).isEqualTo(user2);
    }
}
