package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.sem.ssm2.Game;
import com.sem.ssm2.stroll.Stroll;
import com.sem.ssm2.util.AccelerationState;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class StrollScreen extends GameScreen {
    public StrollScreen(Game game) {
        super(game);
    }


    protected SpriteBatch batch;
    protected TextureRegion world;
    protected float rotation = 0;
    protected AccelerationState accelerationState;
    TextureRegion[] animationFrames;
    Animation animation;
    float elapsedTime;
    protected boolean isMoving = false;
    protected Label timerLabel;
    protected int remainingTime = 60;
//    protected int remainingTime = 300;
    protected Stage stage;
    protected boolean isActive = false;
    protected Random random = new Random();
    protected int timeSinceLastEvent;
    protected Stroll stroll;

    Timer timer;

    @Override
    public Class<? extends Screen> previousScreen() {
        return MainMenu.class;
    }

    @Override
    public void loadAssets() {
        assets.load("images/world.png", Texture.class);
        assets.load("images/walking_guy.png", Texture.class);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 1;
        int grayScale = 50;
        parameter.borderColor = new Color(grayScale/255f, grayScale/255f, grayScale/255f, 1);
        parameter.size = 120;
        assets.generateFont("strollTimerFont", "fonts/Blenda Script.otf", parameter);
    }

    @Override
    public void show() {
        stroll = game.getStroll();
        isActive = true;
        timeSinceLastEvent = 0;
        elapsedTime = 0f;
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        batch = new SpriteBatch();
        world = new TextureRegion(assets.get("images/world.png", Texture.class));
        TextureRegion[][] temp = TextureRegion.split(assets.get("images/walking_guy.png", Texture.class), 256, 256);
        animationFrames = new TextureRegion[4];
        int index = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                animationFrames[index++] = temp[j][i];
            }
        }

        animation = new Animation(1/4f, animationFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        setWalkingState(game.getAccelerationStatus().getAccelerationState());

        game.getAccelerationStatus().getSubject().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                AccelerationState accelerationState = (AccelerationState) arg;
                setWalkingState(accelerationState);
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(assets.get("strollTimerFont", BitmapFont.class), Color.WHITE);

        timerLabel = new Label(formattedTime(remainingTime), labelStyle);

        if(timer == null) {
            timer = new Timer();
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    if (--remainingTime > 0) {
                        timerLabel.setText(formattedTime(remainingTime));
                        if (isActive) {
                            System.out.println("active");
                            if (random.nextFloat() < (accelerationState.getEventChance() * timeSinceLastEvent / 60f)) {
                                game.setScreen(stroll.getRandomEventScreen());
                            } else {
                                timeSinceLastEvent++;
                            }
                        }
                    } else {
                        if(isActive) {
                            timer.stop();
                            timer = null;
                            game.setScreen(RewardScreen.class);
                        }
                    }
                }
            }, 1, 1);
        }

        Table table = new Table();
        table.setFillParent(true);
        table.add(timerLabel).top().padTop(40 * assets.getRatio());
        table.row();
        table.add().fill().expand();
        table.row();

        stage.addActor(table);
    }

    public void setWalkingState(AccelerationState accelerationState) {
        System.out.println(accelerationState.toString() + " -> " + accelerationState.getRotationSpeed());
        this.accelerationState = accelerationState;
        isMoving = accelerationState.isMoving();
    }

    public String formattedTime(int time) {
        int minutes = (int)(time / 60f);
        int seconds = time % 60;
        if(seconds < 10)
            return "" + minutes + ":0" + seconds;
        else
            return "" + minutes + ":" + seconds;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(59 / 255f, 179 / 255f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += delta;

        batch.begin();
        float width = Gdx.graphics.getWidth();
        batch.draw(world,
                0,
                -0.7f * width,
                width / 2f,
                width / 2f,
                width,
                width,
                1,
                1,
                rotation);
        if(isMoving) {
            batch.draw(animation.getKeyFrame(elapsedTime),
                    Gdx.graphics.getWidth() / 2f - 128 * assets.getRatio(),
                    270 * assets.getRatio(),
                    0,
                    0,
                    256 * assets.getRatio(),
                    256 * assets.getRatio(),
                    1,
                    1,
                    0);
        } else {
            batch.draw(animation.getKeyFrame(0),
                    Gdx.graphics.getWidth() / 2f - 128 * assets.getRatio(),
                    270 * assets.getRatio(),
                    0,
                    0,
                    256 * assets.getRatio(),
                    256 * assets.getRatio(),
                    1,
                    1,
                    0);
        }
//        batch.draw(animation.getKeyFrame(elapsedTime),
//                Gdx.graphics.getWidth() / 2f - 128, 120 * assets.getRatio());

        rotation += accelerationState.getRotationSpeed();
        batch.end();
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
        isActive = false;
    }

    @Override
    public void unloadAssets() {

    }

    @Override
    public void dispose() {

    }
}
