package com.sem.ssm2.android.util;

import android.content.Context;
import android.provider.Settings;
import com.sem.ssm2.client.UserIDResolver;

/**
 * Gets the user id's from the android application.
 */
public class AndroidIDResolver implements UserIDResolver {

    /**
     * The context of the android game application.
     */
    protected Context cContext;

    /**
     * Constructs a new android is resolver.
     *
     * @param context The game context.
     */
    public AndroidIDResolver(final Context context) {
        cContext = context;
    }

    @Override
    public String getID() {
        return Settings.Secure.getString(cContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}