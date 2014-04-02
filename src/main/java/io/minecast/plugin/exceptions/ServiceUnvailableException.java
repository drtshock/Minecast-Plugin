package io.minecast.plugin.exceptions;

public class ServiceUnvailableException extends Exception {

    public ServiceUnvailableException() {
        super("Could not connect to Minecast.");
    }
}
