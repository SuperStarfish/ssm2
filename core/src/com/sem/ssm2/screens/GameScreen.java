package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.sem.ssm2.Game;
import com.sem.ssm2.assets.Assets;
import com.sem.ssm2.client.Client;

public abstract class GameScreen implements Screen {
    protected Assets assets;
    protected Game game;
    protected InputMultiplexer inputMultiplexer;
    protected Client client;

    public GameScreen(Game game) {
        this.game = game;
        assets = game.getAssets();
        client = game.getClient();
        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
    }

}
