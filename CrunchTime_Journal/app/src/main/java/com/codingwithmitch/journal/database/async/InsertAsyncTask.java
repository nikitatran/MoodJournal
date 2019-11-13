package com.codingwithmitch.journal.database.async;

import android.os.AsyncTask;

import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.database.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }

}