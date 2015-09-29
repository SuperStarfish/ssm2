package com.sem.ssm2.desktop.util;

import com.sem.ssm2.structures.Subject;
import com.sem.ssm2.util.AccelerationState;
import com.sem.ssm2.util.AccelerationStatus;

/**
 * Desktop implementation of the acceleration status.
 */
public class DesktopAccelerationStatus implements AccelerationStatus {

    /**
     * Subject that does not do anything.
     */
    protected Subject cUselessSubject = new Subject();

    /**
     * Since a desktop has no acceleration, we here always return a default state.
     * The default state is AccelerationState.CHEATING
     *
     * @return AccelerationState.CHEATING
     */
    @Override
    public final AccelerationState getAccelerationState() {
        return AccelerationState.CHEATING;
    }

    @Override
    public final Subject getSubject() {
        return cUselessSubject;
    }
}