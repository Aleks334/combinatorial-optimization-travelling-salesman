package org.example.application.port.out;

import org.jfree.chart.JFreeChart;
import java.io.File;

public interface SolutionArchiver {
    File createRunDirectory(String algorithmName) throws Exception;
    void saveLogs(String logs, File directory) throws Exception;
    void saveChart(JFreeChart chart, File directory) throws Exception;
}