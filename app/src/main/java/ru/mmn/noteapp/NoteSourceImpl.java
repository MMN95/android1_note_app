package ru.mmn.noteapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class NoteSourceImpl implements NoteSource{
    private List<Note> dataSource;
    private Resources resources;

    public NoteSourceImpl(Resources resources){
        dataSource = new ArrayList<>();
        this.resources = resources;
    }

    public NoteSourceImpl init(){
        String[] titles = resources.getStringArray(R.array.note_titles);
        String[] descriptions = resources.getStringArray(R.array.note_description);

        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new Note(titles[i], descriptions[i]));
        }
        return this;
    }
    public Note getNote(int position){
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }
}
