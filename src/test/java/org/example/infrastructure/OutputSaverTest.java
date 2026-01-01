package org.example.infrastructure;

import org.example.domain.model.City;
import org.example.domain.model.Tour;
import org.example.ui.ConsoleUI;
import org.example.ui.DefaultChartCreator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputSaverTest {

    @Test
    public void shouldCreateRunDirectoryAndSaveFiles() throws Exception {
        // given
        File tempDir = Files.createTempDirectory("outputs_test").toFile();
        OutputSaver saver = new OutputSaver(tempDir.getAbsolutePath(), new DefaultChartCreator());
        ConsoleUI ui = new ConsoleUI();
        ui.println("test log line");

        City c1 = new City("A", 0, 0);
        City c2 = new City("B", 1, 0);
        Tour tour = new Tour(Arrays.asList(c1, c2));

        // when
        File runDir = saver.createRunDirectory("TEST_ALGO");
        File logFile = saver.saveLogs(ui, runDir);
        File chartFile = saver.saveChart(tour, runDir, "chart.png");

        // then
        assertThat(runDir.exists()).isTrue();
        assertThat(logFile.exists()).isTrue();
        assertThat(chartFile.exists()).isTrue();
        assertThat(logFile.length()).isGreaterThan(0);
        assertThat(chartFile.length()).isGreaterThan(0);

        // cleanup
        for (File f : Objects.requireNonNull(runDir.listFiles())) {
            f.delete();
        }
        runDir.delete();
        tempDir.delete();
    }
}
