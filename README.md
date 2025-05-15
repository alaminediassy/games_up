# GamesUP

## Description du projet
GamesUP est une application web de gestion de catalogue et d'achat de jeux de société.  
Le backend est développé avec **Spring Boot** et fournit une API REST sécurisée pour :
- les utilisateurs (authentification, wishlist, achats)
- les administrateurs (gestion de jeux, auteurs, catégories, éditeurs)
- un système de **recommandation intelligent** via un microservice Python.

---

## Technologies utilisées

- Java 21 / Spring Boot 3
- Spring Security (JWT + OAuth2)
- Spring Data JPA (Hibernate)
- MySQL
- Redis (blacklist JWT)
- Python & FastAPI (pour la recommandation)
- JaCoCo (rapport de couverture de tests)

---

## Structure du projet

```

com.gamesup.gamesUP
├── common                # Composants transversaux (init admin)
├── config                # Configuration (JWT, Redis, sécurité)
├── controller            # Contrôleurs REST
├── dto                   # Objets de transfert (requêtes/réponses)
├── exception             # Gestion des erreurs
├── filter                # Filtres JWT
├── mapper                # DTO ↔ Entités
├── model                 # Entités JPA (BDD)
├── repository            # Accès aux données
├── security              # Configuration de la sécurité
├── service               # Logique métier + implémentations
└── resources             # Fichiers de config (application.properties)

````

---

## Authentification & rôles

- Authentification sécurisée avec **JWT**.
- Implémentation d’un **OAuth2 Resource Server**.
- Rôles définis :
  - `ROLE_ADMIN` → gestion des jeux & utilisateurs
  - `ROLE_CLIENT` → consultation, wishlist, achats

---

## Fonctionnalités développées

### Authentification
- `POST /api/public/auth/register` : inscription client
- `POST /api/public/auth/login` : connexion avec JWT

### Gestion des utilisateurs
- `GET /api/private/admin/users` : liste des utilisateurs (admin uniquement)

### Gestion des jeux
- `GET /api/public/games` : tous les jeux
- `GET /api/public/games/{id}` : détail d’un jeu
- `GET /api/public/games/search?name=...` : recherche par nom
- `GET /api/public/games/filter?...` : filtres par catégorie, auteur, éditeur
- `POST /api/private/admin/games` : création (admin)
- `PUT /api/private/admin/games/{id}` : modification (admin)
- `DELETE /api/private/admin/games/{id}` : suppression (admin)

### Données meta (admin uniquement)
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
- `GET /api/private/client/purchases` : historique d’achats

---

## Gestion des erreurs personnalisées

Erreurs capturées automatiquement par `GlobalExceptionHandler` :
- `ResourceNotFoundException`
- `AlreadyExistsException`
- `EmailAlreadyUsedException`
- `BadCredentialsException`

---

## Système de recommandation IA

Un microservice Python, développé avec **FastAPI**, reçoit l’historique d’achats d’un utilisateur pour lui proposer des jeux similaires.

- `POST /recommendations` : envoie une liste de jeux achetés → reçoit des recommandations.
- Backend Java ↔ microservice Python via appel HTTP.

Dossier du microservice : `CodeApiPython/`

---

## Rapport de tests JaCoCo

- Exécution via Maven avec la commande :
  ```bash
  mvn clean test verify

## Rapport généré dans :

  ```
  target/site/jacoco/index.html
  ```
* Couverture atteinte : **95%** sur les classes de service, contrôleurs et logique métier (hors DTOs et entités).

---

## Système de recommandation & bilan critique

### Fonctionnement

Le système recommande des jeux à partir de l’historique d’achat de l’utilisateur. Un appel HTTP envoie la liste des jeux achetés au microservice Python qui retourne une liste triée de jeux similaires selon l’auteur, la catégorie ou l’éditeur.

### Bonnes pratiques

* Architecture modulaire et propre
* Couche sécurité avec JWT bien gérée
* Tests d’intégration JaCoCo complets
* Gestion des erreurs centralisée
* Bonne séparation des responsabilités (DTOs, Services, Mappers…)

### À améliorer

* Pas encore de pagination dans les listings
* Tests parfois sensibles aux ID en base (nettoyage à soigner)
* Pas d’interface utilisateur (API uniquement)
* Pas encore de recommandations basées sur l’analyse du comportement utilisateur (machine learning réel)

---

## Lien vers les dépôts

* 🎯 Backend Java : [github.com/alaminediassy/games\_up](https://github.com/alaminediassy/games_up)
* 🤖 Microservice FastAPI : [github.com/alaminediassy/games\_up\_python\_fastapi](https://github.com/alaminediassy/games_up_python_fastapi)

---

## Auteurs

* Projet développé dans le cadre d’un cas d’étude académique.
* Réalisé par **Mamadou Lamine DIASSY** – Master Expert en Ingénierie Logicielle.

---

## Lancer le projet

```bash
# Lancer le backend Java (port 8092)
./mvnw spring-boot:run

# Lancer le microservice Python (port 8000)
cd CodeApiPython
uvicorn api.main:app --reload
```
