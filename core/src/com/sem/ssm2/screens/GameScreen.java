package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.sem.ssm2.GameCore;
import com.sem.ssm2.assets.Assets;

public abstract class GameScreen implements Screen {
    protected Assets assets = Assets.getInstance();
    protected GameCore game;
    protected InputMultiplexer inputMultiplexer;

    public GameScreen(GameCore game) {
        this.game = game;
        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
    }

    public boolean loadAssets() {
        createAssets();
        return assets.update();
    }

    protected abstract void createAssets();
}
