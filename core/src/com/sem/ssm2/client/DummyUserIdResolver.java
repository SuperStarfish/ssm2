package com.sem.ssm2.client;

/**
 * A dummy user id resolver to make sure there always is a user is resolver.
 */
public class DummyUserIdResolver implements UserIDResolver {
    @Override
    public String getID() {
        return "";
    }
}