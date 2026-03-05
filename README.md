# life-rpg

Spring Boot 3 (Java 21) backend for Life RPG.

## Requirements

- Java 21
- PostgreSQL

## Configuration

Main config file: `src/main/resources/application.yml`

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
