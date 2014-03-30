package io.minecast.minecast.exceptions;

public class BadAPIRequestException extends Exception {

    public BadAPIRequestException() {
        super("Looks like you formatted your API request incorrectly.");
    }
}
