package org.example.infrastructure.output;

import org.example.application.port.LogManager;
import org.example.application.port.output.OutputSaverPort;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.jfree.chart.ChartUtils.saveChartAsPNG;

public class FileOutputSaver implements OutputSaverPort {
    private static final String DEFAULT_BASE_DIR = "outputs";
    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;

    private final String baseDir;

    public FileOutputSaver() {
        this(DEFAULT_BASE_DIR);
    }

    public FileOutputSaver(String baseDir) {
        Objects.requireNonNull(baseDir, "Base directory path cannot be null");
        this.baseDir = baseDir.isBlank() ? DEFAULT_BASE_DIR : baseDir;
    }

    @Override
    public File createRunDirectory(String algorithmName) throws IOException {
        String base = (algorithmName == null || algorithmName.isBlank())
            ? "algorithm"
            : algorithmName;
        String safeName = base.replaceAll("[^a-zA-Z0-9_-]", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String dirName = safeName + "_" + timestamp;

        Path path = Paths.get(baseDir, dirName);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new IOException(
                    String.format("Failed to create directory: %s", path), e
            );
        }

        return path.toFile();
    }

    @Override
    public File saveLogs(LogManager logManager, File directory) throws IOException {
        Objects.requireNonNull(logManager, "LogManager cannot be null");
        validateDirectory(directory);

        Path file = directory.toPath().resolve("logs.txt");
        try (FileWriter writer = new FileWriter(file.toFile())) {
            writer.write(logManager.getLogs());
        } catch (IOException e) {
            throw new IOException("Failed to save logs to file", e);
        }
        return file.toFile();
    }

    @Override
    public File saveChart(JFreeChart chart, File directory, String fileName) throws IOException {
        Objects.requireNonNull(chart, "Chart cannot be null");
        validateDirectory(directory);
        validateFileName(fileName);

        Path file = directory.toPath().resolve(sanitizeFileName(fileName));
        try {
            Files.createDirectories(file.getParent());
            saveChartAsPNG(file.toFile(), chart, CHART_WIDTH, CHART_HEIGHT);
        } catch (IOException e) {
            throw new IOException(
                    String.format("Failed to save chart '%s'", fileName), e
            );
        }
        return file.toFile();
    }

    private void validateDirectory(File directory) throws IOException {
        if (directory == null) {
            throw new IOException("Directory cannot be null");
        }
        if (!directory.isDirectory()) {
            throw new IOException("Invalid directory: " + directory.getAbsolutePath());
        }
    }

    private void validateFileName(String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            throw new IOException("File name cannot be empty");
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
