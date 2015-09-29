package com.sem.ssm2.server.database;

/**
 * When communicating with the server, a ResponseHandler is supplied. This is important since different connections
 * require either the connection to be blocking (local) or non-block (remote).
 */
public interface ResponseHandler {
    /**
     * Method invoked when a reply is received from the server.
     *
     * @param response The Response given by the server.
     */
    void handleResponse(Response response);
}