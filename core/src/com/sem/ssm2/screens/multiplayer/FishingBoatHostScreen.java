package com.sem.ssm2.screens.multiplayer;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.sem.ssm2.Game;
import com.sem.ssm2.multiplayer.Host;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.screens.StrollScreen;

public class FishingBoatHostScreen extends FishingBoatEvent {

    public FishingBoatHostScreen(Game game, Host host) {
        super(game, host);
        this.host = host;
    }

    @Override
    protected WidgetGroup createBody() {

        for(int i = 0; i < allFish.length; i++) {
            SmallFish smallFish = new SmallFish(assets, i);
            allFish[i] = smallFish;
            stage.addActor(smallFish);
        }

        return null;

    }

    @Override
    public Class<? extends Screen> previousScreen() {
        return StrollScreen.class;
    }

    @Override
    public void loadAssets() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        updateFishes();
        super.render(delta);
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
