package com.sem.ssm2.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sem.ssm2.util.TextFieldTable;

public class Assets extends AssetManager {

    private final float DEV_HEIGHT = 1920f;
    private float ratio;

    protected Texture white, black, blue, pink, yellow, green;
    protected Pixmap pixmap;

    boolean generateStyles = true;

    protected TextField.TextFieldStyle textFieldStyle;
    protected TextButton.TextButtonStyle simpleTextButtonStyle;
    protected Label.LabelStyle labelStyle;

    public Assets() {
        ratio = Gdx.graphics.getHeight() / DEV_HEIGHT;
        Texture.setAssetManager(this);
        pixmap = new Pixmap(8,8, Pixmap.Format.RGBA8888);
        white = generateTexture(Color.WHITE);
        black = generateTexture(Color.BLACK);
        blue = generateTexture(Color.BLUE);
        pink = generateTexture(Color.PINK);
        yellow = generateTexture(Color.YELLOW);
        green = generateTexture(Color.GREEN);

        loadDefaults();
    }

    protected void loadDefaults() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        generateFont("someFont", "fonts/NotoSans-Regular.ttf", parameter);
        parameter.color = new Color(71 / 255f, 37 / 255f, 2 / 255f, 1);
        parameter.size = 80;
        generateFont("brown_buttonFont", "fonts/Blenda Script.otf", parameter);
        parameter.size = 60;
        parameter.color = Color.WHITE;
        generateFont("white_buttonFont", "fonts/Blenda Script.otf", parameter);
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        generateFont("white_buttonFontBordered", "fonts/Blenda Script.otf", parameter);
        load("images/lightrays.png", Texture.class);
        load("images/button.png", Texture.class);
        load("images/button_disabled.png", Texture.class);
        load("images/button_pressed.png", Texture.class);
    }

    public Texture generateTexture(Color color) {
        pixmap.setColor(color);
        pixmap.fill();
        return new Texture(pixmap);
    }

    public void generateFont(String key, String fontFile) {
        generateFont(key, fontFile, new FreeTypeFontGenerator.FreeTypeFontParameter());
    }

    public void generateFont(String key, String fontFile, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal(fontFile)
        );

        parameter.size *= ratio;

        this.addAsset(key, BitmapFont.class, generator.generateFont(parameter));
        generator.dispose();

        parameter.size /= ratio;

    }

    public void generateStyles() {
        if(generateStyles) {
            generateStyles = false;

            simpleTextButtonStyle = generateSimpleTextButtonStyle();
            textFieldStyle = generateTextFieldStyle();
            labelStyle = generateLabelStyle();
        }
    }

    protected Label.LabelStyle generateLabelStyle() {
        return generateLabelStyle(Color.WHITE);
    }

    protected Label.LabelStyle generateLabelStyle(Color color) {
        return new Label.LabelStyle(
                get("white_buttonFont", BitmapFont.class),
                color
        );
    }

    public Label generateLabel(String text) {
        return generateLabel(text, null);
    }

    public Label generateLabel(String text, Color color) {
        if(color == null)
            return new Label(text, labelStyle);
        else
            return new Label(text, generateLabelStyle(color));
    }

    protected TextButton.TextButtonStyle generateSimpleTextButtonStyle() {
        return new TextButton.TextButtonStyle(
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                get("white_buttonFontBordered", BitmapFont.class)
        );
    }

    public TextButton.TextButtonStyle generateWoodenTextButtonStyle(float xScale, float yScale) {
        Texture texture = get("images/button.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * getRatio() * xScale, texture.getHeight() * getRatio() * yScale);

        Sprite sprite2 = new Sprite(get("images/button_pressed.png", Texture.class));
        sprite2.setSize(texture.getWidth() * getRatio() * xScale, texture.getHeight() * getRatio() * yScale);

        Sprite disabled = new Sprite(get("images/button_disabled.png", Texture.class));
        disabled.setSize(texture.getWidth() * getRatio() * xScale, texture.getHeight() * getRatio() * yScale);

        return new TextButton.TextButtonStyle(
                new SpriteDrawable(sprite),
                new SpriteDrawable(sprite2),
                new SpriteDrawable(sprite2),
                get("brown_buttonFont", BitmapFont.class)
        );
    }

    public SelectBox.SelectBoxStyle generateSelectBoxStyle(float xScale, float yScale) {
        Texture texture = get("images/button.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * getRatio() * xScale, texture.getHeight() * getRatio() * yScale);

        Sprite sprite2 = new Sprite(get("images/button_pressed.png", Texture.class));
        sprite2.setSize(texture.getWidth() * getRatio() * xScale, texture.getHeight() * getRatio() * yScale);

        Sprite disabled = new Sprite(get("images/button_disabled.png", Texture.class));
        disabled.setSize(texture.getWidth() * getRatio() * xScale, texture.getHeight() * getRatio() * yScale);

        List.ListStyle listStyle = new List.ListStyle(
                get("someFont", BitmapFont.class),
                Color.GRAY,
                Color.WHITE,
                new TextureRegionDrawable(new TextureRegion(generateTexture(new Color(0,0,0,.4f))))
        );

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(
                new TextureRegionDrawable(new TextureRegion(generateTexture(new Color(1,1,1,.7f)))),
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable(),
                new BaseDrawable()
        );

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(
                get("someFont", BitmapFont.class),
                Color.WHITE,
                new BaseDrawable(),
                scrollPaneStyle,
                listStyle
        );

        return selectBoxStyle;
    }

    public TextButton generateSimpleTextButton(String text) {
        return new TextButton(text, simpleTextButtonStyle);
    }

    protected TextField.TextFieldStyle generateTextFieldStyle() {
        System.out.println(ratio);
        Texture texture = get("images/button.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * ratio, texture.getHeight() * ratio);

        TextureRegion textureRegion = new TextureRegion(generateTexture(Color.BLACK));
        textureRegion.setRegionWidth(2);

        return new TextField.TextFieldStyle(
                get("white_buttonFont", BitmapFont.class),
                new Color(71 / 255f, 37 / 255f, 2 / 255f, 1),
                new TextureRegionDrawable(textureRegion),
                new SpriteDrawable(new Sprite(generateTexture(new Color(0,0,0,0.3f)))),
                new BaseDrawable()
        );
    }

    public TextField.TextFieldStyle getTextFieldStyle() {
        return textFieldStyle;
    }

    public TextFieldTable generateTextField(String initialText) {
        return new TextFieldTable(this, initialText);
    }

    public float getRatio() {
        return ratio;
    }
}
