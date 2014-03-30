package io.minecast.minecast.exceptions;

public class BadServerKeyException extends Exception {

    public BadServerKeyException() {
        super("Server key is not valid.");
    }
}
