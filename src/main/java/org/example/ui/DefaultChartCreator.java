package org.example.ui;

import org.example.domain.model.City;
import org.example.domain.model.Tour;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;

public class DefaultChartCreator implements ChartCreator {

    @Override
    public JFreeChart createChart(Tour tour) {
        XYSeriesCollection dataset = createDataset(tour);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "TSP Solution - Tour length: " + String.format("%.2f", tour.getTotalDistance()),
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer routeRenderer = new XYLineAndShapeRenderer();
        routeRenderer.setSeriesPaint(0, Color.BLUE);
        routeRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        routeRenderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(0, routeRenderer);

        XYLineAndShapeRenderer cityRenderer = new XYLineAndShapeRenderer();
        cityRenderer.setSeriesPaint(1, Color.RED);
        cityRenderer.setSeriesShapesVisible(1, true);
        cityRenderer.setSeriesLinesVisible(1, false);
        cityRenderer.setSeriesShape(1, new java.awt.geom.Ellipse2D.Double(-5, -5, 10, 10));
        plot.setRenderer(1, cityRenderer);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        return chart;
    }

    private XYSeriesCollection createDataset(Tour tour) {
        List<City> cities = tour.getCities();

        XYSeries routeSeries = new XYSeries("Route", false);
        XYSeries citySeries = new XYSeries("Cities", false);

        for (City city : cities) {
            routeSeries.add(city.getX(), city.getY());
            citySeries.add(city.getX(), city.getY());
        }

        if (!cities.isEmpty()) {
            City firstCity = cities.getFirst();
            routeSeries.add(firstCity.getX(), firstCity.getY());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(routeSeries);
        dataset.addSeries(citySeries);

        return dataset;
    }
}
