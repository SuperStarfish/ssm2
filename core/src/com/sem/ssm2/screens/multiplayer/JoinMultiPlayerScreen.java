package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sem.ssm2.Game;
import com.sem.ssm2.multiplayer.MultiPlayerClient;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.SimpleScreen;
import com.sem.ssm2.screens.StrollScreen;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.HostData;
import com.sem.ssm2.util.TextFieldTable;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class JoinMultiPlayerScreen extends SimpleScreen {

    public JoinMultiPlayerScreen(Game game) {
        super(game);
    }

    protected TextFieldTable textFieldTable;
    protected MultiPlayerClient multiPlayerClient;

    @Override
    protected WidgetGroup createBody() {
        Table table = new Table();
        table.add(assets.generateLabel("Connect to someone"));
        table.row();

        textFieldTable = assets.generateTextField("code");
        textFieldTable.getInnerTextField().addListener(new ClickListener() {

        });

        table.add(textFieldTable);
        table.row();

        TextButton connectButton = assets.generateSimpleTextButton("Connect");
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                client.getHostData(textFieldTable.getInnerTextField().getText().trim(), new ResponseHandler(){
                    @Override
                    public void handleResponse(Response response) {
                        if(response.isSuccess()){
                            try {
                                multiPlayerClient = new MultiPlayerClient((HostData)response.getData());
                                multiPlayerClient.getDisconnectSubject().addObserver(
                                        new Observer() {
                                            @Override
                                            public void update(Observable o, Object arg) {
                                                if(!(boolean)arg) {
                                                    Gdx.app.postRunnable(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            game.setScreen(StrollScreen.class);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                );
                                multiPlayerClient.connect();
                                game.setHost(multiPlayerClient);
                                game.setMultiPlayerScreen(FishingBoatClientScreen.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        table.add(connectButton);
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
