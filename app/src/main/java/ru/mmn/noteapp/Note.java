package ru.mmn.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int index;
    private String title;
//    private String description;
    //TODO private Date date;

    public Note(int index, String title){
        this.index = index;
        this.title = title;
//        this.description = description;
    }

    protected Note(Parcel in){
        index = in.readInt();
        title = in.readString();
//        description = in.readString();
    }

    public String getTitle() {
        return title;
    }

//    public String getDescription() {
//        return description;
//    }

    public int getIndex() {
        return index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
//        dest.writeString(getDescription());
        dest.writeInt(getIndex());
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
