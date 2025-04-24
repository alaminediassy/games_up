# GamesUP

## Description du projet
GamesUP est une application web de gestion de catalogue et d'achat de jeux de société. Le backend est développé avec **Spring Boot** et fournit une API REST pour les utilisateurs, les administrateurs, la gestion des jeux, la wishlist et les achats.

---

## Technologies utilisées
- Java 21
- Spring Boot 3
- Spring Security (JWT + OAuth2)
- Spring Data JPA (Hibernate)
- MySQL
- Redis (blacklist JWT)
- FastAPI (pour la partie IA)

---

## Structure du projet

```
com.gamesup.gamesUP
├── common                # Composants transversaux (init admin)
├── config                # Configuration (JWT, Redis)
├── controller            # Tous les contrôleurs REST
├── dto                   # Données entrantes/sortantes (request/response)
├── exception             # Gestion centralisée des erreurs
├── filter                # Filtres JWT
├── mapper                # Mappers entité ↔ DTO
├── model                 # Entités JPA (BDD)
├── repository            # Interfaces d'accès aux données
├── security              # Configuration de la sécurité
├── service               # Services metier + implémentations
└── resources             # Fichiers de config (application.properties)
```

---

## Authentification & Rôles
- Utilisation de **JWT** avec **OAuth2 Resource Server**
- 3 rôles :
    - `ROLE_ADMIN`
    - `ROLE_CLIENT`
    - (extension possible : `ROLE_MODERATOR`, `ROLE_JURIST`, etc.)

---

## Fonctionnalités développées

### Authentification
- `POST /api/public/auth/register` : inscription client
- `POST /api/public/auth/login` : connexion avec JWT

### Gestion des utilisateurs
- `GET /api/private/admin/users` : liste des utilisateurs (admin uniquement)

### Gestion des jeux
- `GET /api/public/games` : liste de tous les jeux
- `GET /api/public/games/{id}` : détail d'un jeu
- `GET /api/public/games/search?name=...` : recherche par nom
- `GET /api/public/games/filter?...` : recherche par catégorie, auteur, éditeur, nom
- `POST /api/private/admin/games` : création d'un jeu (admin)
- `PUT /api/private/admin/games/{id}` : modification (admin)
- `DELETE /api/private/admin/games/{id}` : suppression (admin)

### Gestion des données meta (admin)
- `POST /api/private/admin/meta/authors`
- `POST /api/private/admin/meta/categories`
- `POST /api/private/admin/meta/publishers`
- `GET /api/public/authors`
- `GET /api/public/categories`
- `GET /api/public/publishers`

### Wishlist (client)
- `GET /api/private/client/wishlist`
- `POST /api/private/client/wishlist/{gameId}`
- `DELETE /api/private/client/wishlist/{gameId}`

### Achats (client)
- `POST /api/private/client/purchases` : effectuer un achat
- `GET /api/private/client/purchases` : historique de l'utilisateur

---

## Gestion des erreurs personnalisées
- `ResourceNotFoundException`
- `AlreadyExistsException`
- `EmailAlreadyUsedException`
- `BadCredentialsException`
- `GlobalExceptionHandler` centralise tous les cas

---

## API IA (FastAPI - Python)
Un microservice Python expose une route :
- `POST /recommendations` : reçoit les achats de l'utilisateur et retourne des recommandations

Structure côté Python : `CodeApiPython/`

---

## Historique des fonctionnalités ajoutées (exemples de commits)
- `feat: gestion complète des jeux (CRUD admin + recherche)`
- `feat: implémentation wishlist client`
- `feat: ajout fonctionnalité d'achat (purchase)`
- `feat: API de recommandation FastAPI`

---

## Auteurs et contribution
- Projet encadré dans le cadre d'une étude de cas
- Développé par [Nom de l'étudiant], M2 Ingénierie Logicielle
- Tuteur : [Nom du tuteur]

---

## Lancer le projet

```bash
# Lancer le backend Java
./mvnw spring-boot:run

# Lancer l'API Python (en local)
cd CodeApiPython
uvicorn api.main:app --reload
```


