package ru.mmn.noteapp;

public interface NoteSource {
    Note getNote(int position);
    int size();
}
