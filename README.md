# Kanshi API

<img src="https://img.shields.io/badge/coverage-67.90%25-blue" alt="coverage badge"> <img src="https://img.shields.io/badge/dependencies-up%20to%20date-brightgreen" alt="dependencies badge"> <img src="https://img.shields.io/badge/license-MIT-green" alt="license badge"> <img src="https://img.shields.io/badge/version-1.0.1-blue" alt="version badge">

This is the API for the Kanshi project. It is a RESTful API that allows users to create, read, update and delete recommendations for Animes.

## Running instructions

1. Clone the repository
2. Create a `.env` file in the root directory of the project
3. Add and set these following environment variables on the `.env` file:
   - `POSTGRES_USER`
   - `POSTGRES_PASSWORD`
   - `POSTGRES_DB`
   - `TZ`
4. Run `docker compose -f docker-compose.prod.yml up` to start the server
5. The server will be running on `http://localhost:8000`. You can access the API documentation on `http://localhost:8000/swagger-ui.html`
