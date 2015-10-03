package com.sem.ssm2.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.stroll.Stroll;

import java.util.Random;

public class TapTheFishScreen extends GameScreen {

    Stage stage;
    Image image;
    Random random = new Random();
    Stroll stroll;
    int count;

    public TapTheFishScreen(Game game) {
        super(game);
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return StrollScreen.class;
    }

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = Color.LIME;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        assets.generateFont("quizFont", "fonts/NotoSans-Regular.ttf", parameter);
        assets.load("images/catfish.png", Texture.class);
    }

    @Override
    public void show() {
        stage = new Stage();
        stroll = game.getStroll();
        inputMultiplexer.addProcessor(stage);

        count = 0;

        Label.LabelStyle labelStyle = new Label.LabelStyle(assets.get("quizFont", BitmapFont.class), Color.CHARTREUSE);
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Tap for the fish!", labelStyle)).top();
        stage.addActor(table);

        Sprite sprite = new Sprite(assets.get("images/catfish.png", Texture.class));
        sprite.setSize(sprite.getWidth() * assets.getRatio(), sprite.getHeight() * assets.getRatio());
        image = new Image(new SpriteDrawable(sprite));
        setRandomPosition();
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DONT TOUCH ME");
                setRandomPosition();
                count++;
                stroll.addRewards(1);
                if(count == 2) {
                    game.setScreen(previousScreen());
                }
                super.clicked(event, x, y);
            }
        });
        stage.addActor(image);
    }

    protected void setRandomPosition() {
        image.setPosition(
                random.nextInt((int)(Gdx.graphics.getWidth() - image.getWidth())),
                random.nextInt((int)(Gdx.graphics.getHeight() - image.getHeight()))
        );
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
        inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}
