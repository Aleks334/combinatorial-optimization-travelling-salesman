package org.example;

import org.example.antColony.AntColonyTspSolver;
import org.example.tsp.City;
import org.example.tsp.GreedyTspSolver;
import org.example.tsp.Tour;
import org.example.tsp.TspSolver;
import org.example.visualization.TspVisualizer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "dane.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcome();

        List<Point> points = null;
        String selectedAlgorithm = "ACO";
        boolean exitProgram = false;

        while (!exitProgram) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    points = loadPointsFromFile();
                    break;
                case 2:
                    points = generatePoints();
                    break;
                case 3:
                    points = generateAndSavePoints();
                    break;
                case 4:
                    selectedAlgorithm = chooseAlgorithm();
                    break;
                case 5:
                    if (points != null && !points.isEmpty()) {
                        solveTsp(points, selectedAlgorithm);
                    } else {
                        System.out.println("\n⚠ Najpierw wczytaj lub wygeneruj dane (opcja 1, 2 lub 3)");
                    }
                    break;
                case 6:
                    exitProgram = true;
                    System.out.println("\nDo widzenia!");
                    break;
                default:
                    System.out.println("\n⚠ Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }

        scanner.close();
    }

    private static void displayWelcome() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         Problem Komiwojażera (TSP) - Solver            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }

    private static void displayMenu() {
        System.out.println("\n┌────────────────── MENU ──────────────────┐");
        System.out.println("│ 1. Wczytaj dane z pliku (" + FILE_NAME + ")       │");
        System.out.println("│ 2. Wygeneruj nowe dane losowe            │");
        System.out.println("│ 3. Wygeneruj i zapisz do pliku           │");
        System.out.println("│ 4. Wybierz algorytm                      │");
        System.out.println("│ 5. Rozwiąż TSP                           │");
        System.out.println("│ 6. Wyjście                               │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("Wybierz opcję (1-6): ");
    }

    private static int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // clear buffer
            return -1;
        }
    }

    private static List<Point> loadPointsFromFile() {
        DataFileHandler fileHandler = new DataFileHandler(FILE_NAME);
        List<Point> points = fileHandler.readPoints();

        if (points != null && !points.isEmpty()) {
            System.out.println("\n✓ Wczytano " + points.size() + " punktów z pliku " + FILE_NAME);
            displayPoints(points);
            return points;
        } else {
            System.out.println("\n✗ Nie udało się wczytać danych z pliku " + FILE_NAME);
            return null;
        }
    }

    private static List<Point> generatePoints() {
        System.out.print("\nPodaj liczbę miast do wygenerowania (lub 0 dla losowej 1-50): ");
        int numberOfCities = getUserChoice();

        PointGenerator generator = new PointGenerator();
        List<Point> points;

        if (numberOfCities > 0) {
            points = generator.generate(numberOfCities);
            System.out.println("\n✓ Wygenerowano " + numberOfCities + " miast");
        } else {
            points = generator.generate();
            System.out.println("\n✓ Wygenerowano losowo " + points.size() + " miast");
        }

        displayPoints(points);
        return points;
    }

    private static List<Point> generateAndSavePoints() {
        List<Point> points = generatePoints();

        if (points != null && !points.isEmpty()) {
            DataFileHandler fileHandler = new DataFileHandler(FILE_NAME);
            try {
                fileHandler.writePoints(points);
                System.out.println("✓ Zapisano dane do pliku " + FILE_NAME);
            } catch (FileNotFoundException e) {
                System.err.println("✗ Błąd zapisu: Nie można utworzyć pliku " + FILE_NAME);
            }
        }

        return points;
    }

    private static void displayPoints(List<Point> points) {
        if (points == null || points.isEmpty()) return;

        System.out.println("\nWspółrzędne miast:");
        int limit = Math.min(points.size(), 10);
        for (int i = 0; i < limit; i++) {
            Point point = points.get(i);
            System.out.printf("  %2d. (%d, %d)%n", i + 1, point.getX(), point.getY());
        }

        if (points.size() > 10) {
            System.out.println("  ... (i " + (points.size() - 10) + " więcej)");
        }
    }

    private static String chooseAlgorithm() {
        System.out.println("\n┌─────── WYBÓR ALGORYTMU ───────┐");
        System.out.println("│ 1. Algorytm Zachłanny         │");
        System.out.println("│ 2. Algorytm Mrówkowy          │");
        System.out.println("└───────────────────────────────┘");
        System.out.print("Wybierz algorytm (1-2): ");

        int algorithmChoice = getUserChoice();

        switch (algorithmChoice) {
            case 1:
                System.out.println("✓ Wybrano: Algorytm Zachłanny (Greedy)");
                return "GREEDY";
            case 2:
                System.out.println("✓ Wybrano: Algorytm Mrówkowy (ACO)");
                return "ACO";
            default:
                System.out.println("⚠ Nieprawidłowy wybór. Ustawiono domyślnie: Algorytm Mrówkowy (ACO)");
                return "ACO";
        }
    }

    private static void solveTsp(List<Point> points, String algorithm) {
        List<City> cities = convertPointsToCities(points);

        String algorithmName;
        TspSolver solver;

        if ("GREEDY".equals(algorithm)) {
            algorithmName = "Algorytm Zachłanny (Greedy)";
            solver = new GreedyTspSolver();
        } else {
            algorithmName = "Algorytm Mrówkowy (ACO)";
            solver = new AntColonyTspSolver(points);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("URUCHAMIANIE: " + algorithmName);
        System.out.println("=".repeat(60));

        long startTime = System.currentTimeMillis();
        Tour tour = solver.solve(cities);
        long endTime = System.currentTimeMillis();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("WYNIK KOŃCOWY - " + algorithmName);
        System.out.println("=".repeat(60));
        System.out.println(tour);
        System.out.printf("Całkowity czas wykonania: %.3f sekund%n", (endTime - startTime) / 1000.0);
        System.out.println("=".repeat(60));

        TspVisualizer.displayTour(tour, "Wizualizacja TSP - " + algorithmName);

        System.out.print("\nNaciśnij Enter aby powrócić do menu...");
        scanner.nextLine(); // clear buffer
        try {
            scanner.nextLine();
        } catch (Exception ignored) {}
    }

    private static List<City> convertPointsToCities(List<Point> points) {
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            String cityName = "City" + (i + 1);
            cities.add(new City(cityName, point.getX(), point.getY()));
        }
        return cities;
    }
}
