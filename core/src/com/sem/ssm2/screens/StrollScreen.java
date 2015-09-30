package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sem.ssm2.Game;

public class StrollScreen extends GameScreen {
    public StrollScreen(Game game) {
        super(game);
    }


    protected SpriteBatch batch;
    protected TextureRegion world;
    protected float rotation = 0, rotationSpeed = 0.05f;
    TextureRegion[] animationFrames;
    Animation animation;
    float elapsedTime;

    @Override
    public void loadAssets() {
        assets.load("images/world.png", Texture.class);
        assets.load("images/walking_guy.png", Texture.class);
    }

    @Override
    public void show() {
        elapsedTime = 0f;
        batch = new SpriteBatch();
        Texture texture = assets.get("images/world.png", Texture.class);
        world = new TextureRegion(assets.get("images/world.png", Texture.class));
        TextureRegion[][] temp = TextureRegion.split(assets.get("images/walking_guy.png", Texture.class), 256, 256);
        animationFrames = new TextureRegion[4];
        int index = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                animationFrames[index++] = temp[j][i];
            }
        }

        animation = new Animation(1/4f, animationFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += delta;

        batch.begin();
        float width = Gdx.graphics.getWidth();
        batch.draw(world,
                0,
                -0.7f * width,
                width / 2f,
                width / 2f,
                width,
                width,
                1,
                1,
                rotation);
        batch.draw(animation.getKeyFrame(elapsedTime),
                Gdx.graphics.getWidth() / 2f - 128 * assets.getRatio(),
                270 * assets.getRatio(),
                0,
                0,
                256 * assets.getRatio(),
                256 * assets.getRatio(),
                1,
                1,
                0);

//        batch.draw(animation.getKeyFrame(elapsedTime),
//                Gdx.graphics.getWidth() / 2f - 128, 120 * assets.getRatio());

        rotation += rotationSpeed;
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}
