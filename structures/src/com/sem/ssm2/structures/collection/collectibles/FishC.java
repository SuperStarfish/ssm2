package com.sem.ssm2.structures.collection.collectibles;

import java.io.Serializable;
import java.util.Date;

/**
 * A collectible with variable colour that can be generated.
 */
public class FishC extends Collectible implements Serializable {

    /**
     * The rarity of this collectible.
     */
    protected final float cFormRarity = 1f;

    /**
     * Constructs a FishC collectible.
     *
     * @param hue     representing the colour of the collectible
     * @param ownerId The owner of the collectible.
     */
    public FishC(final float hue, final String ownerId) {
        super(hue, ownerId);
    }

    /**
     * Constructs a FishC collectible.
     *
     * @param hue     The colour of the collectible.
     * @param amount  The amount of collectibles represented by this instance.
     * @param date    The date this collectible was collected.
     * @param ownerId The owner of the collectible.
     */
    public FishC(final float hue, final int amount, final Date date, final String ownerId) {
        super(hue, amount, date, ownerId);
    }

    @Override
    public String getImagePath() {
        return "images/FishC.png";
    }

    @Override
    public float getFormRarity() {
        return cFormRarity;
    }

}