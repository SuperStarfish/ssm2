package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.SimpleScreen;
import com.sem.ssm2.screens.StrollScreen;


public class MultiPlayerScreen extends SimpleScreen {

    protected TextButton.TextButtonStyle buttonStyle;

    public MultiPlayerScreen(Game game) {
        super(game);
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return StrollScreen.class;
    }

    @Override
    public void loadAssets() {

    }

    @Override
    protected WidgetGroup createBody() {
        Table table = new Table();
        Texture texture = assets.get("images/button.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        Sprite sprite2 = new Sprite(assets.get("images/button_pressed.png", Texture.class));
        sprite2.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        Sprite disabled = new Sprite(assets.get("images/button_disabled.png", Texture.class));
        disabled.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        buttonStyle = new TextButton.TextButtonStyle(
                new SpriteDrawable(sprite),
                new SpriteDrawable(sprite2),
                new SpriteDrawable(sprite2),
                assets.get("brown_buttonFont", BitmapFont.class)
        );

        TextButton hostButton = new TextButton("Host", buttonStyle);
        hostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(HostMultiPlayerScreen.class);
            }
        });

        TextButton joinButton = new TextButton("Join", buttonStyle);
        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(JoinMultiPlayerScreen.class);
            }
        });

        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen());
            }
        });

        table.setFillParent(true);
        table.add(hostButton);
        table.row();
        table.add(joinButton);
        table.row();
        table.add(backButton);
        table.row();
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
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}
