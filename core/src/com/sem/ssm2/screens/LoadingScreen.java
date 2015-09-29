package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.sem.ssm2.Game;

public class LoadingScreen extends GameScreen {

    public LoadingScreen(Game game) {
        super(game);
    }

    private SpriteBatch batch;
    private BitmapFont loadingFont;
    GlyphLayout layout;

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        assets.generateFont("default", "fonts/Blenda Script.otf", parameter);
    }

    @Override
    public void show() {
        loadingFont = assets.get("default", BitmapFont.class);
        layout = new GlyphLayout(loadingFont, "0");
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        if(assets.update()) {
            game.setQueuedScreen();
        } else {
            float progress = assets.getProgress();
            Gdx.gl.glClearColor(
                    59 / 255f * progress,
                    179 / 255f * progress,
                    1f * progress,
                    1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            layout.setText(loadingFont, Integer.toString((int)(progress * 100)));
            batch.begin();
            loadingFont.draw(
                    batch,
                    layout,
                    Gdx.graphics.getWidth() / 2 - layout.width / 2,
                    Gdx.graphics.getHeight() / 2 + layout.height / 2);
            batch.end();
        }
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
