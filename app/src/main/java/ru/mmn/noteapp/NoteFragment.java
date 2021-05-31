package ru.mmn.noteapp;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NoteFragment extends Fragment {

    public static final String ARG_NOTE = "note";
    private Note note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        TextView noteTitleView = view.findViewById(R.id.title_view);
        noteTitleView.setText(note.getTitle());
        return view;
    }

    public static NoteFragment newInstance(Note note){
        NoteFragment f = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

}