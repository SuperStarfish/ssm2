package com.sem.ssm2.screens.aquarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.Random;

// TODO: Fix boundaries
public class Fish extends Image {

    protected static Random random = new Random();
    protected Sprite sprite;
    protected float speed = 1.0f;
    protected int maxAngleTurnSpeed = 4;

    // Current angle of a fish. When a fish is rotating to a next angle, this value is kept as storage.
    protected int currentAngle = 0;


    public Fish(SpriteDrawable sprite) {
        super(sprite);
        this.sprite = sprite.getSprite();
        currentAngle = (int) this.getRotation(); // This should happen before initializeRandomPosition()
        initializeRandomPosition();
    }

    // Called every X time.
    public void swim() {
        generateAngle(maxAngleTurnSpeed);
        move(speed);
        boundaryCheck();
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

        this.setPosition(screenWidth/2, screenHeight/2);

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

    public void move(float speed) {
        final float defaultRotation = 90f;
        this.setRotation(currentAngle + defaultRotation);

        this.moveBy(
                (float) Math.sin(Math.toRadians(currentAngle)) * speed,
                (float) Math.cos(Math.toRadians(currentAngle)) * -speed);
    }


    // check for boundaries.
    public void boundaryCheck() {
        final int halfOfCircleAngle = 180;

        if (outOfHorizontalBoundary()) {
            flipImageY();
            currentAngle *= -1;
        }

        if (outOfVerticalBoundary()) {
            currentAngle = normalizeAngle(halfOfCircleAngle - currentAngle);
        }
    }

    public boolean outOfHorizontalBoundary() {
        final int screenWidth = Gdx.graphics.getWidth();

        return this.getOriginX() < 0 || screenWidth < getOriginX();
    }

    public boolean outOfVerticalBoundary() {
        final int screenHeight = Gdx.graphics.getHeight();

        return this.getOriginY() < 0 || this.getOriginY() > screenHeight;
    }


    // normalize the angle
    public int normalizeAngle(int angle) {
        final int halfCircle = 180;
        final int fullCircle = 360;

        if (angle > halfCircle) {
            angle = angle - fullCircle;
        }
        if (angle < -halfCircle) {
            angle = angle + fullCircle;
        }

        return angle;
    }

    // Validate that the angle is between a defined minimum and maximum.
    // This is so we do not get weird (vertically) swimming fish, which will
    // get out of bounds because the flips only happen on the Y-axis.
    // The current swimming algorithm can only swim horizontally.
    public boolean validateAngle(int angle) {
        final int min = 25; // 25 default
        final int max = 155; // 155 default

        angle = Math.abs(angle);
        return min < angle && angle < max;
    }

    // flips the image, so that the fish can swim to the other side of the aquarium
    public void flipImageY() {
        this.setScaleY(-this.getScaleY());
    }

}
