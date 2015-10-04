package com.sem.ssm2.structures.collection.collectibles.comparators;

import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.util.Comparator;

/**
 * Compares two collectibles based on their rarity.
 */
public class RarityComparator implements Comparator<Collectible> {

    @Override
    public int compare(Collectible o1, Collectible o2) {
        if (o1.getRarity() <  o2.getRarity()) return 1;
        if (o1.getRarity() == o2.getRarity()) return 0;

        return -1;
    }

    @Override
    public String toString() {
        return "Rarity (Comparator)";
    }
}
