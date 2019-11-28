package com.codingwithmitch.journal.database.async;

import android.os.AsyncTask;
import android.util.Log;

import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.database.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, long[]> {
    private NoteDao mNoteDao;
    private long[] id;
    public AsyncResponse delegate = null;
    //ref: https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a

    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }


    @Override
    protected long[] doInBackground(Note... notes) {
        return mNoteDao.insertNotes(notes);
        //return null;
    }

    @Override
    protected void onPostExecute(long[] id) {
        delegate.processFinish(id[0]);
        Log.d("insertasync", ""+id[0]);
        return;
    }
}

