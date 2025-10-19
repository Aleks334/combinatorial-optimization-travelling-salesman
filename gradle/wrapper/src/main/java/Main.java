import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "dane.txt";

    public static void main(String[] args) {
        int[][] points = Generator();

        PrintWriter zapis = new PrintWriter(FILE_NAME);
        zapis.println(points.length);

        for (int i = 0; i < points.length; i++) {
            zapis.println((i+1) + " " points[i][0] + " " + points[i][1]);`
        }

        zapis.close();
        System.out.println("Zapisano dane do pliku dane.txt!");

        int Readpoints = ReadData();
        if (readPoints != null) {
            int numberofPoints = readPoints.length;
            System.out.println("\nOdczytano dane z pliku " + FILE_NAME + ":");
            System.out.println("Liczba odczytanych punktów: " + numberofPoints);


            for (int i = 0; i < numberofPoints; i++) {
                System.out.println((i+1)+ " "+ readPoints[i][0] + " " + readPoints[i][1]);
            }
        }

    }

    public static int[][] Generator() {
        Random rand = new Random();
        int Number_of_Points = rand.nextInt(50) + 1;
        int[][] points = new int[Number_of_Points][2];

        for (int i = 0; i < Number_of_Points; i++) {
            int x, y;
            boolean exists;
            do {
                exists = false;
                x = rand.nextInt(3000);
                y = rand.nextInt(3000);

                for (int j = 0; j < i; j++) {
                    if (points[j][0] == x && points[j][1] == y) {
                        exists = true;
                        break;
                    }
                }
            } while (exists);

            points[i][0] = x;
            points[i][1] = y;

        }
        return points;
    }
    public static int[][] ReadData() {
        try {
            Scanner odczyt = new Scanner(new File(FILE_NAME));

            if (!odczyt.hasNextInt()) {
                System.err.println("Błąd odczytu: Plik nie zawiera liczby punktów w pierwszej linii.");
                odczyt.close();
                return null;
            }

            // Odczyt  Liczby punktów (N)
            int numberOfPoints = odczyt.nextInt();
            odczyt.nextLine();

            int[][] points = new int[numberOfPoints][2];

            // Odczyt kolejnych N punktów
            for (int i = 0; i < numberOfPoints; i++) {

                if (odczyt.hasNextInt()) {
                    odczyt.nextInt(); // Ignoruj (i+1) z pliku
                }else{
                    System.err.println("Błąd odczytu: Brak indeksu punktu" + (i + 1));
                    odczyt.close();
                    return null;
                }

                if (odczyt.hasNextInt()) {
                    points[i][0] = odczyt.nextInt();
                } else {
                    System.err.println("Błąd odczytu: Brak danych dla współrzędnej X punktu " + (i+1));
                    odczyt.close();
                    return null;
                }

                if (odczyt.hasNextInt()) {
                    points[i][1] = odczyt.nextInt();
                } else {
                    System.err.println("Błąd odczytu: Brak danych dla współrzędnej Y punktu " + (i+1));
                    odczyt.close();
                    return null;
                }
            }

            odczyt.close();
            return points;

        } catch (FileNotFoundException e) {
            System.err.println("Błąd odczytu: Nie znaleziono pliku " + FILE_NAME);
            return null;
        }
    }
}
