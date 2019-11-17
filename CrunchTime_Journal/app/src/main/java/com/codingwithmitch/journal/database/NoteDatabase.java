package com.codingwithmitch.journal.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.codingwithmitch.journal.models.Note;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    private static NoteDatabase instance;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notes "
                    + " ADD COLUMN bored REAL NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE notes "
                    + " ADD COLUMN angry REAL NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE notes "
                    + " ADD COLUMN sad REAL NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE notes "
                    + " ADD COLUMN fear REAL NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE notes "
                    + " ADD COLUMN happy REAL NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE notes "
                    + " ADD COLUMN excited REAL NOT NULL DEFAULT 0");
        }
    };

    static NoteDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    public abstract NoteDao getNoteDao();
}