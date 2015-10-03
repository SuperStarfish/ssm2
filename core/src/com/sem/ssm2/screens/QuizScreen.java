package com.sem.ssm2.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sem.ssm2.Game;
import com.sem.ssm2.stroll.Stroll;

public class QuizScreen extends GameScreen {

    Stage stage;
    Stroll stroll;

    public QuizScreen(Game game) {
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
    }

    @Override
    public void show() {
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        stroll = game.getStroll();
        Label.LabelStyle labelStyle = new Label.LabelStyle(assets.get("quizFont", BitmapFont.class), Color.CHARTREUSE);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                assets.get("quizFont", BitmapFont.class)
        );


        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Some quiz here", labelStyle)).top();
        table.row();
        table.add(new Label("What is A?", labelStyle));
        table.row();
        TextButton buttonA = new TextButton("That's A", textButtonStyle);
        buttonA.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stroll.addRewards(1);
                game.setScreen(previousScreen());
            }
        });

        table.add(buttonA).pad(20).expandX();
        table.row();
        table.add(new TextButton("That's B", textButtonStyle)).pad(20).expandX();
        table.row();
        table.add(new TextButton("That's C", textButtonStyle)).pad(20).expandX();
        table.row();
        table.add(new TextButton("That's D", textButtonStyle)).pad(20).expandX();
        table.row();

        stage.addActor(table);
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
