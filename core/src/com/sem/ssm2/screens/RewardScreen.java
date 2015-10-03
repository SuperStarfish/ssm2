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

public class RewardScreen extends GameScreen {

    protected Stage stage;
    protected Stroll stroll = game.getStroll();

    public RewardScreen(Game game) {
        super(game);
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
    }

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        assets.generateFont("rewardsFont", "fonts/NotoSans-Regular.ttf", parameter);
    }

    @Override
    public void show() {
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        Label.LabelStyle labelStyle = new Label.LabelStyle(assets.get("rewardsFont", BitmapFont.class), Color.RED);
        table.add(new Label("Rewards: " + stroll.getStrollRewards(), labelStyle));
        table.row();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                assets.get("rewardsFont", BitmapFont.class)
        );

        TextButton textButton = new TextButton("Back", textButtonStyle);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen());
            }
        });
        table.add(textButton);

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
