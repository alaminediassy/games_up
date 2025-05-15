package com.gamesUP.gamesUP.unit.model;

import com.gamesUP.gamesUP.model.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void shouldContainAdminAndClientRoles() {
        assertThat(Role.values()).containsExactly(Role.ADMIN, Role.CLIENT);
    }

    @Test
    void shouldGetRoleByName() {
        assertThat(Role.valueOf("ADMIN")).isEqualTo(Role.ADMIN);
        assertThat(Role.valueOf("CLIENT")).isEqualTo(Role.CLIENT);
    }
}
