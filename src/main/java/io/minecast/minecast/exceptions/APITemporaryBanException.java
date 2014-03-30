package io.minecast.minecast.exceptions;

public class APITemporaryBanException extends Exception {

    public APITemporaryBanException() {
        super("API access has been temporarily banned. Most likely to throttle requests.");
    }
}
