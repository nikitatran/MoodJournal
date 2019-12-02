/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */

package cpsc4150.projects.journal.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import cpsc4150.projects.journal.database.async.AsyncResponse;
import cpsc4150.projects.journal.database.async.DeleteAsyncTask;
import cpsc4150.projects.journal.database.async.InsertAsyncTask;
import cpsc4150.projects.journal.database.async.InsertAsyncTaskNoInterface;
import cpsc4150.projects.journal.database.async.UpdateAsyncTask;
import cpsc4150.projects.journal.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    //Insert a note into the database where something listens for it to finish
    public void insertNoteTask(Note note, AsyncResponse context){
        InsertAsyncTask insert = new InsertAsyncTask(mNoteDatabase.getNoteDao());
        insert.delegate = context;
        insert.execute(note);
    }

    //Insert a note into the database without any listeners
    public void insertNoteTask(Note note){
        new InsertAsyncTaskNoInterface(mNoteDatabase.getNoteDao()).execute(note);
    }

    //Update a note in the database
    public void updateNoteTask(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void deleteNoteTask(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    /* QUERIES */

    /**
     * @return all notes in the database ordered by timestamp descending (newest first)
     */
    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    /**
     * @param time timeframe to filter notes by (EX: get notes that are no more than 10 days old)
     * @return all notes with a timestamp that matches a given timeframe
     */
    public LiveData<List<Note>> retrieveNotesByTimeTask(long time) {
        return mNoteDatabase.getNoteDao().getNotesByTime(time);
    }

    /**
     * @param id primary key
     * @return note that matches id
     */
    public LiveData<List<Note>> getNoteById(int id) {
        return mNoteDatabase.getNoteDao().getOneNote(id);
    }
}













