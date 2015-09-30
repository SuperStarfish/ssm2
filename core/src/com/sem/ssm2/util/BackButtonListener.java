package com.sem.ssm2.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.sem.ssm2.Game;

public class BackButtonListener implements InputProcessor {
    protected Game game;

    public BackButtonListener(Game game) {
        this.game = game;
    }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            game.setPreviousScreen();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
