# URL Shortlink Service (Spring Boot + Postgres + Flyway)

A small **production-style URL shortener service** built with **Spring Boot**, **PostgreSQL**, and **Flyway**.

It supports:
- Creating short codes for URLs
- Redirecting short codes to original URLs
- Persistent storage via PostgreSQL (survives restarts)
- Docker Compose setup for local development
- Database migrations via Flyway

---

## Tech Stack
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL 16
- Flyway
- Maven
- Docker + Docker Compose

---

## Running the Service (Docker Compose)

### Prerequisites
- Docker Desktop
- Docker Compose

## API
> Windows note: if you are using PowerShell/CMD, line continuation differs.
> The README curl examples are written for Git Bash/macOS/Linux (use `\`).

### Start everything
```bash
docker compose up -d --build

### Full reset (delete DB volume) (Very needed)
docker compose down -v