package ru.mmn.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Note implements Parcelable {

    private final String title;
    private final String description;
    private final Date date;
    private String id;

    public Note(String title, String description, Date date){
        this.title = title;
        this.description = description;
        this.date = date;
    }

    protected Note(Parcel in){
        title = in.readString();
        description = in.readString();
        date = new Date(in.readLong());
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Date getDate() {
        return date;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeLong(date.getTime());
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
}
