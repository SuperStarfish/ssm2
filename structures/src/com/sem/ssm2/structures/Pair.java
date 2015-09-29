package com.sem.ssm2.structures;

/**
 * Data class which holds a Pair of type T.
 *
 * @param <T> Type of the objects.
 */
public class Pair<T> {

    /**
     * First element of the pair.
     */
    private T cElement1;

    /**
     * Second element of the pair.
     */
    private T cElement2;


    /**
     * Initializes a pair of two elements.
     *
     * @param elem1 first element
     * @param elem2 second element
     */
    public Pair(final T elem1, final T elem2) {
        cElement1 = elem1;
        cElement2 = elem2;
    }

    /**
     * Returns the first element of the pair.
     *
     * @return T first element
     */
    public T getElement1() {
        return cElement1;
    }

    /**
     * Sets the first element of the pair.
     *
     * @param elem first element
     */
    public void setElement1(final T elem) {
        this.cElement1 = elem;
    }

    /**
     * gets the second element of the pair.
     *
     * @return T second element
     */
    public T getElement2() {
        return cElement2;
    }

    /**
     * Sets the second element of the pair.
     *
     * @param elem The second element;
     */
    public void setElement2(final T elem) {
        this.cElement2 = elem;
    }

}