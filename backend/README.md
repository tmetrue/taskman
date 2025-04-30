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
- `POST /api/auth/login` - Login and get JWT token
- `POST /api/auth/logout` - Logout (requires authentication)
- `GET /api/auth/me` - Get current user profile (requires authentication)
- `GET /api/auth/check-token` - Validate JWT token

### Categories

- `GET /api/categories` - List all categories (anonymous allowed)
- `GET /api/categories/{id}` - Get a specific category (anonymous allowed)
- `POST /api/categories` - Create a new category (admin only)
- `PUT /api/categories/{id}` - Update a category (admin only)
- `DELETE /api/categories/{id}` - Delete a category (admin only)

### Tasks

- `GET /api/tasks` - List all tasks (anonymous allowed)
- `GET /api/tasks/{id}` - Get a specific task (anonymous allowed)
- `GET /api/tasks/my` - Get current user's tasks (requires authentication)
- `GET /api/tasks/status/{completed}` - Get tasks by completion status (anonymous allowed)
- `GET /api/tasks/category/{categoryId}` - Get tasks by category (anonymous allowed)
- `POST /api/tasks` - Create a new task (requires authentication)
- `PUT /api/tasks/{id}` - Update a task (requires authentication, owner only)
- `DELETE /api/tasks/{id}` - Delete a task (requires authentication, owner only)

### Admin Only

- `GET /api/tasks` - List all tasks (admin only)
- `GET /api/admin/users` - List all users
- `GET /api/admin/users/{id}` - Get a specific user
- `POST /api/admin/users` - Create a new user
- `PUT /api/admin/users/{id}` - Update a user
- `DELETE /api/admin/users/{id}` - Delete a user

## Default Admin Account

A default admin account is created automatically:
- Username: `admin`
- Password: `adminSecure123!`

This should be changed in production.

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
| JWT_SECRET | Secret key for JWT token generation | pleaseChangeThisSecretForANewSecretAndMakeItSecure |

## Logging

Authentication failures and security-related events are logged to both console and file:

- Console output for all logs
- File logging to `logs/taskman.log` with daily rolling policy
- Specific detailed logging for authentication failures