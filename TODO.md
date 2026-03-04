# TODO

## To Do

- Add integration tests with real PostgreSQL + Flyway profile.
- Add endpoint-level MockMvc tests for cookie lifecycle.
- Add CI workflow for build + test.

## In Progress

- None.

## Done

- Implemented register/login/logout/me auth flow.
- Added JWT cookie security filter and stateless Spring Security config.
- Added Flyway migration for `users` and `user_profile`.
- Added validation DTOs, repositories, entities, services, controllers.
- Added global exception handling for 400/401/409 responses.
- Added minimal unit tests and validated with `./mvnw test`.
- Applied `.codex/rules` formatting and structure conventions.
