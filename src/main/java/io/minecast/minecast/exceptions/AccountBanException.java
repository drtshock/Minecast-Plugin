package io.minecast.minecast.exceptions;

public class AccountBanException extends Exception {

    public AccountBanException() {
        super("This account has been banned. Please contact support@minecast.io if you believe it is a mistake.");
    }
}
