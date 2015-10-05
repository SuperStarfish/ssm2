package com.sem.ssm2.screens.multiplayer;

import com.sem.ssm2.Game;
import com.sem.ssm2.multiplayer.Host;
import com.sem.ssm2.screens.SimpleScreen;
import com.sem.ssm2.util.Accelerometer;

import java.util.HashMap;

/**
 * Defaults for both the client and the host part of the CraneFishing MultiPlayer Event.
 */
public abstract class FishingBoatEvent extends SimpleScreen {
    /**
     * Lowers the noise threshold to make it less 'snappy' to the X and Y axis.
     */
    protected final float cNoiseThreshold = 0.5f;
    /**
     * The reward received when completing the event.
     */
    protected final int reward = 3;

    /**
     * Connection with the other client.
     */
    protected Host host;

    /**
     * The accelerometer to determine rotation and direction.
     */
    protected Accelerometer accelerometer;

    protected int amountOfFish = 10;
    protected SmallFish[] allFish;

    /**
     * Construct a new CraneFishingEvent.
     *
     * @param otherClient Connection with the other client.
     */
    public FishingBoatEvent(Game game, Host otherClient) {
        super(game);
        host = otherClient;
        accelerometer = new Accelerometer(game.getSensorReader());
        accelerometer.filterGravity(false);
        accelerometer.setNoiseThreshold(cNoiseThreshold);
        accelerometer.setFilterPerAxis(true);

        allFish = new SmallFish[amountOfFish];
    }

    public void updateFishes() {
        for(SmallFish smallFish : allFish) {
            if(smallFish != null) {
                smallFish.swim();
            }
        }
    }

}