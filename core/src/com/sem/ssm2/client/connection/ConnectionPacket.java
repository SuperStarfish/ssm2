package com.sem.ssm2.client.connection;

import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.server.database.query.Query;

/**
 * Packet to contain the info needed for a transfer over a server connection.
 */
public class ConnectionPacket {
    /**
     * The query to be sent.
     */
    protected Query cQuery;
    /**
     * The response handler to handle the response of the transfer.
     */
    protected ResponseHandler cResponseHandler;

    /**
     * Packet to contain the info needed for a transfer over a server connection.
     *
     * @param query           The query to be sent to the server.
     * @param responseHandler The response handler to handle the output of the transfer.
     */
    public ConnectionPacket(final Query query, final ResponseHandler responseHandler) {
        cQuery = query;
        cResponseHandler = responseHandler;
    }

    /**
     * Getter for the query.
     *
     * @return The query for the transfer.
     */
    public Query getQuery() {
        return cQuery;
    }

    /**
     * Getter for the response handler.
     *
     * @return The response handler for the transfer.
     */
    public ResponseHandler getResponseHandler() {
        return cResponseHandler;
    }
}