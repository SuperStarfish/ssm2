package com.sem.ssm2.screens.aquarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.structures.Pair;

import java.util.Random;

public class Fish extends Image {

    protected static Random random = new Random();
    protected Sprite sprite;
    protected Pair<Integer> target;
    protected int speed = 1;

    public Fish(SpriteDrawable sprite) {
        super(sprite);
        this.sprite = sprite.getSprite();
        this.setPosition(randomX(), randomY());
        target = new Pair<>(randomX(), randomY());
    }

    public void swim() {
        double deltaX = Math.abs(target.getElement1() - getX());
        double deltaY = Math.abs(target.getElement2() - getY());

        if(deltaX >= speed){
            if(target.getElement1() > getX()) {
                moveBy(speed, 0);
            } else {
                moveBy(-speed, 0);
            }
        }
        if(deltaY >= speed) {
            if(target.getElement2() > getY()) {
                moveBy(0, speed);
            } else {
                moveBy(0, -speed);
            }
        }

        if(deltaX <= speed && deltaY <= speed){
            setNewTarget();
        }

        rotateBy(random.nextBoolean() ? 1 : -1);
    }

    protected void setNewTarget() {
        target.setElement1(random.nextInt(randomX()));
        target.setElement2(random.nextInt(randomY()));
    }

    protected int randomX() {
        return random.nextInt((int)(Gdx.graphics.getWidth() - sprite.getWidth()));
    }

    protected int randomY() {
        return random.nextInt((int)(Gdx.graphics.getHeight() - sprite.getHeight()));
    }

}
