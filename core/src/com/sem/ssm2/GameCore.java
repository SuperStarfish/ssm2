package com.sem.ssm2;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.sem.ssm2.assets.Assets;
import com.sem.ssm2.screens.GameScreen;
import com.sem.ssm2.screens.LoadingScreen;
import com.sem.ssm2.screens.MainMenu;
import com.sem.ssm2.screens.SettingsScreen;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class GameCore extends Game {
	Assets assets;
	HashMap<String, GameScreen> storedScreens;
	String queuedScreen;

	@Override
	public void create() {
		assets = Assets.getInstance();
		storedScreens = new HashMap<String, GameScreen>();
		Gdx.input.setInputProcessor(new InputMultiplexer());

		LoadingScreen temp = new LoadingScreen(this);
		temp.loadAssets();
		assets.finishLoading();
		storedScreens.put(LoadingScreen.class.getName(), temp);
		setScreen(SettingsScreen.class);
	}

	public void setScreen(Class<? extends GameScreen> screen) {
        if(storedScreens.containsKey(screen.getName())) {
			super.setScreen(storedScreens.get(screen.getName()));
		} else {
			try {
				GameScreen screenInstance = screen.getDeclaredConstructor(GameCore.class).newInstance(this);
				storedScreens.put(screen.getName(), screenInstance);
				setGameScreen(screenInstance);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public void setGameScreen(GameScreen screen) {
		if(!screen.loadAssets()) {
			queuedScreen = screen.getClass().getName();
            setScreen(LoadingScreen.class);
		} else {
			super.setScreen(screen);
		}
	}

	public void setQueuedScreen() {
        if(queuedScreen != null) {
			super.setScreen(storedScreens.get(queuedScreen));
			queuedScreen = null;
		}
	}
}
