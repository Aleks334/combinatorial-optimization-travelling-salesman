package org.example.infrastructure.output;

import org.example.domain.model.City;
import org.example.domain.model.Tour;
import org.example.ui.chart.DefaultChartCreator;
import org.example.ui.console.ConsoleUIFacade;
import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class FileOutputSaverTest {

    private File tempDir;
    private File runDir;

    @AfterEach
    void tearDown() {
        deleteRecursively(runDir);
        deleteRecursively(tempDir);
    }

    @Test
    void shouldCreateRunDirectoryAndSaveFiles() throws Exception {
        // given
        tempDir = Files.createTempDirectory("outputs_test").toFile();
        FileOutputSaver saver = new FileOutputSaver(tempDir.getAbsolutePath());

        ConsoleUIFacade ui = new ConsoleUIFacade();
        ui.output().println("test log line");

        City c1 = new City("A", 0, 0);
        City c2 = new City("B", 1, 0);
        Tour tour = new Tour(Arrays.asList(c1, c2));

        DefaultChartCreator chartCreator = new DefaultChartCreator();
        JFreeChart chart = chartCreator.createChart(tour);

        // when
        runDir = saver.createRunDirectory("TEST_ALGO");
        File logFile = saver.saveLogs(ui.logManager(), runDir);
        File chartFile = saver.saveChart(chart, runDir, "chart.png");

        // then
        assertThat(runDir).exists();
        assertThat(logFile).exists().isNotEmpty();
        assertThat(chartFile).exists().isNotEmpty();
    }

    private void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                deleteRecursively(f);
            }
        }
        file.delete();
    }
}
