package com.codingwithmitch.journal.util;

import android.util.Log;
import java.util.Date;

public class Utility {

    private static final String TAG = "Utility";

    public static String getCurrentEpochMilli(){
        try {
            String currentDateTime = Long.toString(new Date().getTime());
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
