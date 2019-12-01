package com.codingwithmitch.journal.database.async;

import android.os.AsyncTask;
import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.database.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.updateNotes(notes);
        return null;
    }

}