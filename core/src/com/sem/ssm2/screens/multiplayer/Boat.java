package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.sem.ssm2.assets.Assets;

/**
 * Displays the boat image.
 */
public class Boat extends Image {
    /**
     * Creates a new boat and sets the origin to the center of the wrapping Stack.
     *
     * @param stack The wrapping Stack.
     */
    public Boat(Assets assets, Stack stack) {
        super(assets.get("images/Boat.png", Texture.class));
        this.layout();

        final int center = (int) (stack.getWidth() / 2f);
        this.setOrigin(center, center);
    }
}
