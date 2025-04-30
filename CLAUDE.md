# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build/Test/Lint Commands
- Build: `./gradlew build` (backend)
- Run: `./gradlew run` (backend)
- Test (all): `./gradlew test` (backend)
- Test (single): `./gradlew test --tests "com.taskman.TaskControllerTest"` (backend)
- Clean: `./gradlew clean` (backend)

## Code Style Guidelines
- **Formatting**: Use ktlint for Kotlin code formatting
- **Imports**: Group imports by standard library/third-party/internal, sort alphabetically
- **Types**: Use explicit return types for public functions
- **Naming**: camelCase for variables/functions, PascalCase for classes
- **Error Handling**: Use sealed classes or Result type for error handling when appropriate
- **API Design**: Favor immutable data classes for models
- **Testing**: Write unit tests for all public API methods
- **Documentation**: Use KDoc comments for public functions and classes

## Project Structure
This is a task management application with a Micronaut Kotlin backend.