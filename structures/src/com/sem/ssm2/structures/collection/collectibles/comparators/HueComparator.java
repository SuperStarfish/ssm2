package com.sem.ssm2.structures.collection.collectibles.comparators;

import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.util.Comparator;

/**
 * Compares two Collectibles based on their Hue.
 */
public class HueComparator implements Comparator<Collectible> {

    @Override
    public int compare(Collectible o1, Collectible o2) {
        if (o1.getHue() <  o2.getHue()) return  1;
        if (o1.getHue() == o2.getHue()) return  0;
        // else
        return -1;
    }

    @Override
    public String toString() {
        return "Hue";
    }
}
