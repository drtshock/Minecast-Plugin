package io.minecast.minecast.exceptions;

public class DuplicateTweetException extends Exception {

    public DuplicateTweetException() {
        super("Twitter blocked this tweet because it is too similar to another.");
    }
}
