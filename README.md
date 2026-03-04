# life-rpg

Spring Boot 4 (Java 21) backend for Life RPG.

## Requirements

- Java 21
- PostgreSQL (database: `liferpg`, user/password: `liferpg`)

## Configuration

Main config file: `src/main/resources/application.yml`

- JWT secret: `AUTH_SECRET` env var (fallback: `dev-secret-change-me`)
- Session cookie name: `session`
- Cookie max age: 7 days
- CORS allowed origin: `http://localhost:3000`

## Run

```powershell
./mvnw spring-boot:run
```

## Test

```powershell
./mvnw test
```

## Auth Endpoints

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`
- `GET /api/v1/me`

`register` and `login` return `{ "userId": "..." }` and set httpOnly session cookie.
`logout` clears cookie and returns `{ "ok": true }`.
