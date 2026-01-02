package org.example.ui.chart;

import org.example.domain.model.City;
import org.example.domain.model.Tour;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartManager {

    public JFreeChart createChart(Tour tour) {
        XYSeriesCollection dataset = createDataset(tour);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "TSP Tour: " + String.format("%.2f", tour.getTotalDistance()),
                "X", "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        customizePlot(chart);
        return chart;
    }

    public void display(JFreeChart chart, String title) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            ChartPanel panel = new ChartPanel(chart);
            panel.setPreferredSize(ChartConfig.WINDOW_SIZE);
            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private XYSeriesCollection createDataset(Tour tour) {
        List<City> cityList = tour.getCities();

        XYSeries routeSeries = new XYSeries("Route", false);
        XYSeries citySeries = new XYSeries("Cities", false);

        for (City city : cityList) {
            routeSeries.add(city.getX(), city.getY());
            citySeries.add(city.getX(), city.getY());
        }
        if (!cityList.isEmpty()) {
            City firstCity = cityList.getFirst();
            routeSeries.add(firstCity.getX(), firstCity.getY());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(routeSeries);
        dataset.addSeries(citySeries);

        return dataset;
    }

    private void customizePlot(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer routeRenderer = new XYLineAndShapeRenderer();
        routeRenderer.setSeriesPaint(0, ChartConfig.ROUTE_COLOR);
        routeRenderer.setSeriesStroke(0, new BasicStroke(ChartConfig.ROUTE_LINE_WIDTH));
        routeRenderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(0, routeRenderer);

        XYLineAndShapeRenderer cityRenderer = new XYLineAndShapeRenderer();
        cityRenderer.setSeriesPaint(1, ChartConfig.CITY_COLOR);
        cityRenderer.setSeriesShapesVisible(1, true);
        cityRenderer.setSeriesLinesVisible(1, false);
        cityRenderer.setSeriesShape(1, new java.awt.geom.Ellipse2D.Double(-5, -5, 10, 10));
        plot.setRenderer(1, cityRenderer);

        plot.setBackgroundPaint(ChartConfig.BACKGROUND_COLOR);
        plot.setDomainGridlinePaint(ChartConfig.GRID_COLOR);
        plot.setRangeGridlinePaint(ChartConfig.GRID_COLOR);
    }
}