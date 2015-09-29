package com.sem.ssm2.server.database;

/**
 * Combines multiple response handlers to a single response handler.
 * Only successfulness of the operations is handled.
 */
public class MultiResponseHandler implements ResponseHandler {

    /**
     * The response handler to call with the result.
     */
    protected final ResponseHandler cResponseHandler;
    /**
     * The number of responses handled so far.
     */
    protected int cNumberOfResponses;
    /**
     * The number of responses to handle.
     */
    protected int cMaxNumberOfResponses;

    /**
     * Combines multiple response handlers to a single response handler.
     *
     * @param responseHandler      The response handler to call with the result.
     * @param maxNumberOfResponses The number of responses to handle.
     */
    public MultiResponseHandler(ResponseHandler responseHandler, final int maxNumberOfResponses) {
        cResponseHandler = responseHandler;
        cNumberOfResponses = 0;
        cMaxNumberOfResponses = maxNumberOfResponses;
    }

    @Override
    public void handleResponse(Response response) {
        cNumberOfResponses++;
        if (!response.isSuccess() || cNumberOfResponses == cMaxNumberOfResponses) {
            if (cResponseHandler != null) {
                cResponseHandler.handleResponse(response);
            }
        }
    }
}