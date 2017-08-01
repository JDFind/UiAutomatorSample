package com.bitdefender.uiautomatorsample;

import android.content.Context;
import android.content.Intent;

/**
 * Created by anicolae on 01-Aug-17.
 */

public class TestUtils {

    /**
     * Checks if an app with a given {@code packageName} is installed on the device
     */
    public static boolean isAppInstalled(Context context, String packageName) {

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return intent != null;
    }

    /**
     * Launches the application with a given {@code packageName}
     * @return {@code true} if the app launch was started, {@code false} otherwise
     */
    public static boolean launchApp(Context context, String packageName) {

        if (!isAppInstalled(context, packageName)) {
            return false;
        }

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        return true;
    }
}
