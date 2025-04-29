package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.request.UserRegisterDTO;
import com.gamesUP.gamesUP.dto.response.UserResponseDTO;
import com.gamesUP.gamesUP.exception.EmailAlreadyUsedException;
import com.gamesUP.gamesUP.mapper.UserMapper;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscription d'un nouvel utilisateur.
     * Étapes :
     * 1. Vérification si l'email est déjà utilisé → lève une exception si c'est le cas.
     * 2. Convertion du DTO d'inscription en entité User.
     * 3. Encodage du mot de passe avec BCrypt.
     * 4. Enregistrement de l'utilisateur dans la base de données.
     * 5. Retourne un DTO de réponse contenant les infos publiques de l'utilisateur.
     *
     * @param dto données d'inscription fournies par l'utilisateur
     * @return UserResponseDTO contenant le nom, l'email et le rôle de l'utilisateur créé
     */
    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getNom(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }
}
