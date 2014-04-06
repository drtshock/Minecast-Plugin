package io.minecast.plugin.exceptions;

public class DuplicateKeyException extends Exception {

    public DuplicateKeyException() {
        super("Multiple server keys have collided... Please contact support@minecast.io to get your server key replaced.");
    }
}
