package io.minecast.minecast.exceptions;

public class APILockoutException extends Exception {

    public APILockoutException() {
        super("Your account has been locked out of the API. Email support@minecast.io for further info.");
    }
}
