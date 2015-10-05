package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.assets.Assets;
import com.sem.ssm2.structures.mp_fishingboat.Coordinate;
import com.sem.ssm2.structures.mp_fishingboat.SmallFishData;

import java.util.Random;

/**
 * Class to make more readable that this Image is a SmallFish.
 */
public class SmallFish extends Image {

    protected int index;
    protected int maxX, maxY;
    protected float ratio;

    protected SmallFishData smallFishData;

    public SmallFish(Assets assets, int index) {
        super(new SpriteDrawable(new Sprite(assets.get("images/SmallFish.png", Texture.class))));
        smallFishData = new SmallFishData();
        ratio = assets.getRatio();
        this.index = index;

        Sprite sprite = ((SpriteDrawable)getDrawable()).getSprite();
        sprite.setSize(sprite.getWidth() * assets.getRatio(), sprite.getHeight() * assets.getRatio());

        maxX = (int)(Gdx.graphics.getWidth() - sprite.getWidth());
        maxY = (int)(Gdx.graphics.getHeight() - sprite.getHeight());

        setPosition(smallFishData.getPosition());
    }

    protected void swim() {
        smallFishData.move();
        if(smallFishData.destinationReached()) {
            smallFishData.setDestination(Coordinate.randomCoordinate());
        }
        setPosition(smallFishData.getPosition());
        setRotation(smallFishData.getRotation());
    }

    protected void setPosition(Coordinate coordinate) {
        setPosition(coordinate.getX() * ratio, coordinate.getY() * ratio);
    }

}