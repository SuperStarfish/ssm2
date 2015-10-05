package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.sem.ssm2.Game;
import com.sem.ssm2.client.Client;
import com.sem.ssm2.multiplayer.MessageHandler;
import com.sem.ssm2.multiplayer.MultiPlayerHost;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.SimpleScreen;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;

public class HostMultiPlayerScreen extends SimpleScreen {

    public HostMultiPlayerScreen(Game game) {
        super(game);
    }

    protected Label label;
    protected MultiPlayerHost host;

    @Override
    public void show() {
        host = new MultiPlayerHost();
        super.show();
    }

    @Override
    public void hide() {
        host.stopHosting();
        super.hide();
    }

    @Override
    protected WidgetGroup createBody() {

        Table table = new Table();
        table.add(assets.generateLabel("Wait for someone to join"));
        table.row();

        label = assets.generateLabel("Getting a code");
        table.add(label).padTop(100 * assets.getRatio()).padBottom(100 * assets.getRatio());

        table.row();

        if(Client.getIPAddress(true).isEmpty()) {
            label.setText("Not connected!");
        } else {
            client.requestHostCode(host.getPort(), new ResponseHandler() {
                @Override
                public void handleResponse(Response response) {
                    if(response.isSuccess()) {
                        label.setText("Code: " + response.getData());
                        host.receiveTCP(new MessageHandler() {
                            @Override
                            public void handleMessage(Object message) {
                                System.out.println(message);
                            }
                        }, false);
                    }
                }
            });
        }

        Label shareLabel = assets.generateLabel("Share this code with \n your friends!");
        shareLabel.setAlignment(Align.center);

        table.add(shareLabel).align(Align.center);
        table.row();

        TextButton textButton = assets.generateSimpleTextButton("Back");
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen());
            }
        });
        table.add(textButton);
        table.row();

        return table;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MultiPlayerScreen.class;
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
