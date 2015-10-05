package com.sem.ssm2.screens;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sem.ssm2.Game;
import com.sem.ssm2.client.Client;
import com.sem.ssm2.util.TextFieldTable;

public class SettingsScreen extends BaseMenuScreen {

    protected Label successLabel;

    public SettingsScreen(Game game) {
        super(game);
    }

    protected TextFieldTable ipField, portField;

    @Override
    public String getScreenName() {
        return "Set server config";
    }

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return ChangeUsernameScreen.class;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return null;
    }

    @Override
    void extraAssets() {

    }

    @Override
    protected Actor createSubHeader() {
        return null;
    }

    @Override
    protected WidgetGroup createBody() {

        ipField = assets.generateTextField(client.getIPSettings());
        successLabel = new Label("", labelStyle);
        portField = assets.generateTextField(Integer.toString(client.getPortSettings()));

        ipField.getInnerTextField().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                successLabel.setText("");
            }
        });
        portField.getInnerTextField().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                successLabel.setText("");
            }
        });

        TextButton saveButton = assets.generateSimpleTextButton("Save");
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String ip = ipField.getInnerTextField().getText().trim();
                int port = -1;
                try {
                    port = Integer.parseInt(portField.getInnerTextField().getText().trim());
                } catch (NumberFormatException e) {
                    successLabel.setText("Invalid port");
                }

                if(Client.isValidIP(ip)) {
                    if(Client.isValidPort(port)){
                        client.setRemoteIP(ip);
                        client.setRemotePort(port);
                        successLabel.setText("Success!");
                    } else {
                        successLabel.setText("Invalid port");
                    }
                } else {
                    successLabel.setText("Invalid IP");
                }
            }
        });

        Table table = new Table();
        table.add(new Label("Enter an IP:", labelStyle)).padTop(100 * assets.getRatio());
        table.row();
        table.add(ipField);
        table.row();
        table.add(new Label("Enter a Port:", labelStyle));
        table.row();
        table.add(portField);
        table.row();
        table.add(saveButton);
        table.row();
        table.add(successLabel);
        table.row();
        table.add().fillY().expandY();
        return table;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
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
