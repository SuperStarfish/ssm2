package com.sem.ssm2.screens.aquarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.screens.GameScreen;
import com.sem.ssm2.screens.Screen;

public class Aquarium extends GameScreen {

    protected Game game;

    protected Table filler, layout;
    protected Stage stage;


    public Aquarium(Game game) {
        super(game);
    }


    @Override
    public Class<? extends Screen> previousScreen() {
        return null;
    }

    @Override
    public void loadAssets() {
        assets.load("images/FishA.png", Texture.class);
        assets.load("images/FishB.png", Texture.class);
        assets.load("images/FishC.png", Texture.class);
        assets.load("images/Rock1.png", Texture.class);
        assets.load("images/kelp.png", Texture.class);
        assets.load("images/rock2.png", Texture.class);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        assets.generateFont("someFont", "fonts/NotoSans-Regular.ttf", parameter);
    }

    @Override
    public void show() {
        stage = new Stage();
        stage.setDebugAll(true);
        filler = new Table();
        stage.addActor(filler);
        filler.setFillParent(true);
        layout = new Table();
        layout.setBackground(new TextureRegionDrawable(new TextureRegion(createMenuBackground())));

        filler.add(layout).top();

        Label.LabelStyle labelStyle = new Label.LabelStyle(assets.get("someFont", BitmapFont.class), Color.ORANGE);
        layout.add(new Label("select group", labelStyle)).top();

    }

    private Texture createMenuBackground() {
        Pixmap pixmap = new Pixmap(4,4, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(59 / 255f, 85 / 255f, 1, 1));
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
