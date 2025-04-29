package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.request.PurchaseCreateDTO;
import com.gamesUP.gamesUP.dto.request.PurchaseDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.mapper.PurchaseMapper;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.service.PurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    // Constructeur pour injecter les dépendances nécessaires au service
    public PurchaseServiceImpl(UserRepository userRepository,
                               GameRepository gameRepository,
                               PurchaseRepository purchaseRepository,
                               PurchaseMapper purchaseMapper) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }


    /**
     * Crée un nouvel achat pour un utilisateur donné.
     * - Vérifie que l'utilisateur existe.
     * - Crée une nouvelle entité Purchase.
     * - Crée les lignes d'achat en récupérant les jeux associés.
     * - Sauvegarde l'achat et retourne le résultat sous forme de DTO.
     *
     * @param userEmail email de l’utilisateur qui effectue l’achat
     * @param dto données d’entrée
     * @return un DTO représentant l’achat enregistré
     */
    @Override
    @Transactional
    public PurchaseDTO createPurchase(String userEmail, PurchaseCreateDTO dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userEmail));

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPaid(true);
        purchase.setDelivered(false);
        purchase.setArchived(false);

        List<PurchaseLine> lines = dto.lines().stream()
                .map(lineDTO -> {
                    Game game = gameRepository.findById(lineDTO.gameId())
                            .orElseThrow(() -> new ResourceNotFoundException("Jeu", lineDTO.gameId()));

                    return new PurchaseLine(
                            (Long) null,
                            (int) lineDTO.rating(),
                            game.getPrice(),
                            game,
                            purchase
                    );
                }).toList();

        purchase.setLines(lines);

        Purchase saved = purchaseRepository.save(purchase);
        return purchaseMapper.toDTO(saved);
    }

    /**
     * Récupère tous les achats d’un utilisateur.
     *
     * @param userEmail email de l’utilisateur
     * @return liste des achats au format DTO
     */
    @Override
    public List<PurchaseDTO> getPurchasesByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", userEmail));

        return purchaseRepository.findByUser(user).stream()
                .map(purchaseMapper::toDTO)
                .toList();
    }
}
