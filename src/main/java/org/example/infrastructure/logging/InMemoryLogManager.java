package org.example.infrastructure.logging;

public class InMemoryLogManager {
    private final StringBuilder buffer;

    public InMemoryLogManager() {
        this.buffer = new StringBuilder();
    }

    public void append(String message) {
        buffer.append(message);
    }

    public void appendLine(String message) {
        buffer.append(message).append(System.lineSeparator());
    }

    public String getLogs() {
        return buffer.toString();
    }

    public void clear() {
        buffer.setLength(0);
    }
}