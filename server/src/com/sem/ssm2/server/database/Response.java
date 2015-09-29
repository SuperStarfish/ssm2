package com.sem.ssm2.server.database;

import java.io.Serializable;

/**
 * Object that server sends back to client.
 */
public class Response implements Serializable {
    /**
     * Indicates if the query was successful.
     */
    protected Boolean cSuccess;
    /**
     * The data to return to the client.
     */
    protected Serializable cData;

    /**
     * Constructs a new response.
     *
     * @param success If the query was successful.
     * @param data    The data to return.
     */
    public Response(final boolean success, final Serializable data) {
        cSuccess = success;
        cData = data;
    }

    /**
     * Was the query successful.
     *
     * @return whether it was successful.
     */
    public Boolean isSuccess() {
        return cSuccess;
    }

    /**
     * The data the server returned.
     *
     * @return The data.
     */
    public Serializable getData() {
        return cData;
    }
}