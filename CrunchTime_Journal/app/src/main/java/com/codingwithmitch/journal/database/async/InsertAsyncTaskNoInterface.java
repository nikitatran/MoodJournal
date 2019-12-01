package com.codingwithmitch.journal.database.async;

import android.os.AsyncTask;
import com.codingwithmitch.journal.database.NoteDao;
import com.codingwithmitch.journal.models.Note;

public class InsertAsyncTaskNoInterface extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public InsertAsyncTaskNoInterface(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }

}
