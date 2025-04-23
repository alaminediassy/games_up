package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO dto);
}
