package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.sem.ssm2.Game;
import com.sem.ssm2.screens.aquarium.Fish;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.util.CollectibleDrawer;

import java.util.ArrayList;
import java.util.Random;

public class MainMenu extends GameScreen {

    SpriteBatch batch;
    Texture background, rock1, rock2, kelp;
    TextureRegion region;
    float rotation = 0, addition = 0.008f;
    Stage stage;
    protected PlayerData playerdata;
    Timer timer;
    int remainingTime;
    TextButton button;
    CollectibleDrawer collectibleDrawer;

    ArrayList<Fish> allFish;

    TextButton.TextButtonStyle buttonStyle;


    int minTimeElapsed = 3600;

    public MainMenu(Game game) {
        super(game);
    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return null;
    }

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 124;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        assets.generateFont("titleFont", "fonts/Blenda Script.otf", parameter);
        parameter.color = new Color(71 / 255f, 37 / 255f, 2 / 255f, 1);
        parameter.size = 80;
        parameter.borderWidth = 0;
        assets.generateFont("brown_buttonFont", "fonts/Blenda Script.otf", parameter);
        parameter.color = new Color(45/ 255f, 45/ 255f, 45/ 255f, 1);
        assets.generateFont("grey_buttonFont", "fonts/Blenda Script.otf", parameter);
        parameter.color = Color.WHITE;
        assets.generateFont("white_buttonFont", "fonts/Blenda Script.otf", parameter);
        assets.load("images/lightrays.png", Texture.class);
        assets.load("images/button.png", Texture.class);
        assets.load("images/button_disabled.png", Texture.class);
        assets.load("images/button_pressed.png", Texture.class);
        assets.load("images/rock1.png", Texture.class);
        assets.load("images/rock2.png", Texture.class);
        assets.load("images/kelp.png", Texture.class);
        assets.load("images/FishA.png", Texture.class);
        assets.load("images/FishB.png", Texture.class);
        assets.load("images/FishC.png", Texture.class);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        allFish = new ArrayList<>();
        collectibleDrawer = new CollectibleDrawer(assets);
        background = assets.get("images/lightrays.png", Texture.class);
        rock1 = assets.get("images/rock1.png", Texture.class);
        rock2 = assets.get("images/rock2.png", Texture.class);
        kelp = assets.get("images/kelp.png", Texture.class);
        region = new TextureRegion(background);
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        createUI();
    }

    public void createUI() {
        client.getLocalCollection(true, 5, new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                Collection collection = (Collection)response.getData();
                if(collection != null) {
                    for(Collectible collectible : collection) {
                        Sprite sprite = collectibleDrawer.drawCollectible(collectible);
                        sprite.setSize(
                                sprite.getTexture().getWidth() / 1.5f * assets.getRatio(),
                                sprite.getTexture().getHeight() / 1.5f * assets.getRatio()
                        );
                        Fish image = new Fish(new SpriteDrawable(sprite));
                        allFish.add(image);
                        stage.addActor(image);
                    }
                }
            }
        });

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

        TextButton.TextButtonStyle disabledButtonStyle = new TextButton.TextButtonStyle(
                new SpriteDrawable(disabled),
                new SpriteDrawable(disabled),
                new SpriteDrawable(disabled),
                assets.get("grey_buttonFont", BitmapFont.class)
        );

        Label.LabelStyle labelStyle = new Label.LabelStyle(
                assets.get("titleFont", BitmapFont.class),
                Color.YELLOW
        );

        Label label = new Label("Super Starfish\nMania", labelStyle);

        client.getPlayerData(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                playerdata = (PlayerData) response.getData();
            }
        });

        remainingTime = minTimeElapsed - playerdata.getElapsedTime();

        if(remainingTime < 0) {
            button = new TextButton("Stroll", buttonStyle);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    client.updateStrollTime(System.currentTimeMillis(), new ResponseHandler(){
                        @Override
                        public void handleResponse(Response response) {
                            game.startStroll();
                            game.setScreen(StrollScreen.class);
                        }
                    });
                }
            });
        } else {
            button = new TextButton(formattedTime(remainingTime), disabledButtonStyle);
            button.setDisabled(true);
            if(timer == null) {
                timer = new Timer();
                timer.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        if(--remainingTime < 0) {
                            timer.stop();
                            button = new TextButton("Stroll", buttonStyle);
                            button.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    game.setScreen(StrollScreen.class);
                                }
                            });
                        } else {
                            button.setText(formattedTime(remainingTime));
                        }
                    }
                }, 0, 1);
            }
        }

        TextButton collectibleButton = new TextButton("Collection", buttonStyle);
        collectibleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(CollectionScreen.class);
            }
        });

        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(SettingsScreen.class);
            }
        });

        TextureRegion textureRegion = new TextureRegion(assets.generateTexture(Color.BLACK));

        textureRegion.setRegionWidth(2);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(
                assets.get("white_buttonFont", BitmapFont.class),
                new Color(71 / 255f, 37 / 255f, 2 / 255f, 1),
                new TextureRegionDrawable(textureRegion),
                new SpriteDrawable(new Sprite(assets.generateTexture(new Color(0,0,0,0.3f)))),
                new BaseDrawable()
        );

        TextField test = new TextField("192.168.254.154", textFieldStyle);
        test.setAlignment(Align.center);

        Table extraTable = new Table();
        extraTable.add(test).padLeft(18 * assets.getRatio())
                .padRight(18 * assets.getRatio()).width(430 * assets.getRatio());
        extraTable.setBackground(new SpriteDrawable(sprite));

        Table table = new Table();
        table.setFillParent(true);
        table.add(label).padTop(100 * assets.getRatio());
        table.row();
        table.add(button);
        table.row();
        table.add(collectibleButton);
        table.row();
        table.add(settingsButton);
        table.row();
        table.add(extraTable);
        table.row();
        table.add().fillY().expandY();
        stage.addActor(table);
    }

    public String formattedTime(int time) {
        if(time < 0)
            time = 0;
        int minutes = (int)(time / 60f);
        int seconds = time % 60;
        if(seconds < 10)
            return "" + minutes + ":0" + seconds;
        else
            return "" + minutes + ":" + seconds;
    }

    @Override
    public void render(float delta) {
        for(Fish fish : allFish) {
            fish.swim();
        }
        renderBackground();
        stage.act();
        stage.draw();
    }

    public void renderBackground() {
        if(rotation > 5f || rotation < -5f) {
            addition = 0 - addition;
        }
        rotation += addition;

        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(rock2,
                10 * assets.getRatio(),
                0,
                rock2.getWidth() * assets.getRatio(),
                rock2.getHeight() * assets.getRatio()
        );
        batch.draw(kelp,
                630 * assets.getRatio(),
                0,
                kelp.getWidth() * assets.getRatio(),
                kelp.getHeight() * assets.getRatio()
        );
        batch.draw(rock1,
                700 * assets.getRatio(),
                0,
                rock1.getWidth() * assets.getRatio(),
                rock1.getHeight() * assets.getRatio()
                );
        batch.draw(region,
                -167 * assets.getRatio(),
                -85 * assets.getRatio(),
                0,
                Gdx.graphics.getHeight(),
                background.getWidth() * assets.getRatio(),
                background.getHeight() * assets.getRatio(),
                1,
                1,
                rotation);
        batch.end();
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