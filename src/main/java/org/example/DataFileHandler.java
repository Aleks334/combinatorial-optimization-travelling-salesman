package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataFileHandler {
    private final String fileName;

    public DataFileHandler(String fileName) {
        this.fileName = fileName;
    }

    public void writePoints(List<Point> points) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println(points.size());

            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                writer.println((i + 1) + " " + point.getX() + " " + point.getY());
            }

            System.out.println("Zapisano dane do pliku " + fileName + "!");
        }
    }

    public List<Point> readPoints() {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            if (!scanner.hasNextInt()) {
                System.err.println("Błąd odczytu: Plik nie zawiera liczby punktów w pierwszej linii.");
                return null;
            }

            int numberOfPoints = scanner.nextInt();
            scanner.nextLine();

            List<Point> points = new ArrayList<>();

            for (int i = 0; i < numberOfPoints; i++) {
                if (scanner.hasNextInt()) {
                    scanner.nextInt(); // Ignoruj (i+1) z pliku
                } else {
                    System.err.println("Błąd odczytu: Brak indeksu punktu " + (i + 1));
                    return null;
                }

                if (!scanner.hasNextInt()) {
                    System.err.println("Błąd odczytu: Brak danych dla współrzędnej X punktu " + (i + 1));
                    return null;
                }
                int x = scanner.nextInt();

                if (!scanner.hasNextInt()) {
                    System.err.println("Błąd odczytu: Brak danych dla współrzędnej Y punktu " + (i + 1));
                    return null;
                }
                int y = scanner.nextInt();

                points.add(new Point(x, y));
            }

            return points;

        } catch (FileNotFoundException e) {
            System.err.println("Błąd odczytu: Nie znaleziono pliku " + fileName);
            return null;
        }
    }
}
