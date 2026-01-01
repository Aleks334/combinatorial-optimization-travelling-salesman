# Travelling Salesman Problem

Console-based Java application solving the Travelling Salesman Problem (TSP) using:
- greedy algorithm
- ant colony optimization algorithm (metaheuristic)

## Test

To run only unit tests use:
```bash
.\gradlew test
```

## Build

```bash
.\gradlew clean build
```

## How to run

### Option 1: Run via Gradle (development)

```bash
.\gradlew.bat run --console=plain
```

### Option 2: Run as standalone console application

Build distribution:

```bash
./gradlew clean installDist
```

Run:

```bash
./build/install/travelling-salesman-problem/bin/travelling-salesman-problem
```

(On Windows: use .bat file in the same directory)

## Requirements

- Java 21 or higher
- Gradle 8.x or higher (included via wrapper)
