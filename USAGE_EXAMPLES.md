# Przykłady Użycia Algorytmu Mrówkowego

## Przykład 1: Podstawowe użycie

```java
import org.example.Point;
import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.Tour;
import java.util.Arrays;
import java.util.List;

public class BasicUsageExample {
    public static void main(String[] args) {
        // 1. Przygotuj punkty (miasta)
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(10, 0),
            new Point(10, 10),
            new Point(0, 10)
        );

        // 2. Utwórz miasta z punktów
        List<City> cities = Arrays.asList(
            new City("Warszawa", 0, 0),
            new City("Kraków", 10, 0),
            new City("Gdańsk", 10, 10),
            new City("Wrocław", 0, 10)
        );

        // 3. Utwórz solver i rozwiąż problem
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        // 4. Wyświetl wyniki
        System.out.println("Znaleziona trasa:");
        System.out.println(tour);
        System.out.println("Całkowita długość: " + tour.getTotalDistance());
    }
}
```

**Wyjście:**
```
=== Rozpoczęcie Algorytmu Mrówkowego ===
Liczba miast: 4
...
Znaleziona trasa:
Warszawa -> Kraków -> Gdańsk -> Wrocław -> Warszawa (Distance: 40.00)
Całkowita długość: 40.0
```

## Przykład 2: Wczytywanie miast z pliku

```java
import org.example.DataFileHandler;
import org.example.Point;
import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.Tour;
import java.util.ArrayList;
import java.util.List;

public class FileInputExample {
    public static void main(String[] args) {
        // 1. Wczytaj punkty z pliku
        DataFileHandler fileHandler = new DataFileHandler("dane.txt");
        List<Point> points = fileHandler.readPoints();

        if (points == null || points.isEmpty()) {
            System.err.println("Błąd: Nie można wczytać danych z pliku");
            return;
        }

        // 2. Konwertuj punkty na miasta
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            cities.add(new City("Miasto" + (i + 1), p.getX(), p.getY()));
        }

        // 3. Rozwiąż problem
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        // 4. Wyświetl statystyki
        System.out.println("\n=== Statystyki ===");
        System.out.println("Liczba miast: " + cities.size());
        System.out.println("Długość trasy: " + String.format("%.2f", tour.getTotalDistance()));
        
        // 5. Wyświetl pierwszych 5 miast w trasie
        List<City> tourCities = tour.getCities();
        System.out.println("\nPierwszych 5 miast:");
        for (int i = 0; i < Math.min(5, tourCities.size()); i++) {
            City city = tourCities.get(i);
            System.out.println("  " + city.getName() + " (" + 
                             city.getX() + ", " + city.getY() + ")");
        }
    }
}
```

## Przykład 3: Porównanie algorytmów

```java
import org.example.Point;
import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.*;
import java.util.Arrays;
import java.util.List;

public class AlgorithmComparisonExample {
    public static void main(String[] args) {
        // Przygotuj dane testowe
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(20, 10),
            new Point(15, 30),
            new Point(5, 25),
            new Point(10, 15)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 20, 10),
            new City("C", 15, 30),
            new City("D", 5, 25),
            new City("E", 10, 15)
        );

        // Algorytm mrówkowy
        System.out.println("=== Algorytm Mrówkowy ===");
        long startTime1 = System.currentTimeMillis();
        TspSolver antSolver = new AntColonyTspSolver(points);
        Tour antTour = antSolver.solve(cities);
        long time1 = System.currentTimeMillis() - startTime1;

        System.out.println("Czas: " + time1 + " ms");
        System.out.println("Długość: " + String.format("%.2f", antTour.getTotalDistance()));

        // Algorytm zachłanny
        System.out.println("\n=== Algorytm Zachłanny ===");
        long startTime2 = System.currentTimeMillis();
        TspSolver greedySolver = new GreedyTspSolver();
        Tour greedyTour = greedySolver.solve(cities);
        long time2 = System.currentTimeMillis() - startTime2;

        System.out.println("Czas: " + time2 + " ms");
        System.out.println("Długość: " + String.format("%.2f", greedyTour.getTotalDistance()));

        // Porównanie
        System.out.println("\n=== Porównanie ===");
        double improvement = ((greedyTour.getTotalDistance() - antTour.getTotalDistance()) 
                             / greedyTour.getTotalDistance()) * 100;
        System.out.println("Poprawa: " + String.format("%.2f%%", improvement));
    }
}
```

**Przykładowe wyjście:**
```
=== Algorytm Mrówkowy ===
Czas: 1234 ms
Długość: 95.42

=== Algorytm Zachłanny ===
Czas: 5 ms
Długość: 102.37

=== Porównanie ===
Poprawa: 6.79%
```

## Przykład 4: Generowanie losowych miast

```java
import org.example.Point;
import org.example.PointGenerator;
import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.Tour;
import java.util.ArrayList;
import java.util.List;

public class RandomCitiesExample {
    public static void main(String[] args) {
        // 1. Wygeneruj losowe miasta
        PointGenerator generator = new PointGenerator();
        List<Point> points = generator.generate(); // Generuje 10 losowych punktów

        // 2. Konwertuj na miasta
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            cities.add(new City("Miasto" + (i + 1), p.getX(), p.getY()));
        }

        // 3. Rozwiąż problem
        System.out.println("Rozwiązywanie TSP dla " + cities.size() + " losowych miast...\n");
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        Tour tour = solver.solve(cities);

        // 4. Wyświetl wyniki
        System.out.println("\n=== Wyniki ===");
        System.out.println("Znaleziona trasa:");
        System.out.println(tour);
    }
}
```

## Przykład 5: Wielokrotne uruchomienie dla najlepszego wyniku

```java
import org.example.Point;
import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.Tour;
import java.util.Arrays;
import java.util.List;

public class MultipleRunsExample {
    public static void main(String[] args) {
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(50, 20),
            new Point(40, 60),
            new Point(10, 50),
            new Point(25, 30)
        );

        List<City> cities = Arrays.asList(
            new City("A", 0, 0),
            new City("B", 50, 20),
            new City("C", 40, 60),
            new City("D", 10, 50),
            new City("E", 25, 30)
        );

        int numberOfRuns = 5;
        Tour bestTour = null;
        double bestDistance = Double.MAX_VALUE;

        System.out.println("Uruchamianie algorytmu " + numberOfRuns + " razy...\n");

        for (int i = 1; i <= numberOfRuns; i++) {
            System.out.println("Uruchomienie " + i + ":");
            AntColonyTspSolver solver = new AntColonyTspSolver(points);
            Tour tour = solver.solve(cities);
            
            double distance = tour.getTotalDistance();
            System.out.println("Dystans: " + String.format("%.2f", distance));

            if (distance < bestDistance) {
                bestDistance = distance;
                bestTour = tour;
                System.out.println("★ Nowe najlepsze rozwiązanie!");
            }
            System.out.println();
        }

        System.out.println("=== Najlepsze Rozwiązanie ===");
        System.out.println(bestTour);
        System.out.println("Długość: " + String.format("%.2f", bestDistance));
    }
}
```

## Przykład 6: Analiza zbieżności (wymaga modyfikacji kodu)

Aby monitorować zbieżność algorytmu, można dodać listener:

```java
// W przyszłości można rozszerzyć AntColonyTspSolver o mechanizm callbacków
public class ConvergenceAnalysisExample {
    public static void main(String[] args) {
        // Przygotuj dane
        List<Point> points = /* ... */;
        List<City> cities = /* ... */;

        // Utwórz solver z obserwatorem
        AntColonyTspSolver solver = new AntColonyTspSolver(points);
        
        // Uruchom solver
        Tour tour = solver.solve(cities);

        // Wyświetl wyniki
        System.out.println("Najlepsza trasa: " + tour.getTotalDistance());
    }
}
```

## Wskazówki Praktyczne

### 1. Dla małych problemów (< 10 miast)
- Algorytm znajdzie rozwiązanie bardzo szybko
- Można użyć mniej iteracji (50)

### 2. Dla średnich problemów (10-30 miast)
- Użyj domyślnych parametrów
- Porównaj z algorytmem zachłannym

### 3. Dla dużych problemów (> 30 miast)
- Zwiększ liczbę iteracji (300+)
- Rozważ wielokrotne uruchomienie
- Użyj parametryzacji z PARAMETER_TUNING_GUIDE.md

### 4. Optymalizacja wydajności
```java
// Dla szybszego działania - mniej iteracji
private static final int MAX_ITERATIONS = 50;

// Dla lepszych wyników - więcej iteracji
private static final int MAX_ITERATIONS = 300;
```

## Typowe Problemy i Rozwiązania

### Problem: Algorytm działa zbyt długo
**Rozwiązanie**: Zmniejsz MAX_ITERATIONS lub numberOfAnts w AntColonyTspSolver.java

### Problem: Słabe wyniki
**Rozwiązanie**: Zwiększ MAX_ITERATIONS, dostosuj parametry α i β

### Problem: Wielokrotne uruchomienia dają bardzo różne wyniki
**Rozwiązanie**: To normalne zachowanie metaheurystyki. Uruchom algorytm kilka razy i wybierz najlepsze rozwiązanie.

## Dalsze Kroki

1. Przeczytaj [ACO_DOCUMENTATION.md](ACO_DOCUMENTATION.md) dla szczegółów implementacji
2. Zobacz [PARAMETER_TUNING_GUIDE.md](PARAMETER_TUNING_GUIDE.md) dla dostrajania parametrów
3. Uruchom testy: `.\gradlew.bat test`
4. Eksperymentuj z własnymi zestawami miast!

