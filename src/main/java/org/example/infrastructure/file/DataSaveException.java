package org.example.infrastructure.file;

public class DataSaveException extends Exception {
    public DataSaveException(String message) {
        super(message);
    }

    public DataSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

