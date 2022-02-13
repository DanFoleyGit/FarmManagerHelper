package com.example.farmmanagerhelper;

import android.text.TextUtils;

public class FarmService {

    public static boolean InputsNotEmpty(String farmName, String farmPassCode) {
        if (TextUtils.isEmpty(farmName) || TextUtils.isEmpty(farmPassCode)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean InputsDontContainWhiteSpaces(String farmName, String farmPassCode) {

        if (farmName.contains(" ") || farmPassCode.contains(" ")) {
            return false;
        }

        return true;

    }
}
