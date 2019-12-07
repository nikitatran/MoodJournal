/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */

package cpsc4150.projects.journal.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import cpsc4150.projects.journal.models.Note;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteDao {

    @Insert(onConflict = REPLACE)
    long[] insertNotes(Note... notes);

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE :currentTime < CAST(timestamp AS LONG)")
    LiveData<List<Note>> getNotesByTime(long currentTime);

    @Query("SELECT * FROM notes WHERE id = :id ")
    LiveData<List<Note>> getOneNote(int id);

    @Delete
    int delete(Note... notes);

    @Update
    int updateNotes(Note... notes);
}
