package com.sem.ssm2.structures.collection;

import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;

import java.util.Random;

/**
 * Objects that generates the rewards.
 */
public class RewardGenerator {
    /**
     * Object that creates collectibles.
     */
    protected CollectibleFactory cCollectibleFactory;
    /**
     * Generates random numbers used in the generation of the rewards.
     */
    protected Random cRNG;
    /**
     * The owner of the collectibles that are generated.
     */
    protected String cOwnerId;

    /**
     * Constructs a new reward generator.
     *
     * @param ownerId The owner of the collectibles that will be generated.
     */
    public RewardGenerator(final String ownerId) {
        cRNG = new Random();
        cCollectibleFactory = new CollectibleFactory();
        cOwnerId = ownerId;
    }

    /**
     * Generates a collectible based on the score that was earned in the event.
     * The higher the score, the more chance on a more rare collectible.
     *
     * @param eventScore score earned during the event.
     * @return Collectible object.
     */
    public final Collectible generateCollectible(final int eventScore) {
        Collectible mostValuable = null;

        for (int i = 0; i < eventScore; i++) {
            Collectible c = generateOneCollectible();
            if (mostValuable == null) {
                mostValuable = c;
            }
            mostValuable = getMostRare(c, mostValuable);
        }
        return mostValuable;
    }

    /**
     * Helper method that should not be called outside of this class.
     * Generates a random collectible and return this created collectible.
     *
     * @return Collectible object.
     */
    protected Collectible generateOneCollectible() {
        String[] collectibleList = cCollectibleFactory.getCollectiblesList();
        int nr = cRNG.nextInt(collectibleList.length);
        float hue = (float) rewardFunction(cRNG.nextFloat());
        return cCollectibleFactory.generateCollectible(collectibleList[nr], hue, cOwnerId);
    }

    /**
     * Returns the most valuable of two collectibles.
     *
     * @param c1 Collectible 1
     * @param c2 Collectible 2
     * @return most rare collectible
     */
    protected Collectible getMostRare(final Collectible c1, final Collectible c2) {
        if (c1.getRarity() > c2.getRarity()) {
            return c1;
        }
        return c2;
    }

    /**
     * Helper method that should not be called outside of this class.
     * Applies a function to the input.
     * <p/>
     * The reward function now is specified as: f(x) = x^4.
     *
     * @param x Input
     * @return Result after applying the function to the input
     */
    protected final double rewardFunction(final double x) {
        final int exponent = 4;
        return Math.pow(x, exponent);
    }
}