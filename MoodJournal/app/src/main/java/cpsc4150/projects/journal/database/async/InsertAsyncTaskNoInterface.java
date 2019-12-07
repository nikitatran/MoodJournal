/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */
package cpsc4150.projects.journal.database.async;

import android.os.AsyncTask;
import cpsc4150.projects.journal.database.NoteDao;
import cpsc4150.projects.journal.models.Note;

//Insert a note from the database in the background
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
