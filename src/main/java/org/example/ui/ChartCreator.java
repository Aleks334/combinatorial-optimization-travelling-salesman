package org.example.ui;

import org.example.domain.model.Tour;
import org.jfree.chart.JFreeChart;

public interface ChartCreator {
    JFreeChart createChart(Tour tour);
}
