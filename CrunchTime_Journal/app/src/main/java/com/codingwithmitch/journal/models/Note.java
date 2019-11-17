package com.codingwithmitch.journal.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "notes")
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    //Adding New Columns for Emotions
    @ColumnInfo(name = "bored")
    private double bored;

    @ColumnInfo(name = "angry")
    private double angry;

    @ColumnInfo(name = "sad")
    private double sad;

    @ColumnInfo(name = "fear")
    private double fear;

    @ColumnInfo(name = "happy")
    private double happy;

    @ColumnInfo(name = "excited")
    private double excited;


    public Note(String title, String content, String timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Ignore
    public Note() {

    }


    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        timestamp = in.readString();

    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    //Settings and Getters for Emotions
    public double getBored() {return bored;}

    public void setBored(double bored) {this.bored = bored;}

    public double getAngry() {return angry;}

    public void setAngry(double angry) {this.angry = angry;}

    public double getSad() {return sad;}

    public void setSad(double sad) {this.sad = sad;}

    public double getFear() {return fear;}

    public void setFear(double fear) {this.fear = fear;}

    public double getHappy() {return happy;}

    public void setHappy(double happy) {this.happy = happy;}

    public double getExcited() {return excited;}

    public void setExcited(double excited) {this.excited = excited;}



    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(timestamp);


    }
}
