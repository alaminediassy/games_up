package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO dto);
}
