package org.example.application.port.out;

import org.example.domain.model.Point;
import java.util.List;

public interface PointRepository {
    List<Point> loadPoints() throws Exception;
    void savePoints(List<Point> points) throws Exception;
}