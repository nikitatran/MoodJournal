/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */
package com.codingwithmitch.journal.database.async;

/*
    Used to perform action in another activity
    after an Async task finishes executing.

    Referenced from https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
 */
public interface AsyncResponse {
    void processFinish(long output);
}
