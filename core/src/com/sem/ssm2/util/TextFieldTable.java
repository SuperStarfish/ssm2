package com.sem.ssm2.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.sem.ssm2.assets.Assets;

public class TextFieldTable extends Table {

    protected Assets assets;
    protected TextField textField;

    public TextFieldTable(Assets assets, String text) {
        this.assets = assets;
        textField = new TextField(text, assets.getTextFieldStyle());
        textField.setAlignment(Align.center);

        Sprite sprite = new Sprite(assets.get("images/button.png", Texture.class));
        sprite.setSize(
                sprite.getWidth() * assets.getRatio(),
                sprite.getHeight() * assets.getRatio()
        );

        this.add(textField).padLeft(18 * assets.getRatio())
                .padRight(18 * assets.getRatio()).width(430 * assets.getRatio());
        this.setBackground(new SpriteDrawable(sprite));
    }

    public TextField getInnerTextField() {
        return textField;
    }


}
