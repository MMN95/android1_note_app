package ru.mmn.noteapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteListFragment extends Fragment {

    public static final String CURRENT_NOTE = "CurrentNote";
    private static final int DEFAULT_DURATION = 1000;

    private NoteSource data;
    private NoteListAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isLandscape;
    private Note currentNote;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public NoteListFragment(){
    }

    public static NoteListFragment newInstance(){
        return new NoteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        initView(view);
        setHasOptionsMenu(true);
        data = new NoteSourceFirebaseImpl().init(new NoteSourceResponse() {
            @Override
            public void initialized(NoteSource noteSource) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notelist_menu, menu);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId){
        switch (menuItemId){
            case R.id.action_add:
                navigation.addFragment(NoteFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(Note note) {
                        data.addNote(note);
                        adapter.notifyItemInserted(data.size() - 1);
                        moveToFirstPosition = true;
                    }
                });
                return true;
            case R.id.action_update:
                final int updatePosition = adapter.getMenuPosition();
                navigation.addFragment(NoteFragment.newInstance(data.getNote(updatePosition)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNoteData(Note note) {
                        data.updateNote(updatePosition, note);
                        adapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;
            case R.id.action_delete:
                showWarning();
                return true;
            case R.id.action_clear:
                data.clearNoteList();
                adapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }

    private void showWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.warning)
                .setMessage(R.string.warning_text)
                .setCancelable(false)
                .setPositiveButton(R.string.warning_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deletePosition = adapter.getMenuPosition();
                        data.deleteNote(deletePosition);
                        adapter.notifyItemRemoved(deletePosition);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_notes);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(DEFAULT_DURATION);
        animator.setRemoveDuration(DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }

        adapter.setOnItemClickListener((view, position) -> {
            Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);

    }

    //TODO landscape orientation
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
            currentNote = new Note(currentNote.getTitle(), currentNote.getDescription(), currentNote.getDate());
            showNote(currentNote);
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
                .replace(R.id.note_fragment_land, NoteFragment.newInstance(currentNote))
                .commit();
    }

    private void showPortNote(Note currentNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NoteActivity.class);
        intent.putExtra(NoteFragment.ARG_NOTE, currentNote);
        startActivity(intent);
    }

}