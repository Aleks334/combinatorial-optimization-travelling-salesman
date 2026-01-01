package org.example.ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class DefaultChartDisplayer implements ChartDisplayer {
    @Override
    public void display(JFreeChart chart, String title) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 600));
            frame.setContentPane(chartPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
