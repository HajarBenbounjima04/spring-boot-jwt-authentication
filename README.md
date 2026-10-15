# TP2 : Authentification avec JWT et Spring Security

API REST sécurisée par **JSON Web Token (JWT)**, réalisée avec Spring Boot et Spring Security.
Un utilisateur s'inscrit, se connecte, reçoit un token, puis l'envoie dans le header `Authorization` pour accéder aux routes protégées.

## Technologies

- Java 21 (17 minimum)
- Spring Boot 4.1.1
- Spring Web, Spring Security, Spring Data JPA
- H2 Database (en mémoire)
- JJWT 0.13.0
- Maven

## Architecture

```
src/main/java/ma/enset/tpjwtspring
├── controller
│   ├── AuthController.java        # /auth/register et /auth/login
│   └── UserController.java        # /api/hello et /api/profile (protégés)
├── dto
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   └── AuthResponse.java
├── entity
│   └── AppUser.java
├── repository
│   └── UserRepository.java
├── security
│   ├── JwtFilter.java             # lit et valide le token à chaque requête
│   └── SecurityConfig.java        # règles d'accès, stateless, BCrypt
├── service
│   ├── CustomUserDetailsService.java
│   └── JwtService.java            # génération et validation du token
└── TpJwtSpringApplication.java
```

## Fonctionnement

1. `POST /auth/register` : crée l'utilisateur, mot de passe haché avec BCrypt.
2. `POST /auth/login` : vérifie les identifiants et renvoie un JWT.
3. Pour chaque requête vers `/api/**`, `JwtFilter` lit `Authorization: Bearer <token>`, valide le token et authentifie l'utilisateur.
4. Sans token valide, l'accès à `/api/**` est refusé (403).

## Lancer le projet

Prérequis : JDK 17 ou plus.

```bash
git clone <url-du-depot>
cd tp-jwt-spring
./mvnw spring-boot:run
```

L'application démarre sur `http://localhost:8080`.

## Configuration

Fichier `src/main/resources/application.properties` :

```properties
jwt.secret=<cle-base64-d-au-moins-32-octets>
jwt.expiration=3600000
```

Pour générer une clé : `openssl rand -base64 32`

> La clé présente dans ce dépôt sert uniquement au TP. Ne jamais publier une vraie clé de production.

## Endpoints

| Méthode | URL | Accès | Description |
|---|---|---|---|
| POST | `/auth/register` | Public | Création d'un compte |
| POST | `/auth/login` | Public | Connexion, retourne le token |
| GET | `/api/hello` | Token requis | Message de bienvenue |
| GET | `/api/profile` | Token requis | Utilisateur connecté |
| GET | `/h2-console` | Public | Console de la base H2 |

## Tests avec curl

```bash
# Inscription
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"hajar@test.com","password":"1234"}'

# Connexion
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"hajar@test.com","password":"1234"}'

# Route protégée
curl http://localhost:8080/api/hello \
  -H "Authorization: Bearer <TOKEN>"
```

Résultats attendus :

| Test | Résultat |
|---|---|
| `/api/hello` sans token | 403 |
| `/auth/register` | 200, `Utilisateur cree` |
| `/auth/login` | 200, `{"token":"..."}` |
| `/api/hello` avec token | `Bienvenue dans l'API securisee` |
| `/api/profile` avec token | `Utilisateur connecte : hajar@test.com` |

## Console H2

- URL : `http://localhost:8080/h2-console`
- JDBC URL : `jdbc:h2:mem:jwtdb`
- User : `sa`, mot de passe vide

## Auteure

Hajar : TP2, ENSET
