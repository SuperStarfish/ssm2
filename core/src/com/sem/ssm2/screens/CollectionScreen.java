package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.sem.ssm2.Game;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.util.CollectibleDrawer;
import com.sem.ssm2.util.SwipeDetector;

import java.util.Random;

public class CollectionScreen extends GameScreen {

    public CollectionScreen(Game game) {
        super(game);
    }

    Stage stage;

    String[] tabs = {"My collection", "Groups", "Manage groups"};
    int index = 0;

    TextButton backButton;
    Label indexLabel;

    Table table;
    Table list;
    List<String> groups;
    ScrollPane body;
    Cell<ScrollPane> cell;

    CollectibleDrawer collectibleDrawer;

    SwipeDetector swiper = new SwipeDetector(
            new SwipeDetector.DirectionListener() {
        @Override
        public void onLeft() {
            if(index < tabs.length - 1) {
                index++;
                setBody();
            }
        }

        @Override
        public void onRight() {
            if(index > 0) {
                index--;
                setBody();
            }
        }

        @Override
        public void onUp() {
            System.out.println("Up");
        }

        @Override
        public void onDown() {
            System.out.println("Down");
        }
    });

    public void setBody() {
        indexLabel.setText(tabs[index]);
        if(index == 0) {
            cell.setActor(new ScrollPane(list));
        } else if (index == 1) {
            cell.setActor(new ScrollPane(groups));
        }
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
    }

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        assets.generateFont("regular", "fonts/NotoSans-Regular.ttf", parameter);
        assets.generateFont("white_buttonFont", "fonts/Blenda Script.otf", parameter);
        assets.load("images/FishA.png", Texture.class);
        assets.load("images/FishB.png", Texture.class);
        assets.load("images/FishC.png", Texture.class);
    }

    @Override
    public void show() {
        stage = new Stage();
        inputMultiplexer.addProcessor(swiper);
        inputMultiplexer.addProcessor(stage);
        collectibleDrawer = new CollectibleDrawer(assets);

        Pixmap pixmap = new Pixmap(4,4, Pixmap.Format.RGBA8888);

        pixmap.setColor(new Color(59 / 255f, 85/255f, 1, 1));
        pixmap.fill();

        Texture headerBackground = new Texture(pixmap);

        pixmap.setColor(new Color(59 / 255f, 141/255f, 1, 1));
        pixmap.fill();

        Texture subHeaderBackground = new Texture(pixmap);

        pixmap.setColor(new Color(1, 1, 1, 1));
        pixmap.fill();
        Texture white = new Texture(pixmap);

        pixmap.dispose();

        table = new Table();
        stage.addActor(table);
        stage.setDebugAll(false);
        table.setFillParent(true);

        Table header = new Table();
        header.setBackground(new TextureRegionDrawable(new TextureRegion(headerBackground)));
        table.add(header).fillX().expandX();
        table.row();

        Table subHeader = new Table();
        subHeader.setBackground(new TextureRegionDrawable(new TextureRegion(subHeaderBackground)));
        table.add(subHeader).fillX().expandX();
        table.row();

//        Table body = new Table();
//        table.add(body).expand();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                assets.get("white_buttonFont", BitmapFont.class)
        );

        final Label.LabelStyle labelStyle = new Label.LabelStyle(assets.get("regular", BitmapFont.class), Color.WHITE);

        indexLabel = new Label(tabs[index], labelStyle);

        backButton = new TextButton("< Back", buttonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(MainMenu.class);
            }
        });

        header.add(backButton).pad(0,10,0,10);
        header.add(indexLabel).pad(0, 5, 0, 5).expandX();

        subHeader.add(new Label("Sort on:", labelStyle)).align(Align.left);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable()
        );

        List.ListStyle listStyle = new List.ListStyle(
                new BitmapFont(),
                Color.FOREST,
                Color.CORAL,
                new BaseDrawable()
        );

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(
                assets.get("regular", BitmapFont.class),
                Color.ORANGE,
                new TextureRegionDrawable(new TextureRegion(white)),
                scrollPaneStyle,
                listStyle
        );

        SelectBox<String> selectBox = new SelectBox<String>(selectBoxStyle);
        String[] items = {"test1", "test2", "test3"};
        selectBox.setItems(items);
        subHeader.add(selectBox);

        List.ListStyle listStyle1 = new List.ListStyle(
                assets.get("regular", BitmapFont.class),
                Color.CHARTREUSE, Color.BLACK, new BaseDrawable()
        );

        Pixmap pixmap1 = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        pixmap1.setColor(Color.CHARTREUSE);

        Random random = new Random();

        list = new Table();

        client.getLocalCollection(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                Collection collection = (Collection)response.getData();

                for(Collectible collectible : collection) {
                    Sprite sprite = collectibleDrawer.drawCollectible(collectible);
                    System.out.println(sprite.getWidth() + ", " + sprite.getHeight());
                    sprite.setSize(
                            sprite.getTexture().getWidth() / 1.2f * assets.getRatio(),
                            sprite.getTexture().getHeight() / 1.2f * assets.getRatio()
                    );
                    System.out.println(sprite.getHeight());
                    list.add(new Image(new SpriteDrawable(sprite)));
                    list.add(new Label(collectible.getOwnerId(), labelStyle)).expandX();
                    list.add(new Label(">", labelStyle)).pad(10);
                    list.row();
                }
            }
        });

        for(int i = 1; i <= 30; i++) {
            pixmap1.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
            pixmap1.fillCircle(128, 128, 50);
            Sprite sprite = new Sprite(new Texture(pixmap1));
            sprite.setSize(128 * assets.getRatio(), 128 * assets.getRatio());
            Image image = new Image(new SpriteDrawable(sprite));
            Label label = new Label("Item " + i, labelStyle);
            Label opti = new Label(">", labelStyle);
            list.add(image).pad(10);
            list.add(label).expandX();
            list.add(opti).pad(10);
            list.row();
        }

        pixmap1.dispose();

        body = new ScrollPane(list);
        cell = table.add(body).minWidth(Gdx.graphics.getWidth()).maxWidth(Gdx.graphics.getWidth());

        groups = new List<String>(listStyle1);
        String[] coll = {"Group 1",
                "Group 2",
                "Group 3",
                "Group 4",
                "Group 5",
                "Group 6",
                "Group 7",
                "Group 8",
                "Group 9",
                "Group 10",
                "Group 11",
                "Group 12",
                "Group 13",
                "Group 14",
                "Group 15",
                "Group 16",
                "Group 17",
                "Group 18"};
        groups.setItems(coll);

        pixmap = new Pixmap(9,9, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        pixmap.setColor(Color.YELLOW);
        int size = pixmap.getWidth() - 1;
        pixmap.drawLine(0, 0, 0, size);
        pixmap.drawLine(size, 0, size, size);
        pixmap.drawLine(0, 0, size, 0);
        pixmap.drawLine(0, size, size, size);

        NinePatch ninePatch = new NinePatch(new Texture(pixmap), 3, 3, 3, 3);

        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(ninePatch);
        ninePatchDrawable.setLeftWidth(15);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                new NinePatchDrawable(ninePatch),
                ninePatchDrawable,
                new BaseDrawable(),
                assets.get("white_buttonFont", BitmapFont.class)
        );


        subHeader.add(new TextButton("Test", style));
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
        inputMultiplexer.removeProcessor(swiper);
        inputMultiplexer.removeProcessor(stage);
        stage.dispose();
    }

    @Override
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}

