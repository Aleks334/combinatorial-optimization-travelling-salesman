package org.example.infrastructure.file;

import org.example.application.port.out.SolutionArchiver;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileSolutionArchiver implements SolutionArchiver {
    private final String baseDir;

    public FileSolutionArchiver(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public File createRunDirectory(String algoName) throws Exception {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String dirName = algoName.replace(" ", "_") + "_" + timestamp;
        Path path = Paths.get(baseDir, dirName);
        Files.createDirectories(path);
        return path.toFile();
    }

    @Override
    public void saveLogs(String logs, File directory) throws Exception {
        try (FileWriter writer = new FileWriter(new File(directory, "logs.txt"))) {
            writer.write(logs);
        }
    }

    @Override
    public void saveChart(JFreeChart chart, File directory) throws Exception {
        ChartUtils.saveChartAsPNG(new File(directory, "chart.png"), chart, 800, 600);
    }
}