package com.mcac.devices.ks8123.util;

import android.os.Build;

public class DeviceUtils {
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }
}
