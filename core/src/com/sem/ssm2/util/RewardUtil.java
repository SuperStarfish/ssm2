package com.sem.ssm2.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Utility class that converts a wavelength to a RGB colour.
 * <p/>
 * Sources:
 * http://www.efg2.com/Lab/ScienceAndEngineering/Spectra.htm
 * http://stackoverflow.com/questions/1472514/convert-light-frequency-to-rgb
 */
public final class RewardUtil {
    /**
     * Number of color categories.
     */
    protected static final int NUMBER_OF_COLORS = 6;

    /**
     * Hue is capped to prevent double colors with high and low rarity.
     */
    protected static final float HUE_CAP = (NUMBER_OF_COLORS - 1f) / NUMBER_OF_COLORS;

    /**
     * Int belonging to each color category.
     */
    protected static final int ORANGE = 0, LIGHT_GREEN = 1, DARK_GREEN = 2, CYAN = 3, MAGENTA = 4, PURPLE = 5;

    /**
     * Utilities needed for rewards.
     */
    protected RewardUtil() {

    }

    /**
     * Generates a color based on the hue given.
     *
     * @param hue The hue (between 0 and 1)
     * @return The color belonging to the hue
     */
    public static Color generateColor(final float hue) {
        float h = hue * HUE_CAP * NUMBER_OF_COLORS;
        float f = h - (float) java.lang.Math.floor(h);
        float q = 1 - f;
        Color color;
        switch ((int) h) {
            case ORANGE:
                color = new Color(1, f, 0, 1);
                break;
            case LIGHT_GREEN:
                color = new Color(q, 1, 0, 1);
                break;
            case DARK_GREEN:
                color = new Color(0, 1, f, 1);
                break;
            case CYAN:
                color = new Color(0, q, 1, 1);
                break;
            case MAGENTA:
                color = new Color(f, 0, 1, 1);
                break;
            case PURPLE:
                color = new Color(1, 0, q, 1);
                break;
            default:
                color = new Color(0, 0, 0, 1);
        }
        return color;
    }
}