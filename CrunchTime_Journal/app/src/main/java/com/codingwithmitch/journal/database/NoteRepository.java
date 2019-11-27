package com.codingwithmitch.journal.database;


import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.codingwithmitch.journal.database.async.AsyncResponse;
import com.codingwithmitch.journal.database.async.DeleteAsyncTask;
import com.codingwithmitch.journal.database.async.InsertAsyncTask;
import com.codingwithmitch.journal.database.async.UpdateAsyncTask;
import com.codingwithmitch.journal.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note, AsyncResponse context){
        InsertAsyncTask insert = new InsertAsyncTask(mNoteDatabase.getNoteDao());
        insert.delegate = context;
        insert.execute(note);
    }

    public void updateNoteTask(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNoteTask(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}













