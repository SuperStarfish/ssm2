package com.sem.ssm2.structures.collection.collectibles;

import java.util.*;

/**
 * Creates and returns collectible objects.
 */
public class CollectibleFactory {

    /**
     * List containing the names of all the possible collectibles.
     * Used by the rewardgenerator to randomly pick one.
     */
    protected String[] cCollectiblesList = {
            Collectible.FishA.toString(),
            Collectible.FishB.toString(),
            Collectible.FishC.toString()};

    /**
     * Generates a collectible of the class of the given string, with colour wavelength.
     *
     * @param type    represents the class of the collectible
     * @param hue     represent colour of the collectible.
     * @param ownerId The owner of the collectible.
     * @return Collectible object
     */
    public com.sem.ssm2.structures.collection.collectibles.Collectible generateCollectible(final String type, final float hue, final String ownerId) {
        com.sem.ssm2.structures.collection.collectibles.Collectible result = null;

        switch (type) {
            case "FishA":
                result = new FishA(hue, ownerId);
                break;
            case "FishB":
                result = new FishB(hue, ownerId);
                break;
            case "FishC":
                result = new FishC(hue, ownerId);
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * Constructs a new collectible.
     * This method in for the construction by the client and server to retrieve the data and make a new object.
     *
     * @param type    The type of collectible to construct.
     * @param hue     The color of the collectible.
     * @param amount  The amount of fish of this collectible.
     * @param date    The date that this collectible was found.
     * @return The collectible.
     */
    public final com.sem.ssm2.structures.collection.collectibles.Collectible generateCollectible(final String type, final float hue, final int amount,
                                                 final Date date) {
        return generateCollectible(type, hue, amount, date, null);
    }

    /**
     * Constructs a new collectible.
     * This method in for the construction by the client and server to retrieve the data and make a new object.
     *
     * @param type    The type of collectible to construct.
     * @param hue     The color of the collectible.
     * @param amount  The amount of fish of this collectible.
     * @param date    The date that this collectible was found.
     * @param ownerId The owner of the collectible.
     * @return The collectible.
     */
    public final com.sem.ssm2.structures.collection.collectibles.Collectible generateCollectible(final String type, final float hue, final int amount,
                                                 final Date date, final String ownerId) {
        com.sem.ssm2.structures.collection.collectibles.Collectible result = null;

        switch (type) {
            case "FishA":
                result = new FishA(hue, amount, date, ownerId);
                break;
            case "FishB":
                result = new FishB(hue, amount, date, ownerId);
                break;
            case "FishC":
                result = new FishC(hue, amount, date, ownerId);
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * Gets the list of the names of all the possible collectibles.
     *
     * @return String[] containing all the names.
     */
    public final String[] getCollectiblesList() {
        return cCollectiblesList;
    }

    /**
     * Contains all the possible collectibles.
     *
     * @author Jean de Leeuw
     */
    public enum Collectible {
        /**
         * Collectible FishA.
         */
        FishA,

        /**
         * Collectible FishB.
         */
        FishB,

        /**
         * Collectible FishC.
         */
        FishC;

        private static final List<Collectible> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Collectible randomCollectible()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }
}