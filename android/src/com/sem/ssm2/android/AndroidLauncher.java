package com.sem.ssm2.android;

import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sem.ssm2.Game;
import com.sem.ssm2.android.util.AndroidAccelerationStatus;
import com.sem.ssm2.android.util.AndroidIDResolver;
import com.sem.ssm2.android.util.AndroidStorageResolver;
import com.sem.ssm2.android.util.notification.AndroidNotificationController;

import java.io.File;

/**
 * The AndroidLauncher class runs the application on an Android device.
 */
public class AndroidLauncher extends AndroidApplication {

	/**
	 * System sensor service of Android. Used for the accelerometer.
	 */
	protected SensorManager cSensorManager;

	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File dbFile = getContext().getDatabasePath("local.db");
		if (!dbFile.exists()) {
			if (!dbFile.getParentFile().exists()) {
				dbFile.getParentFile().mkdirs();
			}
			SQLiteDatabase db = getContext().openOrCreateDatabase("local.db", MODE_WORLD_WRITEABLE, null);
			db.close();
		}

//		WifiManager wfm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		if (wfm.isWifiEnabled()) {
//			wfm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "cg.group4.android");
//		}

		cSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		config.useImmersiveMode = true;
		initialize(new Game(
                    new AndroidAccelerationStatus(cSensorManager),
                    new AndroidNotificationController(this),
                    new AndroidIDResolver(getContext()),
                    new AndroidStorageResolver()),
            config);
	}
}