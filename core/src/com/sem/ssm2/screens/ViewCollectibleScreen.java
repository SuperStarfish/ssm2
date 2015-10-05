package com.sem.ssm2.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.structures.groups.GroupData;
import com.sem.ssm2.util.CollectibleDrawer;

import java.util.ArrayList;

public class ViewCollectibleScreen extends BaseMenuScreen {

    protected Collectible collectible;
    protected GroupData[] groups;
    protected String[] list;
    protected CollectibleDrawer collectibleDrawer;
    protected Table table;

    public ViewCollectibleScreen(Game game) {
        super(game);
    }


    @Override
    public String getScreenName() {
        return "Collectible";
    }

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return null;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return null;
    }

    @Override
    void extraAssets() {
        assets.load("images/FishA.png", Texture.class);
        assets.load("images/FishB.png", Texture.class);
        assets.load("images/FishC.png", Texture.class);
    }

    @Override
    protected Actor createSubHeader() {
        return null;
    }

    @Override
    protected WidgetGroup createBody() {
        table = new Table();
        collectibleDrawer = new CollectibleDrawer(assets);
        collectible = game.getCollectible();

        table.add(new Image(new SpriteDrawable(collectibleDrawer.drawCollectible(collectible))));
        table.row();
        table.add(new Label("Type: " + collectible.getClass().getSimpleName(), labelStyle));
        table.row();
        table.add(new Label("Rarity: " + collectible.getRarity(), labelStyle));
        table.row();

        client.getGroups(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
            if(response.isSuccess()) {
                ArrayList<GroupData> list = (ArrayList<GroupData>) response.getData();

                    List.ListStyle listStyle = new List.ListStyle(
                            assets.get("white_buttonFont", BitmapFont.class),
                            Color.TEAL,
                            Color.CORAL,
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.WHITE)))
                    );

                    ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.LIME))),
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.FOREST))),
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.MAGENTA))),
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.PURPLE))),
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.SLATE)))
                    );

                    SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(
                            assets.get("white_buttonFont", BitmapFont.class),
                            Color.SALMON,
                            new TextureRegionDrawable(new TextureRegion(assets.generateTexture(Color.BROWN))),
                            scrollPaneStyle,
                            listStyle
                    );

                    final SelectBox<GroupData> selectBox = new SelectBox<>(selectBoxStyle);
                    selectBox.setItems(list.toArray(new GroupData[list.size()]));

                    table.add(selectBox);
                    table.row();
                    TextButton textButton = new TextButton("Send to group", buttonStyle);;
                    textButton.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            client.sendCollectible(collectible, selectBox.getSelected().getGroupId(), new ResponseHandler(){
                                @Override
                                public void handleResponse(Response response) {
                                    game.setScreen(CollectionScreen.class);
                                }
                            });
                        }
                    });
                    table.add(textButton);


            }
            }
        });

        return table;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return CollectionScreen.class;
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
