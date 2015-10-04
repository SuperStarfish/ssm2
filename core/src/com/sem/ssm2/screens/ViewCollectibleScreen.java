package com.sem.ssm2.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.structures.groups.GroupData;

import java.util.ArrayList;

public class ViewCollectibleScreen extends BaseMenuScreen {

    protected Collectible collectible;
    protected int[] groups;

    public ViewCollectibleScreen(Game game) {
        super(game);
        collectible = game.getCollectible();
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
        assets.load(collectible.getImagePath(), Texture.class);
    }

    @Override
    protected Actor createSubHeader() {
        return null;
    }

    @Override
    protected WidgetGroup createBody() {
        final Table table = new Table();

        table.add(new Image(assets.get(collectible.getImagePath(), Texture.class)));
        table.row();
        table.add(new Label("Type: " + collectible.getClass().getSimpleName(), labelStyle));
        table.row();
        table.add(new Label("Rarity: " + collectible.getRarity(), labelStyle));
        table.row();

        client.getGroups(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {

                if(response.isSuccess()) {
                    ArrayList<GroupData> groupData = (ArrayList<GroupData>)response.getData();
                    System.out.println("SIze: " + groupData.size());
                    if(groupData.size() > 0) {
                        System.out.println("LARGER THAN 2");


                        String[] list = new String[groupData.size()];
                        groups = new int[groupData.size()];
                        for(int i = 0; i < groupData.size(); i++){
                            list[i] = groupData.get(i).getName();
                            groups[i] = groupData.get(i).getGroupId();
                        }

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

                        final SelectBox<String> selectBox = new SelectBox<>(selectBoxStyle);
                        selectBox.setItems(list);

                        table.add(selectBox);
                        table.row();
                        TextButton textButton = new TextButton("Send to group", buttonStyle);;
                        textButton.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                client.sendCollectible(collectible, groups[selectBox.getSelectedIndex()], new ResponseHandler(){
                                    @Override
                                    public void handleResponse(Response response) {
                                        game.setScreen(ViewCollectibleScreen.class);
                                    }
                                });
                            }
                        });
                        table.add(textButton);

                    }
                }
            }
        });

        return table;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return ViewCollectibleScreen.class;
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
