package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.stroll.Stroll;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.structures.collection.collectibles.CollectibleFactory;
import com.sem.ssm2.util.CollectibleDrawer;

public class RewardScreen extends GameScreen {

    protected Stage stage;
    protected Stroll stroll = game.getStroll();
    protected CollectibleDrawer collectibleDrawer;

    public RewardScreen(Game game) {
        super(game);
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
    }

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        assets.generateFont("rewardsFont", "fonts/NotoSans-Regular.ttf", parameter);
    }

    @Override
    public void show() {
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        collectibleDrawer = new CollectibleDrawer(assets);

        Table table = new Table();
        table.setFillParent(true);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        ScrollPane scrollPane = new ScrollPane(horizontalGroup);
        table.add(scrollPane);

        for(Collectible collectible : stroll.getStrollRewards()) {
            horizontalGroup.addActor(new Image(
                    new SpriteDrawable(collectibleDrawer.drawCollectible(collectible))
            ));
        }

        System.out.println(stroll.getStrollRewards());
        client.addLocalCollection(stroll.getStrollRewards(), null);
//
//        if(client.isRemoteConnected()){
//            System.out.println("");
//        }

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                assets.get("rewardsFont", BitmapFont.class)
        );

        table.row();
        TextButton textButton = new TextButton("Back", textButtonStyle);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen());
            }
        });
        table.add(textButton);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
