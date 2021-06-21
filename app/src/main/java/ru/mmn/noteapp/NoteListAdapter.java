package ru.mmn.noteapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private final static String TAG = "NoteListAdapter";

    private final Fragment fragment;
    private NoteSource dataSource;
    private OnItemClickListener itemClickListener;
    private int menuPosition;

    public NoteListAdapter(Fragment fragment){
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        Log.d(TAG, "onCreateViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.ViewHolder holder, int position) {
        holder.setData(dataSource.getNote(position));
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
    public int getMenuPosition() {
        return menuPosition;
    }

    public void setDataSource(NoteSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView description;
        private final TextView date;

        public ViewHolder(final View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.noteTitle);
            description = itemView.findViewById(R.id.noteDescription);
            date = itemView.findViewById(R.id.noteDate);
            registerContextMenu(itemView);

            title.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            title.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu(10,10);
                return true;
            });
        }

        private void registerContextMenu(View itemView) {
            if (fragment != null){
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(Note note){
            title.setText(note.getTitle());
            description.setText(note.getDescription());
            date.setText(new SimpleDateFormat("dd-MM-yy").format(note.getDate()));
        }

    }
}
