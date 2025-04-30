#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Stopping all containers...${NC}"
docker-compose down

echo -e "${GREEN}All services stopped.${NC}"