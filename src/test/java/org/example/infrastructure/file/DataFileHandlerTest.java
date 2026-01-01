package org.example.infrastructure.file;

import org.example.domain.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DataFileHandlerTest {

    @TempDir
    Path tempDir;

    @Nested
    class ReadWriteOperations {

        private DataFileHandler handler;
        private File testFile;

        @BeforeEach
        void setUp() {
            testFile = tempDir.resolve("test.txt").toFile();
            handler = new DataFileHandler(testFile.getAbsolutePath());
        }

        @Test
        void shouldWriteAndReadPointsWithCorrectData() throws FileNotFoundException {
            // given
            List<Point> originalPoints = Arrays.asList(
                new Point(10, 20),
                new Point(30, 40),
                new Point(50, 60)
            );

            // when
            handler.writePoints(originalPoints);
            List<Point> readPoints = handler.readPoints();

            // then
            assertThat(readPoints).hasSize(3);
            assertPointEquals(originalPoints.get(0), readPoints.get(0));
            assertPointEquals(originalPoints.get(1), readPoints.get(1));
            assertPointEquals(originalPoints.get(2), readPoints.get(2));
        }

        @ParameterizedTest(name = "Test with {0} points")
        @ValueSource(ints = {1, 5, 10, 50, 100})
        void shouldHandleVariousPointCounts(int count) throws FileNotFoundException {
            // given
            List<Point> points = generateTestPoints(count);

            // when
            handler.writePoints(points);
            List<Point> readPoints = handler.readPoints();

            // then
            assertThat(readPoints).hasSize(count);
        }
    }

    @Nested
    class ErrorHandling {

        @Test
        void shouldReturnNullForNonExistentFile() {
            // given
            String nonExistentFilePath = "nonexistent.txt";
            DataFileHandler handler = new DataFileHandler(nonExistentFilePath);

            // when
            List<Point> points = handler.readPoints();

            // then
            assertThat(points).isNull();
        }

        @Test
        void shouldReturnNullForInvalidFileFormat() throws Exception {
            // given
            File tempFile = tempDir.resolve("invalid.txt").toFile();
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.println("not a number");
            }
            DataFileHandler handler = new DataFileHandler(tempFile.getAbsolutePath());

            // when
            List<Point> points = handler.readPoints();

            // then
            assertThat(points).isNull();
        }

        @Test
        void shouldReturnNullForEmptyFile() throws Exception {
            // given
            File emptyFile = tempDir.resolve("empty.txt").toFile();
            assertThat(emptyFile.createNewFile()).isTrue();
            DataFileHandler handler = new DataFileHandler(emptyFile.getAbsolutePath());

            // when
            List<Point> points = handler.readPoints();

            // then
            assertThat(points).isNull();
        }

        @Test
        void shouldReturnNullForFileWithOnlyCount() throws Exception {
            // given
            File tempFile = tempDir.resolve("only_count.txt").toFile();
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.println("5");
            }
            DataFileHandler handler = new DataFileHandler(tempFile.getAbsolutePath());

            // when
            List<Point> points = handler.readPoints();

            // then
            assertThat(points).isNull();
        }
    }

    @Nested
    class DataIntegrity {

        @Test
        void shouldPreserveExactCoordinates() throws FileNotFoundException {
            // given
            File testFile = tempDir.resolve("coords.txt").toFile();
            DataFileHandler handler = new DataFileHandler(testFile.getAbsolutePath());
            List<Point> originalPoints = Arrays.asList(
                new Point(0, 0),
                new Point(Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1),
                new Point(1, 1)
            );

            // when
            handler.writePoints(originalPoints);
            List<Point> readPoints = handler.readPoints();

            // then
            assertThat(readPoints).isNotNull();
            for (int i = 0; i < originalPoints.size(); i++) {
                assertPointEquals(originalPoints.get(i), readPoints.get(i));
            }
        }
    }

    private List<Point> generateTestPoints(int count) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            points.add(new Point(i * 10, i * 20));
        }
        return points;
    }

    private void assertPointEquals(Point expected, Point actual) {
        assertThat(actual.getX()).isEqualTo(expected.getX());
        assertThat(actual.getY()).isEqualTo(expected.getY());
    }
}
