package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.Gdx;
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
import com.sem.ssm2.screens.StrollScreen;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class HostMultiPlayerScreen extends SimpleScreen {

    public HostMultiPlayerScreen(Game game) {
        super(game);
    }

    protected Label label;
    protected MultiPlayerHost host;

    @Override
    public void show() {
        host = new MultiPlayerHost();
        host.getDisconnectSubject().addObserver(
                new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(StrollScreen.class);
                            }
                        });

                    }
                }
        );
        super.show();
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
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                host.connect();
                                if(host.isConnected()) {
                                    game.setHost(host);
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            game.setMultiPlayerScreen(FishingBoatHostScreen.class);
                                        }
                                    });

                                }
                            }
                        }).start();
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
