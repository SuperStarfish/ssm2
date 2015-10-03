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
import com.sem.ssm2.structures.groups.GroupData;

import java.util.ArrayList;

public class SearchGroupScreen extends BaseMenuScreen {

    Table table;
    Label label;

    public SearchGroupScreen(Game game) {
        super(game);
    }

    @Override
    public String getScreenName() {
        return "Find Groups";
    }

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return null;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return GroupOverviewScreen.class;
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
        TextButton textButton = new TextButton("Manual join", textButtonStyle);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        int groupId = -1;
                        try {
                            groupId = Integer.parseInt(text.trim());
                        } catch (NumberFormatException e) {
                            //eat error
                        }
                        if(groupId != -1){
                            client.getGroupData(groupId, new ResponseHandler(){
                                @Override
                                public void handleResponse(Response response) {
                                    if(response.getData() != null) {
                                        final GroupData groupData = (GroupData)response.getData();
                                        if(!groupData.getPassword().isEmpty()) {
                                            Input.TextInputListener textInputListener = new Input.TextInputListener() {
                                                @Override
                                                public void input(String text) {
                                                    if(text.equals(groupData.getPassword())) {
                                                        client.joinGroup(groupData, new ResponseHandler() {
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
                                            Gdx.input.getTextInput(textInputListener, "Enter password", "", "password");
                                        }
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void canceled() {

                    }
                };
                Gdx.input.getTextInput(listener, "Enter Group number", "", "group number");
            }
        });
        return textButton;
    }

    @Override
    protected WidgetGroup createBody() {
        table = new Table();
        label = new Label("", labelStyle);
        if(client.isRemoteConnected()) {
            label.setText("Getting group data");
            client.getPublicGroups(new ResponseHandler() {
                @Override
                public void handleResponse(Response response) {
                    if(response.isSuccess()) {
                        ArrayList<GroupData> groups = (ArrayList<GroupData>)response.getData();
                        if(groups.isEmpty()) {
                            label.setText("No groups found");
                        } else {
                            table.removeActor(label);
                            for(final GroupData group : groups) {
                                table.row();
                                table.add(new Label(group.getName(), labelStyle));
                                TextButton textButton = new TextButton("Join", buttonStyle);
                                textButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        client.joinGroup(group, new ResponseHandler() {
                                            @Override
                                            public void handleResponse(Response response) {
                                                game.setScreen(GroupOverviewScreen.class);
                                            }
                                        });
                                    }
                                });
                                table.add(textButton);
                            }
                        }

                    } else {
                        label.setText("Failed to get data");
                    }
                }
            });
        } else {
            label.setText("Not connected to server");
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
