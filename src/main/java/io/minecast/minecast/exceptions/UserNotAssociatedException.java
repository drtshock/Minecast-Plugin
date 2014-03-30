package io.minecast.minecast.exceptions;

public class UserNotAssociatedException extends Exception {

    public UserNotAssociatedException() {
        super("The user does not have an associated Twitter account.");
    }
}
