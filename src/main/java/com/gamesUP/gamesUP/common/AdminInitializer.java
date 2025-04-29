package com.gamesUP.gamesUP.common;

import com.gamesUP.gamesUP.model.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Classe qui initialise un compte ADMIN par défaut au lancement de l'application.
 * Cette opération ne s’exécute que si aucun utilisateur avec le rôle ADMIN n’existe.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminInitializer(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Méthode appelée automatiquement au démarrage de l’application.
     * Elle vérifie si un utilisateur admin existe déjà, et sinon, elle en crée un.
     *
     * @param args les arguments de la ligne de commande (non utilisés ici)
     * @throws Exception en cas d’erreur
     */
    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@gamesup.com";
        String adminName = "Administrateur GamesUP";

        boolean adminExists = userRepository.findByEmail(adminEmail).isPresent();

        if (!adminExists) {
            User admin = new User();
            admin.setNom(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(bCryptPasswordEncoder.encode("Admin123@"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            System.out.println("Admin created successfully : " + adminEmail);
        } else {
            System.out.println("This admin exist : " + adminEmail);
        }
    }
}
