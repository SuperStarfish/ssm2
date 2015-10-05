package com.sem.ssm2.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sem.ssm2.Game;
import com.sem.ssm2.util.TextFieldTable;

public class ChangeUsernameScreen extends BaseMenuScreen {

    protected TextFieldTable textFieldTable;

    public ChangeUsernameScreen(Game game) {
        super(game);
    }

    @Override
    public String getScreenName() {
        return "Change username";
    }

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return null;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return SettingsScreen.class;
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
        textFieldTable = assets.generateTextField(client.getCurrentPlayerData().getUsername());

        TextButton saveButton = assets.generateSimpleTextButton("Save");
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String text = textFieldTable.getInnerTextField().getText().trim();
                if(!text.isEmpty()) {
                    client.getCurrentPlayerData().setUsername(text);
                    client.updatePlayerData();
                }
            }
        });

        Table table = new Table();
        table.add(new Label("Username:", labelStyle)).padTop(100 * assets.getRatio());
        table.row();
        table.add(textFieldTable);
        table.row();
        table.add(saveButton);
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
