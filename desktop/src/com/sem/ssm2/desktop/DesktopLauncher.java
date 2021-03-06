package com.sem.ssm2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sem.ssm2.Game;
import com.sem.ssm2.desktop.util.DesktopAccelerationStatus;
import com.sem.ssm2.desktop.util.DesktopIDResolver;
import com.sem.ssm2.desktop.util.DesktopNotificationController;
import com.sem.ssm2.desktop.util.DesktopStorageResolver;
import com.sem.ssm2.screens.MainMenu;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 960;
		config.width = 540;
		new LwjglApplication(new Game(
				new DesktopAccelerationStatus(),
				new DesktopNotificationController(),
				new DesktopIDResolver(),
				new DesktopStorageResolver(),
				MainMenu.class
		), config);
	}
}
