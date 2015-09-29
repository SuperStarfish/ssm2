package com.sem.ssm2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.sem.ssm2.assets.Assets;
import com.sem.ssm2.client.Client;
import com.sem.ssm2.client.UserIDResolver;
import com.sem.ssm2.screens.LoadingScreen;
import com.sem.ssm2.screens.MainMenu;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.server.LocalStorageResolver;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.structures.collection.Collection;
import com.sem.ssm2.util.AccelerationStatus;
import com.sem.ssm2.util.NotificationController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Game extends com.badlogic.gdx.Game implements ApplicationListener {

    protected AccelerationStatus accelerationStatus;
    protected NotificationController notificationController;
    protected UserIDResolver userIDResolver;
    protected LocalStorageResolver localStorageResolver;

    public Game(AccelerationStatus accelerationStatus,
                NotificationController notificationController,
                UserIDResolver userIDResolver,
                LocalStorageResolver localStorageResolver){
        this.accelerationStatus = accelerationStatus;
        this.notificationController = notificationController;
        this.userIDResolver = userIDResolver;
        this.localStorageResolver = localStorageResolver;
    }

    protected Assets assets;
    protected Screen screen;
    protected HashMap<String, Screen> storedScreens;
    protected Screen queuedScreen;
    protected Client client;

    @Override
    public void create() {
        storedScreens = new HashMap<>();
        assets = new Assets();

        client = new Client(userIDResolver, localStorageResolver);
        client.setRemoteIP("82.169.19.191");
        client.setRemotePort(56789);
        client.connectToRemoteServer();

        Texture.setAssetManager(assets);
        Gdx.input.setInputProcessor(new InputMultiplexer());
        LoadingScreen loadingScreen = new LoadingScreen(this);
        loadingScreen.loadAssets();
        assets.finishLoading();
        storedScreens.put(LoadingScreen.class.getName(), loadingScreen);
        setScreen(MainMenu.class);

        client.getLocalCollection(new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                Collection collection = (Collection)response.getData();
                System.out.println(collection);
            }
        });

    }

    public void setScreen(Class<? extends Screen> newScreen) {
        if (storedScreens.containsKey(newScreen.getName())) {
            setScreen(storedScreens.get(newScreen.getName()));
        } else {
            createNewScreen(newScreen);
        }
    }

    protected void createNewScreen(Class<? extends Screen> newScreen) {
        try {
            Screen screenInstance = newScreen.getDeclaredConstructor(Game.class).newInstance(this);
            screenInstance.loadAssets();
            storedScreens.put(newScreen.getName(), screenInstance);
            if(assets.update()) {
                setScreen(screenInstance);
            } else {
                queuedScreen = screenInstance;
                setScreen(LoadingScreen.class);
            }
        } catch (NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setQueuedScreen() {
        if(queuedScreen != null) {
            setScreen(queuedScreen);
            queuedScreen = null;
        } else {
            setScreen(MainMenu.class);
        }
    }

    protected void setScreen(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void resize(int width, int height) {
        if(screen != null) screen.resize(width, height);
        System.out.println(height);
    }

    @Override
    public void render() {
        if(screen != null) screen.render(Gdx.graphics.getDeltaTime());
        for (Runnable toRunBeforeNextCycle : client.getPostRunnables()) {
            Gdx.app.postRunnable(toRunBeforeNextCycle);
        }
        client.resetPostRunnables();
    }

    @Override
    public void pause() {
        if(screen != null) screen.pause();
    }

    @Override
    public void resume() {
        if(screen != null) screen.resume();
    }

    @Override
    public void dispose() {
        assets.dispose();
//        for(Screen screen : storedScreens.values()) {
//            screen.dispose();
//        }
//        screen = null;
    }

    public Assets getAssets() {
        return assets;
    }

    public Client getClient() {
        return client;
    }

}
