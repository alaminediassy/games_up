package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.request.LoginRequest;
import com.gamesUP.gamesUP.dto.response.LoginResponse;
import com.gamesUP.gamesUP.exception.BadCredentialsException;
import com.gamesUP.gamesUP.security.JwtService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/public/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            String jwtToken = jwtService.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException();
        }
    }
}
