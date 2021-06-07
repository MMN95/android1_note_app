package ru.mmn.noteapp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Date;

public class NoteFragment extends Fragment {

    public static final String ARG_NOTE = "note";

    private Note note;
    private Publisher publisher;
    private TextInputEditText title;
    private TextInputEditText description;
    private DatePicker datePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initView(view);
        if (note != null){
            populateView();
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        note = collectNoteData();
    }

    private Note collectNoteData() {
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        Date date = getDateFromDatePicker();
        return new Note(title, description, date);
    }

    private Date getDateFromDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.datePicker.getYear());
        calendar.set(Calendar.MONTH, this.datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return calendar.getTime();
    }

    private void initView(View view) {
        title = view.findViewById(R.id.title_view);
        description = view.findViewById(R.id.description_view);
        datePicker = view.findViewById(R.id.date_view);
    }

    private void populateView() {
        title.setText(note.getTitle());
        description.setText(note.getDescription());
        initDatePicker(note.getDate());
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

    public static NoteFragment newInstance(Note note){
        NoteFragment f = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        f.setArguments(args);
        return f;
    }

    public static NoteFragment newInstance(){
        return new NoteFragment();
    }
}