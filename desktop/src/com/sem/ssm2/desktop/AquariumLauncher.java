package com.sem.ssm2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sem.ssm2.Game;
import com.sem.ssm2.client.DummyUserIdResolver;
import com.sem.ssm2.desktop.util.*;
import com.sem.ssm2.screens.aquarium.Aquarium;

public class AquariumLauncher  {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        float scaleFactor = 1.5f;

        config.height = (int)(1080/scaleFactor);
        config.width = (int)(1920/scaleFactor);
        config.fullscreen = false;
        new LwjglApplication(new Game(
                new DesktopAccelerationStatus(),
                new DesktopNotificationController(),
                new DummyUserIdResolver(),
                new AquariumStorageResolver(),
                Aquarium.class
        ), config);
    }
}
