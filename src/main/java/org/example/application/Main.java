package org.example.application;

import org.example.application.port.out.PointRepository;
import org.example.application.port.out.SolutionArchiver;
import org.example.application.service.DataService;
import org.example.application.service.TspSolverService;
import org.example.infrastructure.file.FilePointRepository;
import org.example.infrastructure.file.FileSolutionArchiver;
import org.example.ui.chart.ChartManager;
import org.example.ui.console.ConsoleAdapter;

public class Main {
    public static void main(String[] args) {
        PointRepository pointRepo = new FilePointRepository("data.txt");
        SolutionArchiver archiver = new FileSolutionArchiver("outputs");

        ConsoleAdapter console = new ConsoleAdapter();
        ChartManager chartManager = new ChartManager();

        DataService dataService = new DataService(pointRepo);
        TspSolverService solverService = new TspSolverService();

        TspApplication app = new TspApplication(
                console, console, dataService, solverService, archiver, chartManager
        );
        app.run();
    }
}