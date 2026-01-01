package org.example.ui.console;

import org.example.application.port.ConsoleInput;
import org.example.application.port.ConsoleOutput;
import org.example.application.port.LogManager;
import org.example.application.port.MessagePresenter;

public class ConsoleUIFacade {
    private final ConsoleUI consoleUI;

    public ConsoleUIFacade(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    public ConsoleUIFacade() {
       this(new ConsoleUI());
    }

    public ConsoleInput input() {
        return consoleUI;
    }

    public ConsoleOutput output() {
        return consoleUI;
    }

    public MessagePresenter presenter() {
        return consoleUI;
    }

    public LogManager logManager() {
        return consoleUI;
    }

    public void close() {
        consoleUI.close();
    }
}
