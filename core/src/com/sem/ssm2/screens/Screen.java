package com.sem.ssm2.screens;


public interface Screen {

    void loadAssets();

    void show ();

    void render (float delta);

    void resize (int width, int height);

    void pause ();

    void resume ();

    void hide ();

//    Class<? extends Screen> previousScreen();

    void unloadAssets();

    void dispose ();
}
