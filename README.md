# Travelling Salesman Problem - Multiple Algorithms

Java implementation of the Traveling Salesman Problem (TSP) using multiple optimization algorithms.

> 📚 **[Zobacz pełną dokumentację](DOCUMENTATION_INDEX.md)** - Indeks wszystkich plików dokumentacji

## Implemented Algorithms

### 1. Greedy Algorithm (Nearest Neighbor)
Simple heuristic that:
1. Starts at the first city
2. Always goes to the nearest unvisited city
3. Returns to the starting city

**Complexity**: O(n²)  
**Advantages**: Fast, simple to implement  
**Disadvantages**: Often produces suboptimal solutions

### 2. Ant Colony Optimization (ACO)
Metaheuristic inspired by foraging behavior of ants:
1. Virtual ants construct solutions probabilistically
2. Pheromone trails guide the search
3. Iterative refinement leads to better solutions

**Parameters**:
- α (alpha) = 1.0 - pheromone weight
- β (beta) = 2.0 - heuristic weight (visibility)
- ρ (rho) = 0.1 - evaporation coefficient
- Iterations = 100
- Number of ants = number of cities

**Advantages**: 
- Often finds near-optimal solutions
- Can escape local optima
- Flexible and adaptable

**Disadvantages**: 
- Slower than greedy algorithm
- Requires parameter tuning

## Project Structure

```
src/
├── main/java/org/example/
│   ├── Main.java                          # Main application
│   ├── Point.java                         # 2D point representation
│   ├── PointGenerator.java                # Random point generator
│   ├── DataFileHandler.java               # File I/O operations
│   ├── antColony/
│   │   └── AntColonyTspSolver.java       # ACO implementation
│   └── tsp/
│       ├── City.java                      # City (extends Point)
│       ├── Tour.java                      # Tour representation
│       ├── TspSolver.java                 # Solver interface
│       └── GreedyTspSolver.java          # Greedy algorithm
└── test/java/org/example/
    ├── antColony/
    │   └── AntColonyTspSolverTest.java   # ACO unit tests
    ├── integration/
    │   └── TspSolverComparisonTest.java   # Algorithm comparison
    └── tsp/
        ├── TourTest.java                  # Tour unit tests
        ├── CityTest.java                  # City unit tests
        └── GreedyTSPSolverTest.java      # Greedy tests
```

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
// TspSolver solver = new GreedyTspSolver();
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

## Documentation

For detailed information about the Ant Colony Optimization implementation, see [ACO_DOCUMENTATION.md](ACO_DOCUMENTATION.md).

## Testing

The project includes comprehensive unit and integration tests:

- **Unit Tests**: Test individual components (Tour, City, Solvers)
- **Integration Tests**: Compare different algorithms
- **Edge Cases**: Single city, two cities, etc.

All tests use JUnit 5 framework.

## Requirements

- Java 11 or higher
- Gradle 7.x or higher (included via wrapper)

## License

This project is for educational purposes.

