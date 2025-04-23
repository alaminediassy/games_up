package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.UserResponseDTO;
import com.gamesUP.gamesUP.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/private/hello")
    public String hello() {
        return "Bienvenue sur votre espace personnel privé !";
    }

    @PostMapping("/public/users/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO registerDTO) {
        try {
            UserResponseDTO response = userService.register(registerDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
