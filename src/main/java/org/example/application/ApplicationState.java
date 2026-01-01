package org.example.application;

import org.example.domain.algorithm.Algorithm;
import org.example.domain.model.Point;
import org.example.domain.model.Tour;

import java.util.List;

public class ApplicationState {
    private List<Point> points;
    private Algorithm selectedAlgorithm;
    private boolean shouldExit;
    private Tour lastTour;
    private String lastAlgorithmName;
    private boolean lastSaved;

    public ApplicationState() {
        this.points = null;
        this.selectedAlgorithm = Algorithm.ANT_COLONY;
        this.shouldExit = false;
        this.lastTour = null;
        this.lastAlgorithmName = null;
        this.lastSaved = false;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public boolean hasPoints() {
        return points != null && !points.isEmpty();
    }

    public Algorithm getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(Algorithm selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public boolean shouldExit() {
        return shouldExit;
    }

    public void exit() {
        this.shouldExit = true;
    }

    public Tour getLastTour() {
        return lastTour;
    }

    public void setLastTour(Tour lastTour) {
        this.lastTour = lastTour;
    }

    public String getLastAlgorithmName() {
        return lastAlgorithmName;
    }

    public void setLastAlgorithmName(String lastAlgorithmName) {
        this.lastAlgorithmName = lastAlgorithmName;
    }

    public boolean isLastSaved() {
        return lastSaved;
    }

    public void setLastSaved(boolean lastSaved) {
        this.lastSaved = lastSaved;
    }
}
