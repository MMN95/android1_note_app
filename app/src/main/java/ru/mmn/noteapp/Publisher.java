package ru.mmn.noteapp;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private final List<Observer> observers;

    public Publisher(){
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer){
        observers.add(observer);
    }

    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }

    public void notifySingle(Note note){
        for (Observer o : observers){
            o.updateNoteData(note);
            unsubscribe(o);
        }
    }
}
