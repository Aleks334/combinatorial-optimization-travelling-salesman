# Przewodnik Konfiguracji Parametrów Algorytmu Mrówkowego

## Parametry Algorytmu

### 1. INITIAL_PHEROMONE (Początkowa ilość feromonu)
**Wartość domyślna**: 1.0

**Wpływ**:
- Określa początkową ilość feromonu na wszystkich krawędziach
- Wyższa wartość: początkowo wszystkie ścieżki są równie atrakcyjne
- Niższa wartość: szybsze zróżnicowanie ścieżek

**Rekomendacje**:
- Dla małych problemów (< 20 miast): 1.0 - 2.0
- Dla dużych problemów (> 50 miast): 0.1 - 1.0

### 2. PHEROMONE_EVAPORATION_COEFFICIENT (ρ - Rho)
**Wartość domyślna**: 0.1

**Wpływ**:
- Kontroluje szybkość parowania feromonu
- Wyższa wartość (np. 0.3): szybsze zapominanie, większa eksploracja
- Niższa wartość (np. 0.05): wolniejsze zapominanie, większa eksploatacja

**Rekomendacje**:
- Eksploracyjny: 0.2 - 0.5
- Zbalansowany: 0.1 - 0.2
- Eksploatacyjny: 0.01 - 0.1

### 3. PHEROMONE_WEIGHT (α - Alpha)
**Wartość domyślna**: 1.0

**Wpływ**:
- Waga wpływu feromonu na wybór ścieżki
- α = 0: ignorowanie feromonu (losowy wybór)
- Wysokie α (> 2): silne preferowanie ścieżek z dużą ilością feromonu

**Rekomendacje**:
- Standardowe: 0.5 - 2.0
- Dla problemów z dużą liczbą miast: 1.0 - 1.5

### 4. VISIBILITY_WEIGHT (β - Beta)
**Wartość domyślna**: 2.0

**Wpływ**:
- Waga wpływu heurystyki (odległości) na wybór ścieżki
- β = 0: ignorowanie odległości
- Wysokie β (> 3): silne preferowanie bliskich miast

**Rekomendacje**:
- Standardowe: 2.0 - 5.0
- Dla problemów euklidesowych: 2.0 - 3.0
- Dla problemów z różnymi metrykami: 1.0 - 2.0

**Równowaga α/β**:
- α < β: większy nacisk na heurystykę (lokalną optymalizację)
- α > β: większy nacisk na feromon (doświadczenie kolonii)
- α ≈ β: zbalansowane podejście

### 5. Q (Stała feromonu)
**Wartość domyślna**: 100.0

**Wpływ**:
- Określa ilość feromonu osadzanego przez mrówki
- Wyższa wartość: silniejsze wzmocnienie dobrych tras
- Niższa wartość: słabsze wzmocnienie

**Wzór**: Δτ = Q / długość_trasy

**Rekomendacje**:
- Dla małych odległości (< 100): Q = 100 - 1000
- Dla średnich odległości (100-1000): Q = 1000 - 10000
- Dla dużych odległości (> 1000): Q = 10000 - 100000

### 6. MAX_ITERATIONS (Liczba iteracji)
**Wartość domyślna**: 100

**Wpływ**:
- Określa ile razy kolonia mrówek buduje rozwiązania
- Więcej iteracji: lepsze rozwiązania, dłuższy czas
- Mniej iteracji: szybsze działanie, gorsze rozwiązania

**Rekomendacje**:
- Małe problemy (< 10 miast): 50 - 100
- Średnie problemy (10-30 miast): 100 - 300
- Duże problemy (> 30 miast): 300 - 1000

**Kryterium stopu**:
Można zastąpić stałą liczbę iteracji kryterium zbieżności:
- Brak poprawy przez N iteracji
- Osiągnięcie zadanej jakości rozwiązania

### 7. numberOfAnts (Liczba mrówek)
**Wartość domyślna**: numberOfCities

**Wpływ**:
- Określa ile mrówek buduje rozwiązania w każdej iteracji
- Więcej mrówek: lepsza eksploracja, dłuższy czas
- Mniej mrówek: szybsze działanie, gorsza eksploracja

**Rekomendacje**:
- Standardowe: n (liczba miast)
- Szybkie: 0.5n - 0.75n
- Dokładne: 1.5n - 2n

## Przykładowe Konfiguracje

### Konfiguracja 1: Szybka (dla małych problemów)
```java
INITIAL_PHEROMONE = 1.0
PHEROMONE_EVAPORATION_COEFFICIENT = 0.2
PHEROMONE_WEIGHT = 1.0
VISIBILITY_WEIGHT = 3.0
Q = 100.0
MAX_ITERATIONS = 50
numberOfAnts = numberOfCities
```

### Konfiguracja 2: Zbalansowana (domyślna)
```java
INITIAL_PHEROMONE = 1.0
PHEROMONE_EVAPORATION_COEFFICIENT = 0.1
PHEROMONE_WEIGHT = 1.0
VISIBILITY_WEIGHT = 2.0
Q = 100.0
MAX_ITERATIONS = 100
numberOfAnts = numberOfCities
```

### Konfiguracja 3: Dokładna (dla trudnych problemów)
```java
INITIAL_PHEROMONE = 0.5
PHEROMONE_EVAPORATION_COEFFICIENT = 0.05
PHEROMONE_WEIGHT = 1.5
VISIBILITY_WEIGHT = 2.5
Q = 1000.0
MAX_ITERATIONS = 300
numberOfAnts = numberOfCities * 1.5
```

### Konfiguracja 4: Eksploracyjna
```java
INITIAL_PHEROMONE = 2.0
PHEROMONE_EVAPORATION_COEFFICIENT = 0.3
PHEROMONE_WEIGHT = 0.5
VISIBILITY_WEIGHT = 2.0
Q = 100.0
MAX_ITERATIONS = 150
numberOfAnts = numberOfCities * 2
```

## Jak Dostroić Parametry

### 1. Analiza Zbieżności
Obserwuj jak zmienia się najlepsza znaleziona trasa w kolejnych iteracjach:
- **Zbyt szybka zbieżność**: Zwiększ ρ, zmniejsz α
- **Zbyt wolna zbieżność**: Zmniejsz ρ, zwiększ α
- **Utknięcie w optimum lokalnym**: Zwiększ ρ i β

### 2. Analiza Jakości Rozwiązań
Porównaj znalezione rozwiązania z znanym optimum lub upper bound:
- **Gorsze rozwiązania**: Zwiększ MAX_ITERATIONS, zwiększ numberOfAnts
- **Brak poprawy**: Zmień równowagę α/β
- **Duża zmienność**: Zwiększ liczbę iteracji

### 3. Analiza Czasu Wykonania
Jeśli algorytm działa zbyt wolno:
- Zmniejsz MAX_ITERATIONS
- Zmniejsz numberOfAnts
- Zwiększ ρ (szybsza zbieżność)

## Wskazówki Praktyczne

1. **Zacznij od domyślnych wartości** i modyfikuj po jednym parametrze naraz
2. **Użyj logarytmicznej skali** do testowania Q (100, 1000, 10000)
3. **Testuj na małych instancjach** przed uruchomieniem na dużych
4. **Zapisuj wyniki** każdej konfiguracji do późniejszej analizy
5. **Używaj stałego seeda** Random dla porównywalności wyników

## Diagnostyka Problemów

### Problem: Algorytm zawsze zwraca tę samą trasę
**Rozwiązanie**: 
- Zwiększ PHEROMONE_EVAPORATION_COEFFICIENT
- Zmniejsz PHEROMONE_WEIGHT
- Zwiększ losowość w selekcji

### Problem: Rozwiązania są bardzo zmienne
**Rozwiązanie**:
- Zwiększ MAX_ITERATIONS
- Zwiększ numberOfAnts
- Zmniejsz PHEROMONE_EVAPORATION_COEFFICIENT

### Problem: Algorytm nie znajduje dobrych rozwiązań
**Rozwiązanie**:
- Sprawdź równowagę α/β (spróbuj β > α)
- Zwiększ MAX_ITERATIONS
- Zwiększ Q jeśli dystanse są duże

## Referencje

Klasyczne wartości z literatury (Dorigo & Stützle):
- α = 1, β = 2-5, ρ = 0.1-0.3
- m (liczba mrówek) = n (liczba miast)
- Liczba iteracji: 100-1000 w zależności od rozmiaru problemu

