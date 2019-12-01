package com.codingwithmitch.journal.database.async;

import android.os.AsyncTask;
import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.database.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.delete(notes);
        return null;
    }

}