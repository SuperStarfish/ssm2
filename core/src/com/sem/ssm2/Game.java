package com.sem.ssm2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.sem.ssm2.assets.Assets;
import com.sem.ssm2.client.Client;
import com.sem.ssm2.client.UserIDResolver;
import com.sem.ssm2.multiplayer.Host;
import com.sem.ssm2.screens.LoadingScreen;
import com.sem.ssm2.screens.MainMenu;
import com.sem.ssm2.screens.Screen;
import com.sem.ssm2.server.LocalStorageResolver;
import com.sem.ssm2.server.database.Response;
import com.sem.ssm2.server.database.ResponseHandler;
import com.sem.ssm2.stroll.Stroll;
import com.sem.ssm2.structures.PlayerData;
import com.sem.ssm2.structures.collection.collectibles.Collectible;
import com.sem.ssm2.util.AccelerationStatus;
import com.sem.ssm2.util.BackButtonListener;
import com.sem.ssm2.util.NotificationController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Game extends com.badlogic.gdx.Game implements ApplicationListener {

    protected AccelerationStatus accelerationStatus;
    protected NotificationController notificationController;
    protected UserIDResolver userIDResolver;
    protected LocalStorageResolver localStorageResolver;
    protected Class<? extends Screen> startScreen;

    public Game(AccelerationStatus accelerationStatus,
                NotificationController notificationController,
                UserIDResolver userIDResolver,
                LocalStorageResolver localStorageResolver,
                Class<? extends Screen> startScreen){
        this.accelerationStatus = accelerationStatus;
        this.notificationController = notificationController;
        this.userIDResolver = userIDResolver;
        this.localStorageResolver = localStorageResolver;
        this.startScreen = startScreen;
    }

    protected Assets assets;
    protected Screen screen;
    protected HashMap<String, Screen> storedScreens;
    protected Screen queuedScreen;
    protected Client client;
    protected boolean setPreviousScreen = false;
    protected Stroll stroll;
    protected Collectible collectible;
    protected Host host;

    @Override
    public void create() {
        storedScreens = new HashMap<>();
        assets = new Assets();

        client = new Client(userIDResolver, localStorageResolver);
        client.getRemoteStateChange().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if(arg != null && (boolean) arg) {
                    client.getPlayerData(new ResponseHandler() {
                        @Override
                        public void handleResponse(Response response) {
                            client.updateRemotePlayerData((PlayerData)response.getData(), null);
                        }
                    });
                }
            }
        });
        client.setRemoteIP("127.0.0.1");
        client.setRemotePort(56789);
        client.connectToRemoteServer();

        Texture.setAssetManager(assets);
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setCatchBackKey(true);
        multiplexer.addProcessor(new BackButtonListener(this));
        Gdx.input.setInputProcessor(multiplexer);

        LoadingScreen loadingScreen = new LoadingScreen(this);
        loadingScreen.loadAssets();
        assets.finishLoading();
        storedScreens.put(LoadingScreen.class.getName(), loadingScreen);

        client.updateStrollTime(0, new ResponseHandler() {
            @Override
            public void handleResponse(Response response) {
                System.out.println("RESET TIMER");
            }
        });

        setScreen(startScreen);
    }

    public void setScreen(Class<? extends Screen> newScreen) {
        if (storedScreens.containsKey(newScreen.getName())) {
            setScreen(storedScreens.get(newScreen.getName()));
        } else {
            createNewScreen(newScreen);
        }
    }

    public void setMultiPlayerScreen(Class<? extends Screen> newScreen) {
        if(host != null) {
            if (storedScreens.containsKey(newScreen.getName())) {
                setScreen(storedScreens.get(newScreen.getName()));
            } else {
                createNewMultiPlayerScreen(newScreen);
            }
        }
    }

    protected void createNewMultiPlayerScreen(Class<? extends Screen> newScreen) {
        try {
            Screen screenInstance = newScreen.getDeclaredConstructor(Game.class, Host.class).newInstance(this, host);
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

    public void setPreviousScreen() {
        setPreviousScreen = true;
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
    }

    @Override
    public void render() {
        if(screen != null) screen.render(Gdx.graphics.getDeltaTime());
        for (Runnable toRunBeforeNextCycle : client.getPostRunnables()) {
            Gdx.app.postRunnable(toRunBeforeNextCycle);
        }
        client.resetPostRunnables();
        if(setPreviousScreen) {
            setPreviousScreen = false;
            Class<? extends Screen> previousScreen = screen.previousScreen();
            if(previousScreen == null) {
                Gdx.app.exit();
            } else {
                setScreen(screen.previousScreen());
            }
        }
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

    public AccelerationStatus getAccelerationStatus() {
        return accelerationStatus;
    }

    public void startStroll() {
        stroll = new Stroll(this);
    }

    public Stroll getStroll() {
        return stroll;
    }

    public void setCollectible(Collectible collectible) {
        System.out.println(collectible.toString());
        this.collectible = collectible;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void resetHost() {
        if(host != null){
            host.dispose();
            host = null;
        }
    }

    public Collectible getCollectible() {
        return collectible;
    }

}
