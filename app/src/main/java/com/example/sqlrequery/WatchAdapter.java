package com.example.sqlrequery;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlrequery.databinding.ItemWatchingBinding;
import com.example.sqlrequery.model.MovieEntity;
import com.example.sqlrequery.model.StudentEntity;
import com.example.sqlrequery.model.WatchingEntity;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import io.requery.query.Tuple;

/**
 * Displays the tuples returned by the typed join, one reusable row view at a time.
 *
 * <p>The adapter deliberately receives Requery {@link Tuple}s directly. Introducing a separate
 * display-model class would hide the useful connection between the selected typed attributes and
 * the values shown on screen. {@link ItemWatchingBinding} is generated from
 * {@code item_watching.xml}, so row views also use View Binding.</p>
 */
final class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.RowHolder> {
    /** Current snapshot of the join result. */
    private List<Tuple> rows = Collections.emptyList();

    /** Activity action to run when a row's Delete button is pressed. */
    private final Consumer<Tuple> delete;

    /**
     * Creates an adapter whose only row action is supplied by the Activity.
     *
     * @param delete receives the exact tuple represented by the clicked row
     */
    WatchAdapter(Consumer<Tuple> delete) {
        this.delete = delete;
    }

    /** Creates View Binding for one row when RecyclerView needs another reusable view. */
    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWatchingBinding binding = ItemWatchingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RowHolder(binding);
    }

    /**
     * Copies the selected name, title, and rating from one join tuple into one row.
     *
     * @param holder reusable holder for the row views
     * @param position position of the tuple currently being displayed
     */
    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        Tuple row = rows.get(position);
        holder.binding.studentName.setText(row.get(StudentEntity.NAME));
        holder.binding.movieTitle.setText(row.get(MovieEntity.TITLE));
        holder.binding.rating.setText(holder.itemView.getContext().getString(
                R.string.rating_value, row.get(WatchingEntity.RATING)));
        holder.binding.buttonDelete.setOnClickListener(v -> delete.accept(row));
    }

    /** @return number of join tuples currently available to display */
    @Override
    public int getItemCount() {
        return rows.size();
    }

    /**
     * Replaces the tiny teaching data set after the join is rerun.
     *
     * <p>{@code notifyDataSetChanged()} is intentionally sufficient here: the demo has only a few
     * rows, and DiffUtil/ListAdapter would add machinery unrelated to its database lesson.</p>
     *
     * @param rows fresh snapshot returned by Requery
     */
    @SuppressLint("NotifyDataSetChanged")
    void setRows(List<Tuple> rows) {
        this.rows = rows;
        notifyDataSetChanged();
    }

    /** Holds the generated binding for one recyclable {@code item_watching.xml} view. */
    static final class RowHolder extends RecyclerView.ViewHolder {
        /** Type-safe access to the row's three columns and Delete button. */
        private final ItemWatchingBinding binding;

        /** @param binding binding inflated for this reusable row */
        RowHolder(ItemWatchingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
