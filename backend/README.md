# Taskman Backend

A task management API built with Kotlin, Micronaut, and PostgreSQL.

## Features

- RESTful API for task management
- PostgreSQL database for persistence
- Flyway for database migrations
- Docker and Docker Compose setup for easy deployment

## Prerequisites

- JDK 17 or higher
- Gradle 8.5 or higher
- Docker and Docker Compose (for running PostgreSQL locally)

## Running Locally

### Using the Convenience Scripts

We provide scripts to easily start and stop the server:

1. Start everything (choose one option):

```bash
# Start PostgreSQL in Docker and run API locally (default)
./start-server.sh

# Start both PostgreSQL and API with Docker
./start-server.sh --docker

# Start only the PostgreSQL database
./start-server.sh --db-only
```

2. Stop everything:

```bash
./stop-server.sh
```

### Manual Options

#### Option 1: Using Docker Compose

This will start both PostgreSQL and the API:

```bash
docker-compose up
```

#### Option 2: Run PostgreSQL via Docker and the API locally

1. Start PostgreSQL:

```bash
docker-compose up postgres
```

2. Run the API:

```bash
./gradlew run
```

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /login` - Login and get JWT token

### Tasks (Protected - requires authentication)

- `GET /api/tasks/my` - Get current user's tasks
- `GET /api/tasks/{id}` - Get a specific task (if owned by user)
- `GET /api/tasks/status/{completed}` - Get user's tasks by completion status
- `POST /api/tasks` - Create a new task
- `PUT /api/tasks/{id}` - Update a task (if owned by user)
- `DELETE /api/tasks/{id}` - Delete a task (if owned by user)

### Admin Only

- `GET /api/tasks` - List all tasks (admin only)

## Database Migrations

Migrations are managed with Flyway and run automatically when the application starts.

To add a new migration:

1. Create a new SQL file in `src/main/resources/db/migration`
2. Name it using the following pattern: `V{number}__{description}.sql`
   - Example: `V2__add_priority_column.sql`

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| JDBC_URL | PostgreSQL connection URL | jdbc:postgresql://localhost:5432/taskman |
| JDBC_USER | PostgreSQL username | postgres |
| JDBC_PASSWORD | PostgreSQL password | postgres |