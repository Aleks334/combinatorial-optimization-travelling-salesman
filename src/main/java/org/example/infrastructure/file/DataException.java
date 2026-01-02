package org.example.infrastructure.file;

public class DataException extends Exception {
    public DataException(String message) { super(message); }
    public DataException(String message, Throwable cause) { super(message, cause); }
}