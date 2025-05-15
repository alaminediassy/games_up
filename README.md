# GamesUP

## Description du projet

**GamesUP** est une application web de gestion de catalogue et d'achat de jeux de société. Le backend est développé avec **Spring Boot** et fournit une API REST permettant aux utilisateurs de s’inscrire, d’acheter des jeux, de gérer leur wishlist, et aux administrateurs de gérer le contenu du catalogue.

---

## Technologies utilisées

* Java 21
* Spring Boot 3
* Spring Security (JWT + OAuth2 Resource Server)
* Spring Data JPA (Hibernate)
* MySQL
* Redis (pour la blacklist des tokens JWT)
* FastAPI (microservice de recommandation)

---

## Structure du projet

```
com.gamesup.gamesUP
├── common                # Composants transversaux (initialisation admin)
├── config                # Configurations (JWT, Redis, sécurité)
├── controller            # Contrôleurs REST
├── dto                   # Objets Request/Response
├── exception             # Gestion centralisée des erreurs
├── filter                # Filtrage JWT
├── mapper                # Conversion entités ↔ DTO
├── model                 # Entités JPA (BDD)
├── repository            # Interfaces pour l’accès aux données
├── security              # Gestion de la sécurité
├── service               # Logique métier et implémentations
└── resources             # Fichiers de configuration (application.properties)
```

---

## Authentification & Rôles

Authentification sécurisée par **JWT** avec gestion des rôles :

* `ROLE_ADMIN` : gestion complète du catalogue et des utilisateurs
* `ROLE_CLIENT` : consultation, wishlist et achats

---

## Fonctionnalités développées

### Authentification

* `POST /api/public/auth/register` – Inscription
* `POST /api/public/auth/login` – Connexion

### Gestion des utilisateurs (admin)

* `GET /api/private/admin/users` – Liste des utilisateurs

### Gestion des jeux

* `GET /api/public/games` – Tous les jeux
* `GET /api/public/games/{id}` – Détail d’un jeu
* `GET /api/public/games/search?name=...` – Recherche par nom
* `GET /api/public/games/filter?...` – Recherche combinée
* `POST /api/private/admin/games` – Création (admin)
* `PUT /api/private/admin/games/{id}` – Modification (admin)
* `DELETE /api/private/admin/games/{id}` – Suppression (admin)

### Données meta (admin)

* `POST /api/private/admin/meta/authors`
* `POST /api/private/admin/meta/categories`
* `POST /api/private/admin/meta/publishers`
* `GET /api/public/authors`
* `GET /api/public/categories`
* `GET /api/public/publishers`

### Wishlist (client)

* `GET /api/private/client/wishlist`
* `POST /api/private/client/wishlist/{gameId}`
* `DELETE /api/private/client/wishlist/{gameId}`

### Achats (client)

* `POST /api/private/client/purchases`
* `GET /api/private/client/purchases`

---

## Gestion des erreurs personnalisées

* `ResourceNotFoundException`
* `AlreadyExistsException`
* `EmailAlreadyUsedException`
* `BadCredentialsException`
* `GlobalExceptionHandler` centralise toutes les erreurs

---

## Recommandations (FastAPI – Python)

Un microservice Python traite les historiques d’achats pour proposer des jeux similaires :

* `POST /recommendations` – retourne une liste recommandée

Dossier : `CodeApiPython/`

---

## Couverture des tests

Généré avec **JaCoCo** :

* **Couverture globale : 95 %**
* Tests d'intégration pour : authentification, jeux, wishlist, achats, meta

Fichier du rapport : `target/site/jacoco/index.html`

---

## Développement

### Exécution du backend Java

```bash
./mvnw spring-boot:run
```

### Lancer l’API de recommandations (Python)

```bash
cd CodeApiPython
uvicorn api.main:app --reload
```

---

## Auteurs et contribution

* Projet réalisé dans le cadre d’une étude de cas universitaire
* Développé par **\[Ton Nom]**, étudiant en Master 2 Ingénierie Logicielle
* Encadrant : **\[Nom du tuteur]**
