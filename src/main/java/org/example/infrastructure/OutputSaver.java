package org.example.infrastructure;

import org.example.domain.model.Tour;
import org.example.ui.ChartCreator;
import org.example.ui.ConsoleUI;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.jfree.chart.ChartUtils.saveChartAsPNG;

public class OutputSaver {
    private static final String DEFAULT_BASE_DIR = "outputs";
    private final String baseDir;
    private final ChartCreator chartCreator;

    public OutputSaver() {
        this(DEFAULT_BASE_DIR, null);
    }

    public OutputSaver(String baseDir) {
        this(baseDir, null);
    }

    public OutputSaver(String baseDir, ChartCreator chartCreator) {
        this.baseDir = (baseDir == null || baseDir.isBlank()) ? DEFAULT_BASE_DIR : baseDir;
        this.chartCreator = chartCreator;
    }

    public File createRunDirectory(String algorithmName) throws IOException {
        String base = (algorithmName == null || algorithmName.isBlank()) ? "algorithm" : algorithmName;
        String safeName = base.replaceAll("[^a-zA-Z0-9_-]", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String dirName = safeName + "_" + timestamp;
        Path path = Paths.get(baseDir, dirName);
        Files.createDirectories(path);
        return path.toFile();
    }

    public File saveLogs(ConsoleUI ui, File directory) throws IOException {
        Path file = directory.toPath().resolve("logs.txt");
        try (FileWriter writer = new FileWriter(file.toFile())) {
            writer.write(ui.getLogs());
        }
        return file.toFile();
    }

    public File saveChart(Tour tour, File directory, String fileName) throws IOException {
        if (chartCreator == null) {
            throw new IllegalStateException("ChartCreator not provided");
        }
        Path file = directory.toPath().resolve(fileName);
        JFreeChart chart = chartCreator.createChart(tour);
        saveChartAsPNG(file.toFile(), chart, 800, 600);
        return file.toFile();
    }
}





