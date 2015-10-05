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

public class SettingsScreen extends BaseMenuScreen {

    public SettingsScreen(Game game) {
        super(game);
    }

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

        Table extraTable = assets.generateTextField("192.168.0.1");

        Table extraTable2 = assets.generateTextField("56789");



        TextButton saveButton = assets.generateSimpleTextButton("Save");
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("STORE NEW IP ETC.");
            }
        });

        Table table = new Table();
        table.add(new Label("Enter an IP:", labelStyle)).padTop(100 * assets.getRatio());
        table.row();
        table.add(extraTable);
        table.row();
        table.add(new Label("Enter a Port:", labelStyle));
        table.row();
        table.add(extraTable2);
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
