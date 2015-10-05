package com.sem.ssm2.multiplayer;

/**
 * Used to define behaviour when receiving messages over TCP or UDP between clients.
 */
public interface MessageHandler {
    /**
     * The action to take when messages are received.
     *
     * @param message The Object in the message.
     */
    void handleMessage(Object message);
}
