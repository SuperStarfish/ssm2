package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sem.ssm2.Game;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.groups.GroupData;

import java.util.ArrayList;


public class GroupOverviewScreen extends BaseMenuScreen {

    protected Label label;
    protected Table table;
    protected PlayerData playerData;

    public GroupOverviewScreen(Game game) {
        super(game);
    }

    @Override
    public String getScreenName() {
        return "My Groups";
    }

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return SearchGroupScreen.class;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return CollectionScreen.class;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
    }

    @Override
    void extraAssets() {

    }

    @Override
    protected Actor createSubHeader() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                assets.get("white_buttonFont", BitmapFont.class)
        );
        TextButton textButton = new TextButton("+", textButtonStyle);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(NewGroupScreen.class);
            }
        });
        return textButton;
    }

    @Override
    protected WidgetGroup createBody() {
        table = new Table();
        label = new Label("", labelStyle);
        if(client.isRemoteConnected()) {
            label.setText("Retrieving data from server");
            client.getGroups(new ResponseHandler() {
                @Override
                public void handleResponse(Response response) {
                    if(response.isSuccess()) {
                        ArrayList<GroupData> groups = (ArrayList < GroupData >) response.getData();
                        if(groups.isEmpty()) {
                            label.setText("No groups joined yet");
                        } else {
                            table.removeActor(label);
                            client.getPlayerData(new ResponseHandler() {
                                @Override
                                public void handleResponse(Response response) {
                                    playerData = (PlayerData)response.getData();
                                }
                            });

                            for(final GroupData group : groups) {
                                table.row();
                                if(playerData.getId().equals(group.getOwner())) {
                                    table.add(new Label(Integer.toString(group.getGroupId()), labelStyle));
                                    table.add(new Label(group.getName(), labelStyle));
                                } else {
                                    table.add(new Label(group.getName(), labelStyle)).colspan(2);
                                }
                                TextButton button = new TextButton("leave", buttonStyle);
                                button.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        Input.TextInputListener listener = new Input.TextInputListener() {
                                            @Override
                                            public void input(String text) {
                                                if(text.toLowerCase().equals("yes")) {
                                                    client.leaveGroup(group, new ResponseHandler() {
                                                        @Override
                                                        public void handleResponse(Response response) {
                                                            game.setScreen(GroupOverviewScreen.class);
                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void canceled() {

                                            }
                                        };
                                        Gdx.input.getTextInput(listener, "Are you sure?", "", "type 'yes' to confirm");
                                    }
                                });
                                table.add(button);
                            }
                        }

                    }
                }
            });
        } else {
            label.setText("Not connected to the remote server");
        }
        return table;
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
