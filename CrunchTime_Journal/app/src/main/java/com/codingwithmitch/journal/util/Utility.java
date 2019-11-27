package com.codingwithmitch.journal.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Utility {

    private static final String TAG = "Utility";

    public static String getCurrentEpochMilli(){
        try {

            //SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy"); //MUST USE LOWERCASE 'y'. API 23- can't use uppercase
            //String currentDateTime = dateFormat.format(new Date()); // Find todays date
            String currentDateTime = Long.toString(new Date().getTime());
            Log.d(TAG, currentDateTime);

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
