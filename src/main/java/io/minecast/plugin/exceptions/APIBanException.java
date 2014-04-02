package io.minecast.plugin.exceptions;

public class APIBanException extends Exception {

    public APIBanException() {
        super("Access to the API has been banned. Please contact support@minecast.io");
    }
}
