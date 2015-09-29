package com.sem.ssm2.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.sem.ssm2.assets.Assets;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

/**
 * Class that creates images from collectibles and returns these created images.
 */
public final class CollectibleDrawer {

    protected Assets assets;

    public CollectibleDrawer(Assets assets) {
        this.assets = assets;
    }

    /**
     * Returns an image of the given collectible.
     *
     * @param c Collectible that needs to be drawn.
     * @return Texture image with the collectible.
     */
    public Sprite drawCollectible(final Collectible c) {
        Sprite sprite = new Sprite(assets.get(c.getImagePath(), Texture.class));
        sprite.setColor(RewardUtil.generateColor(c.getHue()));
        return sprite;
    }
}