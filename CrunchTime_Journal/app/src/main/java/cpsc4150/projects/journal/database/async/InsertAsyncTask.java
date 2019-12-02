/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)

    References used:
        1. https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
 */

package cpsc4150.projects.journal.database.async;

import android.os.AsyncTask;
import cpsc4150.projects.journal.models.Note;
import cpsc4150.projects.journal.database.NoteDao;

//Insert a note from the database in the background
//and notify observer when it finishes with the returned note primary key id
public class InsertAsyncTask extends AsyncTask<Note, Void, long[]> {
    private NoteDao mNoteDao;
    public AsyncResponse delegate = null;

    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected long[] doInBackground(Note... notes) {
        return mNoteDao.insertNotes(notes);
    }

    @Override
    protected void onPostExecute(long[] id) {
        //Based off of reference 1
        delegate.processFinish(id[0]);
        return;
    }
}

