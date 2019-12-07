/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */

package cpsc4150.projects.journal.util;

import java.util.Date;

public class Utility {

    private static final String TAG = "Utility";

    /**
     * pre:
     * post: returns current time in milliseconds as a String
     *
     * @return current time in milliseconds as a String
     */
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
