package ru.mmn.noteapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteListFragment extends Fragment {

    private boolean isLandscape;
    private Note currentNote;
    public static final String CURRENT_NOTE = "CurrentNote";

    public NoteListFragment(){
    }

    public static NoteListFragment newInstance(){
        return new NoteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_notes);
       NoteSource data = new NoteSourceImpl(getResources()).init();
        initRecyclerView(recyclerView, data);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, NoteSource data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final NoteListAdapter adapter = new NoteListAdapter(data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNoteList(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape){
            showLandNote(currentNote);
        }

        if (savedInstanceState != null){
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = new Note(getResources().getStringArray(R.array.note_titles)[0], getResources().getStringArray(R.array.note_description)[0]);
        }
    }


    private void initNoteList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.note_titles);
        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < notes.length; i++) {
            String note = notes[i];
            View item = layoutInflater.inflate(R.layout.item, layoutView, false);
            TextView noteTitleView = item.findViewById(R.id.noteTitle);
            noteTitleView.setText(note);
            layoutView.addView(item);

            final int currentIndex = i;
            noteTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentNote = new Note(getResources().getStringArray(R.array.note_titles)[0], getResources().getStringArray(R.array.note_description)[0]);
                    showNote(currentNote);

                }
            });
        }
    }

    private void showNote(Note note){
        if (isLandscape) {
            showLandNote(note);
        } else {
            showPortNote(note);
        }
    }

    private void showLandNote(Note currentNote) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.current_note, NoteFragment.newInstance(currentNote))
                .commit();
    }

    private void showPortNote(Note currentNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoteActivity.class);
        intent.putExtra(NoteFragment.ARG_NOTE, currentNote);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);

    }
}