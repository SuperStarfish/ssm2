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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.sem.ssm2.Game;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;

public class MainMenu extends GameScreen {

    SpriteBatch batch;
    Texture background;
    TextureRegion region;
    float rotation = 0, addition = 0.008f;
    Stage stage;

    public MainMenu(Game game) {
        super(game);
    }

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 124;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.color = new Color(71 / 255f, 37 / 255f, 2 / 255f, 1);
        assets.generateFont("titleFont", "fonts/Quicksand-BoldItalic.otf", parameter);
        parameter.size = 80;
        parameter.borderWidth = 0;
        assets.generateFont("brown_buttonFont", "fonts/Blenda Script.otf", parameter);
        assets.load("images/lightrays.png", Texture.class);
        assets.load("images/button.png", Texture.class);
        assets.load("images/button_pressed.png", Texture.class);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = assets.get("images/lightrays.png", Texture.class);
        region = new TextureRegion(background);
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);
        createUI();
        client.setIp("192.168.2.5");
        client.connectToRemoteServer();
        client.getPlayerData(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                System.out.println("test");
            }
        });
    }

    public void createUI() {
        VerticalGroup group = new VerticalGroup();

        Texture texture = assets.get("images/button.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * assets.getRatio(), texture.getHeight() * assets.getRatio());

        Texture texture2 = assets.get("images/button_pressed.png");
        Sprite sprite2 = new Sprite(texture2);
        sprite2.setSize(texture2.getWidth() * assets.getRatio(), texture2.getHeight() * assets.getRatio());

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(
                new SpriteDrawable(sprite),
                new SpriteDrawable(sprite2),
                new SpriteDrawable(sprite2),
                assets.get("brown_buttonFont", BitmapFont.class)
        );

        TextButton button = new TextButton("Stroll", buttonStyle);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Do something");
            }
        });

        TextButton collectibleButton = new TextButton("Collection", buttonStyle);
        collectibleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(CollectionScreen.class);
            }
        });

        group.addActor(button);
        group.addActor(collectibleButton);
        group.setFillParent(true);
        stage.addActor(group);
    }

    @Override
    public void render(float delta) {
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