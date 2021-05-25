package ru.mmn.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int index;
    private String title;

    public Note(int index, String title){
        this.index = index;
        this.title = title;
    }

    protected Note(Parcel in){
        title = in.readString();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
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
