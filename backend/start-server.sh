#!/bin/bash
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check if Docker is running
check_docker() {
  echo -e "${BLUE}Checking if Docker is running...${NC}"
  if ! docker info > /dev/null 2>&1; then
    echo -e "${YELLOW}Docker is not running. Please start Docker and try again.${NC}"
    exit 1
  fi
  echo -e "${GREEN}Docker is running!${NC}"
}

# Function to display usage
usage() {
  echo -e "Usage: $0 [option]"
  echo -e "Options:"
  echo -e "  --docker    Start both PostgreSQL and API using Docker Compose"
  echo -e "  --local     Start PostgreSQL in Docker and API locally (default)"
  echo -e "  --db-only   Start only PostgreSQL database"
  echo -e "  --help      Display this help message"
  exit 1
}

# Check command line arguments
MODE="local"
if [ $# -gt 0 ]; then
  case "$1" in
    --docker)
      MODE="docker"
      ;;
    --local)
      MODE="local"
      ;;
    --db-only)
      MODE="db-only"
      ;;
    --help)
      usage
      ;;
    *)
      echo -e "${YELLOW}Unknown option: $1${NC}"
      usage
      ;;
  esac
fi

# Make sure Docker is running
check_docker

# Execute based on mode
case "$MODE" in
  docker)
    echo -e "${BLUE}Starting PostgreSQL and API with Docker Compose...${NC}"
    docker-compose down 2>/dev/null || true
    docker-compose up
    ;;

  local)
    echo -e "${BLUE}Starting PostgreSQL with Docker...${NC}"
    docker-compose down 2>/dev/null || true
    docker-compose up -d postgres
    
    # Wait for PostgreSQL to be ready
    echo -e "${BLUE}Waiting for PostgreSQL to be ready...${NC}"
    until docker-compose exec postgres pg_isready -U postgres > /dev/null 2>&1; do
      echo -n "."
      sleep 1
    done
    echo -e "\n${GREEN}PostgreSQL is ready!${NC}"
    
    echo -e "${BLUE}Starting the API locally...${NC}"
    ./gradlew run
    ;;

  db-only)
    echo -e "${BLUE}Starting only PostgreSQL with Docker...${NC}"
    docker-compose down 2>/dev/null || true
    docker-compose up -d postgres
    
    # Wait for PostgreSQL to be ready
    echo -e "${BLUE}Waiting for PostgreSQL to be ready...${NC}"
    until docker-compose exec postgres pg_isready -U postgres > /dev/null 2>&1; do
      echo -n "."
      sleep 1
    done
    echo -e "\n${GREEN}PostgreSQL is ready at localhost:5432${NC}"
    echo -e "${GREEN}Database: taskman${NC}"
    echo -e "${GREEN}Username: postgres${NC}"
    echo -e "${GREEN}Password: postgres${NC}"
    ;;
esac