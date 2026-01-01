package org.example.application.port.output;

import java.io.File;
import java.io.IOException;
import org.jfree.chart.JFreeChart;
import org.example.application.port.LogManager;

public interface OutputSaverPort {
    File createRunDirectory(String algorithmName) throws IOException;
    File saveLogs(LogManager logManager, File directory) throws IOException;
    File saveChart(JFreeChart chart, File directory, String fileName) throws IOException;
}

