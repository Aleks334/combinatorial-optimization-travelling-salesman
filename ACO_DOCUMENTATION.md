# Algorytm Mrówkowy (Ant Colony Optimization - ACO) dla TSP

## Opis implementacji

Implementacja algorytmu mrówkowego do rozwiązywania problemu komiwojażera (Traveling Salesman Problem - TSP) w Javie.

## Struktura projektu

### Główne klasy:

1. **AntColonyTspSolver** - główna klasa implementująca algorytm ACO
   - Implementuje interfejs `TspSolver`
   - Zawiera wewnętrzną klasę `Ant` reprezentującą pojedynczą mrówkę
   
2. **Tour** - reprezentuje trasę przechodzącą przez wszystkie miasta
   
3. **City** - reprezentuje miasto (dziedziczy po `Point`)

## Parametry algorytmu

Algorytm wykorzystuje następujące parametry (zdefiniowane jako stałe):

- **INITIAL_PHEROMONE** = 1.0 - początkowa ilość feromonu na każdej krawędzi
- **PHEROMONE_EVAPORATION_COEFFICIENT** (ρ) = 0.1 - współczynnik parowania feromonów
- **PHEROMONE_WEIGHT** (α) = 1.0 - waga feromonu w funkcji prawdopodobieństwa
- **VISIBILITY_WEIGHT** (β) = 2.0 - waga heurystyki (widoczności) w funkcji prawdopodobieństwa
- **Q** = 100.0 - stała używana do obliczania ilości osadzanego feromonu
- **MAX_ITERATIONS** = 100 - maksymalna liczba iteracji algorytmu
- **numberOfAnts** = numberOfCities - liczba mrówek równa liczbie miast

## Zasada działania

### 1. Inicjalizacja

- **Macierz odległości (D)**: Obliczane są odległości euklidesowe między wszystkimi parami miast
- **Macierz feromonów (τ)**: Wszystkie krawędzie inicjalizowane są wartością `INITIAL_PHEROMONE`

### 2. Główna pętla algorytmu

W każdej iteracji (do `MAX_ITERATIONS`):

#### 2.1. Konstrukcja tras
Każda mrówka:
1. Startuje z losowego miasta
2. Buduje trasę wybierając kolejne nieodwiedzone miasta na podstawie prawdopodobieństwa:

```
P(i,j) = [τ(i,j)^α × η(i,j)^β] / Σ[τ(i,l)^α × η(i,l)^β]
```

gdzie:
- τ(i,j) - ilość feromonu na krawędzi (i,j)
- η(i,j) = 1/d(i,j) - heurystyka widoczności (odwrotność odległości)
- α - waga feromonu
- β - waga heurystyki

3. Wybór następnego miasta odbywa się **metodą ruletki** (roulette wheel selection)
4. Pamięta odwiedzone miasta, aby nie tworzyć cykli
5. Po zakończeniu trasy obliczana jest jej całkowita długość

#### 2.2. Aktualizacja najlepszego rozwiązania
Jeśli mrówka znalazła trasę krótszą niż dotychczasowe najlepsze rozwiązanie, rozwiązanie jest aktualizowane.

#### 2.3. Aktualizacja feromonów

**Parowanie (ewaporacja)**:
```
τ(i,j) ← (1 - ρ) × τ(i,j)
```

**Osadzanie**:
```
τ(i,j) ← τ(i,j) + Σ Δτ^k(i,j)
```

gdzie Δτ^k(i,j) = Q / L^k, a L^k to długość trasy mrówki k.

### 3. Zwrócenie wyniku

Po zakończeniu wszystkich iteracji zwracana jest najlepsza znaleziona trasa.

## Testy jednostkowe

### AntColonyTspSolverTest

Testy weryfikujące poprawność działania algorytmu:

1. **testSolveWithSmallSetOfCities** - test z 4 miastami w kwadracie
2. **testSolveWithThreeCities** - test z 3 miastami w linii prostej
3. **testSolveWithTriangleCities** - test z trójkątem prostokątnym (3,4,5)
4. **testAllCitiesVisited** - weryfikacja czy wszystkie miasta są odwiedzone dokładnie raz
5. **testConsistentResults** - sprawdzenie stabilności wyników
6. **testSingleCity** - przypadek brzegowy: jedno miasto
7. **testTwoCities** - przypadek brzegowy: dwa miasta
8. **testLargerSetOfCities** - test z 8 miastami w siatce
9. **testAlgorithmFindsReasonableSolution** - weryfikacja jakości rozwiązań

### TourTest

Testy klasy Tour:

1. **testTotalDistanceCalculation** - obliczanie całkowitej odległości
2. **testTourWithSingleCity** - trasa z jednym miastem
3. **testTourWithTwoCities** - trasa z dwoma miastami
4. **testTourWithSquareCities** - trasa przez wierzchołki kwadratu
5. **testGetCities** - pobieranie listy miast
6. **testToString** - format tekstowy trasy

## Uruchomienie

### Kompilacja i uruchomienie testów:
```bash
./gradlew test
```

### Uruchomienie aplikacji głównej:
```bash
./gradlew run
```

## Przykładowe wyjście

```
=== Rozpoczęcie Algorytmu Mrówkowego ===
Liczba miast: 10
Liczba mrówek: 10
Maksymalna liczba iteracji: 100
Parametry: α=1.0, β=2.0, ρ=0.1

Iteracja 10: Najlepsza długość trasy = 245.32
Iteracja 20: Najlepsza długość trasy = 238.17
Iteracja 30: Najlepsza długość trasy = 235.84
...
Iteracja 100: Najlepsza długość trasy = 230.45

=== Algorytm zakończony ===
Najlepsza długość trasy: 230.45
```

## Zastosowania

Algorytm mrówkowy może być użyty do:
- Rozwiązywania problemu komiwojażera (TSP)
- Optymalizacji tras logistycznych
- Planowania ścieżek w robotyce
- Routingu w sieciach komputerowych

## Możliwe usprawnienia

1. **Adaptacyjne parametry** - dynamiczne dostosowywanie α, β, ρ w trakcie działania
2. **Elitarny system mrówkowy** - dodatkowe wzmocnienie najlepszej trasy
3. **Local search** - lokalna optymalizacja znalezionych tras (np. 2-opt)
4. **Równoległa implementacja** - wykorzystanie wielowątkowości
5. **Różne strategie osadzania feromonów** - np. tylko najlepsza mrówka osadza feromon

