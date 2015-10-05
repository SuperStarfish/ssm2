package com.sem.ssm2.screens.aquarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.structures.collection.collectibles.Collectible;

import java.util.Objects;
import java.util.Random;

// TODO: Fix boundaries
public class Fish extends Image {

    protected Sprite sprite;
    protected float speed = (float) (1 + Math.random() * 2);
    protected float flipChance = .01f;
    protected int maxAngleTurnSpeed = 4;
    protected float upperXBoundary, lowerXBoundary , upperYBoundary, lowerYBoundary;

    // Current angle of a fish. When a fish is rotating to a next angle, this value is kept as storage.
    protected int currentAngle = 0;

    protected Collectible collectible;


    public Fish(SpriteDrawable sprite, Collectible collectible) {
        super(sprite);
        this.collectible = collectible;
        this.sprite = sprite.getSprite();
        currentAngle = (int) this.getRotation(); // This should happen before initializeRandomPosition()
        initializeRandomPosition();
        updateBoundaries();
        setOrigin(getWidth()/2,getHeight()/2);
    }

    // Called once, at creation of the fish
    public void initializeRandomPosition() {
        final int angleOfCircle = 360;

        Random rnd = new Random();
        do {
            currentAngle = normalizeAngle(rnd.nextInt(angleOfCircle));
        } while (!validateAngle(currentAngle));

        if (currentAngle > 0) {
            flipImageY();
        }
        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();

        this.setPosition(screenWidth / 2, screenHeight / 2);

    }


    // Called every X time.
    public void swim() {
        generateAngle(maxAngleTurnSpeed);
        randomFlip();
        move();
        boundaryCheck();
    }

    public void generateAngle(int angle) {
        int newAngle;
        do {
            // create a new angle based on the old angle + a few additional degree of rotation
            // Math.random() * 2 - 1f
            //   :: Math.random() -> generates a double between 0 and 1
            //   :: 2 - 1f -> makes sure the angle will be between -1 and 1 instead of just 0 and 1
            newAngle = currentAngle + (int) ((Math.random() * 2 - 1f) * angle);

            // keep angles which make the fish turn out of the new generated angles
        } while (!validateAngle(newAngle));

        currentAngle = newAngle;
    }

    private void randomFlip(){
        if(Math.random() < flipChance){
            flipImageY();
            currentAngle *= -1;
        }
    }

    public void move() {
        final float defaultRotation = 90f;
        this.setRotation(currentAngle + defaultRotation);

        this.moveBy(
                (float) Math.sin(Math.toRadians(currentAngle)) * speed,
                (float) Math.cos(Math.toRadians(currentAngle)) * -speed);
    }

    public void updateBoundaries() {
        upperXBoundary = Gdx.graphics.getWidth() - getWidth() * 3 / 4;
        lowerXBoundary = -getWidth()/4;
        upperYBoundary = Gdx.graphics.getHeight() - getHeight() * 3 / 4;
        lowerYBoundary = 0;
    }

    // check for boundaries.
    public void boundaryCheck() {
        if (getX() < lowerXBoundary ||  getX() > upperXBoundary) {
            flipImageY();
            currentAngle *= -1;
            move();
        }

        if (getY() < lowerYBoundary || getY() > upperYBoundary) {
            currentAngle = normalizeAngle(180 - currentAngle);
            move();
        }
    }

    // normalize the angle
    public int normalizeAngle(int angle) {
        if (angle > 180) {
            angle = angle - 360;
        }
        if (angle < -180) {
            angle = angle + 360;
        }

        return angle;
    }

    // Validate that the angle is between a defined minimum and maximum.
    // This is so we do not get weird (vertically) swimming fish, which will
    // get out of bounds because the flips only happen on the Y-axis.
    // The current swimming algorithm can only swim horizontally.
    public boolean validateAngle(int angle) {
        final int min = 35; // 25 default
        final int max = 145; // 155 default

        angle = Math.abs(angle);
        return min < angle && angle < max;
    }

    // flips the image, so that the fish can swim to the other side of the aquarium
    public void flipImageY() {
        this.setScaleY(-this.getScaleY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fish)) {
            return false;
        }
        Fish fish = (Fish) o;
        return Objects.equals(collectible, fish.collectible);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectible);
    }
}
