package io.minecast.plugin.exceptions;

public class ServerNotTrustedException extends Exception {

    public ServerNotTrustedException() {
        super("This server attempting to query the API is not trusted.");
    }
}
