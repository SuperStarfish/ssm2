package com.sem.ssm2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sem.ssm2.Game;

public class NewGroupScreen extends BaseMenuScreen {

    TextField.TextFieldStyle textFieldStyle;

    public NewGroupScreen(Game game) {
        super(game);
    }

    @Override
    Class<? extends Screen> swipeLeftScreen() {
        return null;
    }

    @Override
    Class<? extends Screen> swipeRightScreen() {
        return CollectionScreen.class;
    }

    @Override
    void extraAssets() {

    }

    @Override
    protected Actor createSubHeader() {
        return null;
    }

    @Override
    protected WidgetGroup createBody() {
        Table group = new Table();

        Pixmap pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixmap.setColor(Color.BLACK);
        pixmap.drawLine(0, 0, pixmap.getWidth() - 1, 0);
        pixmap.drawLine(0, 0, 0, pixmap.getHeight() - 1);
        pixmap.drawLine(pixmap.getWidth() - 1, pixmap.getHeight() - 1, 0, pixmap.getHeight() - 1);
        pixmap.drawLine(pixmap.getWidth() - 1, pixmap.getHeight() - 1, pixmap.getWidth() - 1, 0);

        Texture texture = new Texture(pixmap);
        NinePatch ninePatch = new NinePatch(texture,
                (int)((1/3f) * pixmap.getWidth()),
                (int)(pixmap.getWidth() - (1/3f) * pixmap.getWidth()),
                (int)(pixmap.getHeight() - (1/3f) * pixmap.getHeight()),
                (int)((1/3f) * pixmap.getHeight())
        );
        pixmap.dispose();

        pixmap = new Pixmap(
                1,1, Pixmap.Format.RGBA8888
        );

        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture texture1 = new Texture(pixmap);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();
        Texture texture2 = new Texture(pixmap);
        pixmap.dispose();



        textFieldStyle = new TextField.TextFieldStyle(
                assets.get("white_buttonFont", BitmapFont.class),
                Color.RED,
                new TextureRegionDrawable(new TextureRegion(texture1)),
                new TextureRegionDrawable(new TextureRegion(texture2)),
                new NinePatchDrawable(ninePatch)
        );


        pixmap = new Pixmap((int)(60 * assets.getRatio()), (int)(60 * assets.getRatio()), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(2, 2, pixmap.getWidth() - 4, pixmap.getHeight() - 4);
        TextureRegionDrawable uncheckedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.setColor(Color.ORANGE);
        pixmap.fillRectangle(2, 2, pixmap.getWidth() - 4, pixmap.getHeight() - 4);
        TextureRegionDrawable checkedDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(
                uncheckedDrawable,
                checkedDrawable,
                assets.get("white_buttonFont", BitmapFont.class),
                Color.TEAL
        );

        pixmap.dispose();


        group.add(new Label("Group name:", labelStyle)).colspan(2)
                .pad(0, Gdx.graphics.getWidth() * 0.15f, 0, Gdx.graphics.getWidth() * 0.15f).fillX().expandX();
        group.row();
        TextField textField = new TextField("text", textFieldStyle);
        group.add(textField).width(0.7f * Gdx.graphics.getWidth()).colspan(2);
        group.row();
        group.add(new Label("Public:", labelStyle))
                .padLeft(Gdx.graphics.getWidth() * 0.15f).fillX().expandX();
        group.add(new CheckBox("", checkBoxStyle)).padRight(Gdx.graphics.getWidth() * 0.15f);
        group.row();
        group.add(new TextButton("Create", buttonStyle)).colspan(2);
        group.row();
//
//        group.addActor(new Label("Group name:", labelStyle));
//        TextField textField = new TextField("text", textFieldStyle);
//        group.addActor(textField);
//        stage.setDebugAll(true);
//        group.addActor(new Label("Public group?", labelStyle));
//        group.addActor(new Label("Public group?", labelStyle));
//        group.addActor(new Label("Public group?", labelStyle));
//        group.addActor(new Label("Public group?", labelStyle));
        return group;
    }

    @Override
    public String getScreenName() {
        return "Create Group";
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
