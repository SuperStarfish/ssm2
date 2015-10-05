package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.sem.ssm2.Game;
import com.sem.ssm2.util.SwipeDetector;

public abstract class BaseMenuScreen extends GameScreen{

    protected Stage stage;
    protected TextButton backButton;
    private Table layout, header, subHeader;
    protected Container<Actor> body;
    protected Label screenTitle;

    protected Texture headerBackground, subHeaderBackground;

    protected TextButton.TextButtonStyle buttonStyle;
    protected Label.LabelStyle labelStyle;

    protected SpriteBatch batch;
    protected TextureRegion region;
    protected Texture background;
    protected static float rotation = 0, addition = 0.008f;

    protected SwipeDetector swipeDetector = new SwipeDetector(new SwipeDetector.DirectionListener() {
        @Override
        public void onLeft() {
            Class<? extends Screen> screen = swipeLeftScreen();
            if(screen != null)
                game.setScreen(screen);
        }

        @Override
        public void onRight() {
            Class<? extends Screen> screen = swipeRightScreen();
            if(screen != null)
                game.setScreen(screen);
        }

        @Override
        public void onUp() {

        }

        @Override
        public void onDown() {

        }
    });


    public BaseMenuScreen(Game game) {
        super(game);
    }

    public abstract String getScreenName();

    abstract Class<? extends Screen> swipeLeftScreen();

    abstract Class<? extends Screen> swipeRightScreen();

    @Override
    public void loadAssets() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        assets.generateFont("regular", "fonts/NotoSans-Regular.ttf", parameter);
        assets.generateFont("white_buttonFont", "fonts/Blenda Script.otf", parameter);
        extraAssets();
    }

    abstract void extraAssets();

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        inputMultiplexer.addProcessor(swipeDetector);
        inputMultiplexer.addProcessor(stage);

        createBackground();

        headerBackground = createHeaderBackground();
        subHeaderBackground = createSubHeaderBackground();

        createStyles();
        createUI();
    }

    private void createBackground() {
        background = assets.get("images/lightrays.png", Texture.class);
        region = new TextureRegion(background);
    }

    private void createStyles() {
        buttonStyle = new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                assets.get("white_buttonFont", BitmapFont.class)
        );

        labelStyle = new Label.LabelStyle(assets.get("regular", BitmapFont.class), Color.WHITE);

    }

    private void createUI() {
        layout = new Table();
        layout.setFillParent(true);
        stage.addActor(layout);

        header = new Table();
        header.setBackground(new TextureRegionDrawable(new TextureRegion(headerBackground)));
        layout.add(header).fillX().expandX();
        layout.row();

        subHeader = new Table();
        subHeader.setBackground(new TextureRegionDrawable(new TextureRegion(subHeaderBackground)));
        layout.add(subHeader).fillX().expandX();
        layout.row();

        body = new Container<>();
        layout.add(body).fill().expand();

        createHeader();

        subHeader.add(createSubHeader());

        body.align(Align.top);
        body.setActor(createBody());
        body.prefWidth(Gdx.graphics.getWidth()).maxWidth(Gdx.graphics.getWidth());
    }

    protected abstract Actor createSubHeader();

    protected abstract WidgetGroup createBody();

    private void createHeader() {
        screenTitle = new Label(getScreenName(), labelStyle);

        backButton = new TextButton("< Back", buttonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen());
            }
        });

        header.add(backButton).pad(0, 20 * assets.getRatio(), 0, 20 * assets.getRatio());
        header.add(screenTitle).pad(0, 5 * assets.getRatio(), 0, 5 * assets.getRatio()).expandX();
    }


    private Texture createHeaderBackground() {
        Pixmap pixmap = new Pixmap(4,4, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(59 / 255f, 85 / 255f, 1, .7f));
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createSubHeaderBackground() {
        Pixmap pixmap = new Pixmap(4,4, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(59 / 255f, 141/255f, 1, .7f));
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
        inputMultiplexer.removeProcessor(swipeDetector);
    }

    public Container<Actor> getBody() {
        return body;
    }
}
