package io.minecast.minecast.exceptions;

public class NetworkBanException extends Exception {

    public NetworkBanException() {
        super("The server attempting to query the Minecast API has been banned.");
    }
}
