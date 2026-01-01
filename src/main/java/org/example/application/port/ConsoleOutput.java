package org.example.application.port;

public interface ConsoleOutput {
    void print(String message);
    void println(String message);
    void printf(String format, Object... args);
}

