package ru.mmn.noteapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteSourceImpl implements NoteSource{
    private final List<Note> dataSource;
    private final Resources resources;

    public NoteSourceImpl(Resources resources){
        dataSource = new ArrayList<>();
        this.resources = resources;
    }

    public NoteSourceImpl init(NoteSourceResponse noteSourceResponse){
        String[] titles = resources.getStringArray(R.array.note_titles);
        String[] descriptions = resources.getStringArray(R.array.note_description);

        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new Note(titles[i], descriptions[i], Calendar.getInstance().getTime()));
        }

        if (noteSourceResponse != null){
            noteSourceResponse.initialized(this);
        }
        return this;
    }
    public Note getNote(int position){
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }

    @Override
    public void deleteNote(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateNote(int position, Note note) {
        dataSource.set(position, note);
    }

    @Override
    public void addNote(Note note) {
        dataSource.add(note);
    }

    @Override
    public void clearNoteList() {
        dataSource.clear();
    }
}
