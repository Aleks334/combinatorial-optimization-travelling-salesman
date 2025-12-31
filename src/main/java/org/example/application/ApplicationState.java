package org.example.application;

import org.example.domain.algorithm.Algorithm;
import org.example.domain.model.Point;

import java.util.List;

public class ApplicationState {
    private List<Point> points;
    private Algorithm selectedAlgorithm;
    private boolean shouldExit;

    public ApplicationState() {
        this.points = null;
        this.selectedAlgorithm = Algorithm.ANT_COLONY;
        this.shouldExit = false;
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
}

