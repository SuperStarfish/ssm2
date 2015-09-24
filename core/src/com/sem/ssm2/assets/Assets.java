package com.sem.ssm2.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets extends AssetManager {
    private static Assets instance;

    private final float DEV_HEIGHT = 1920f;
    private float ratio;


    private Assets() {
        ratio = Gdx.graphics.getHeight() / DEV_HEIGHT;
    }

    public static synchronized Assets getInstance() {
        if(instance == null) {
            instance = new Assets();
        }
        return instance;
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
    }

    public float getRatio() {
        return ratio;
    }
}
