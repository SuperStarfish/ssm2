package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.sem.ssm2.Game;

public abstract class SimpleScreen extends GameScreen {

    protected Stage stage;
    private Table table;

    public SimpleScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(createBody()).fill().expand();
//        table.row();
//        table.add().fillY().expandY();
    }

    protected abstract WidgetGroup createBody();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
    }
}
