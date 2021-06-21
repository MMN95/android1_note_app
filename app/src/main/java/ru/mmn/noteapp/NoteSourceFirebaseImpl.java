package ru.mmn.noteapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteSourceFirebaseImpl implements NoteSource {

    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NoteSourceFirebaseImpl]";

    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private CollectionReference collection = store.collection(NOTES_COLLECTION);
    private List<Note> noteData = new ArrayList<>();

    @Override
    public NoteSource init(final NoteSourceResponse noteSourceResponse) {
        collection.orderBy(NoteDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            noteData = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                Note note = NoteDataMapping.toNote(id, doc);
                                noteData.add(note);
                            }
                            Log.d(TAG, "success " + noteData.size() + " qnt");
                            noteSourceResponse.initialized(NoteSourceFirebaseImpl.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public Note getNote(int position) {
        return noteData.get(position);
    }

    @Override
    public int size() {
        if (noteData == null){
            return 0;
        }
        return noteData.size();
    }

    @Override
    public void deleteNote(int position) {
        collection.document(noteData.get(position).getId()).delete();
        noteData.remove(position);
    }

    @Override
    public void updateNote(int position, Note note) {
        String id = note.getId();
        collection.document(id).set(NoteDataMapping.toDocument(note));
    }

    @Override
    public void addNote(final Note note) {
        collection.add(NoteDataMapping.toDocument(note)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                note.setId(documentReference.getId());
            }
        });
    }

    @Override
    public void clearNoteList() {
        for (Note n: noteData) {
            collection.document(n.getId()).delete();
        }
        noteData = new ArrayList<>();
    }
}

