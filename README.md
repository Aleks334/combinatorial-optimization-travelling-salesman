# Travelling Salesman Problem

Java implementation of the Traveling Salesman Problem (TSP) using greedy algorithm and metaheuristics.

## How to Run

### Build and Test
```bash
# Windows
.\gradlew.bat build

# Linux/Mac
./gradlew build
```

### Run the Program
```bash
# Windows
.\gradlew.bat run

# Linux/Mac
./gradlew run
```

### Run Tests
```bash
# All tests
.\gradlew.bat test

# Specific test class
.\gradlew.bat test --tests org.example.antColony.AntColonyTspSolverTest

# View test report
# Open: build/reports/tests/test/index.html
```

## Switching Between Algorithms

In `Main.java`, you can switch between algorithms:

```java
// Use Ant Colony Optimization (default)
TspSolver solver = new AntColonyTspSolver(points);

// Or use Greedy Algorithm
TspSolver solver = new GreedyTspSolver();
```

## Example Output

```
=== Rozpoczęcie Algorytmu Mrówkowego ===
Liczba miast: 10
Liczba mrówek: 10
Maksymalna liczba iteracji: 100
Parametry: α=1.0, β=2.0, ρ=0.1

Iteracja 10: Najlepsza długość trasy = 245.32
Iteracja 20: Najlepsza długość trasy = 238.17
...
Iteracja 100: Najlepsza długość trasy = 230.45

=== Algorytm zakończony ===
Najlepsza długość trasy: 230.45
City1 -> City3 -> City7 -> City2 -> ... (Distance: 230.45)
```

## Testing

The project includes comprehensive unit tests.

All tests use JUnit 5 framework.

## Requirements

- Java 11 or higher
- Gradle 7.x or higher (included via wrapper)
