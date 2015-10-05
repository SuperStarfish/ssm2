package com.sem.ssm2.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.structures.collection.collectibles.comparators.HueComparator;
import com.sem.ssm2.structures.collection.collectibles.comparators.RarityComparator;
import com.sem.ssm2.util.CollectibleDrawer;

import java.util.Comparator;

public class CollectionScreen extends BaseMenuScreen {

    public CollectionScreen(Game game) {
        super(game);
    }

    protected Table body;
    protected CollectibleDrawer collectibleDrawer;
    protected Sprite menuButton;

    protected Comparator<Collectible> collectibleComparator;
    protected SelectBox<Comparator<Collectible>> collectibleSortSelectBox;

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return GroupOverviewScreen.class;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return null;
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
    }

    @Override
    void extraAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        assets.generateFont("regular", "fonts/NotoSans-Regular.ttf", parameter);
        assets.generateFont("white_buttonFont", "fonts/Blenda Script.otf", parameter);
        assets.load("images/FishA.png", Texture.class);
        assets.load("images/FishB.png", Texture.class);
        assets.load("images/FishC.png", Texture.class);
    }

    @Override
    protected Actor createSubHeader() {
        return createCollectibleComparatorSelectBox();
    }

    @Override
    protected WidgetGroup createBody() {
        body = new Table();
        fillBody();
        return new ScrollPane(body);
    }

    public void fillBody(){
        body.clear();
        collectibleDrawer = new CollectibleDrawer(assets);

        if(client.isRemoteConnected()) {
            client.synchronizeLocalCollection();
        }

        menuButton = new Sprite(assets.get("images/button.png", Texture.class));
        menuButton.setSize(
                menuButton.getWidth() * assets.getRatio(), menuButton.getHeight() * assets.getRatio());
        SpriteDrawable spriteDrawable = new SpriteDrawable(menuButton);

        final TextButton.TextButtonStyle clickableButton = new TextButton.TextButtonStyle(
                spriteDrawable,
                spriteDrawable,
                spriteDrawable,
                assets.get("white_buttonFont", BitmapFont.class)
        );

        client.getLocalCollection(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                Collection collection = (Collection) response.getData();

                for (final Collectible collectible : collection.sort(collectibleComparator)) {
                    Sprite sprite = collectibleDrawer.drawCollectible(collectible);
                    sprite.setSize(
                            sprite.getTexture().getWidth() / 1.2f * assets.getRatio(),
                            sprite.getTexture().getHeight() / 1.2f * assets.getRatio()
                    );
                    body.add(new Image(new SpriteDrawable(sprite)));
                    body.add(new Label("" + collectible.getClass().getSimpleName(), labelStyle)).expandX();
                    TextButton textButton = new TextButton("Info", assets.generateWoodenTextButtonStyle(.6f, .8f));
                    textButton.setSize(sprite.getHeight(), sprite.getHeight());
                    textButton.addListener(new ClickListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            game.setCollectible(collectible);
                            game.setScreen(ViewCollectibleScreen.class);
                            return super.touchDown(event, x, y, pointer, button);
                        }
                    });
                    body.add(textButton);
                    body.row();
                }
            }
        });
    }

    @Override
    public String getScreenName() {
        return "My Collection";
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

    public SelectBox<Comparator<Collectible>> createCollectibleComparatorSelectBox() {

        collectibleComparator = new RarityComparator();
        collectibleSortSelectBox = new SelectBox<Comparator<Collectible>>(assets.generateSelectBoxStyle(1,.8f));
        collectibleSortSelectBox.setItems(collectibleComparator, new HueComparator());
        collectibleSortSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("CollectionScreen sort", "Selected sort: " + collectibleSortSelectBox.getSelected());
                collectibleComparator = collectibleSortSelectBox.getSelected();
                fillBody();
            }
        });

        return collectibleSortSelectBox;
    }
}
