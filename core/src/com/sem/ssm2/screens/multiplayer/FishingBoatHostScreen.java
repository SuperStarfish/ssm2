package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.sem.ssm2.Game;
import com.sem.ssm2.multiplayer.Host;
import com.sem.ssm2.multiplayer.MessageHandler;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.SimpleScreen;
import com.sem.ssm2.screens.StrollScreen;

public class FishingBoatHostScreen extends SimpleScreen {
    protected Host host;

    public FishingBoatHostScreen(Game game, Host host) {
        super(game);
        this.host = host;
    }

    @Override
    protected WidgetGroup createBody() {
        host.sendTCP("ping");
        host.receiveTCP(new MessageHandler() {
            @Override
            public void handleMessage(Object message) {
                System.out.println(message);
                host.sendTCP("ping");
            }
        }, true);
        return null;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return StrollScreen.class;
    }

    @Override
    public void loadAssets() {

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
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}
