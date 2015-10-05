package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.sem.ssm2.assets.Assets;

/**
 * Displays the Crane image.
 */
public class Crane extends Image {

    protected Assets assets;

    /**
     * Creates a new Crane and sets the origin to the center of the wrapping Stack.
     *
     * @param stack The wrapping Stack.
     */
    public Crane(Assets assets, Stack stack) {
        super(assets.get("images/Crane.png", Texture.class));
        this.layout();

        final int center = (int) (stack.getWidth() / 2f);
        this.setOrigin(center, center);
    }

}
