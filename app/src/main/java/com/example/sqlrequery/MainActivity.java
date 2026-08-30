package com.example.sqlrequery;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sqlrequery.databinding.ActivityMainBinding;
import com.example.sqlrequery.model.Movie;
import com.example.sqlrequery.model.MovieEntity;
import com.example.sqlrequery.model.Student;
import com.example.sqlrequery.model.StudentEntity;
import com.example.sqlrequery.model.Watching;
import com.example.sqlrequery.model.WatchingEntity;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.PersistenceException;
import io.requery.query.Result;
import io.requery.query.Tuple;
import io.requery.sql.EntityDataStore;

/**
 * The demo's only screen: three insert dialogs and a deletable RecyclerView of typed join results.
 *
 * <p>{@link ActivityMainBinding} is generated from {@code activity_main.xml}, so every XML view is
 * accessed through View Binding rather than {@code findViewById}. Database calls stay direct and
 * synchronous because this tiny teaching database contains only a few rows. A real application
 * should move potentially slow database work off the main thread.</p>
 */
public class MainActivity extends Activity {
    /** Generated, type-safe access to the views in {@code activity_main.xml}. */
    private ActivityMainBinding binding;

    /** The Requery store used directly by this one Activity. */
    private EntityDataStore<Persistable> data;

    /** Connects typed join tuples to the reusable views in the RecyclerView. */
    private WatchAdapter adapter;

    /**
     * Inflates the bound layout, opens and seeds SQLite, wires the buttons, and displays the join.
     *
     * @param savedInstanceState Android's previously saved Activity state, if one exists
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        data = Database.open(this);
        Database.seedIfEmpty(data);

        adapter = new WatchAdapter(this::deleteWatching);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonAddStudent.setOnClickListener(v -> showAddStudent());
        binding.buttonAddMovie.setOnClickListener(v -> showAddMovie());
        binding.buttonAddWatching.setOnClickListener(v -> showAddWatching());
        showWatchings();
    }

    /**
     * Runs the ORM equivalent of the lesson's two SQL {@code INNER JOIN}s and displays every tuple.
     *
     * <pre>{@code
     * SELECT s.name, m.title, w.rating
     * FROM Student s
     * INNER JOIN Watching w ON w.studentId = s.id
     * INNER JOIN Movie m ON m.id = w.movieId;
     * }</pre>
     *
     * <p>{@code StudentEntity.NAME}, {@code WatchingEntity.STUDENT_ID}, and the other expressions
     * are generated typed attributes—not hand-written table or column-name strings. Selecting these
     * expressions produces a {@link Tuple}; each value is retrieved with the same typed expression
     * that selected it. Two additional selected IDs are the composite key used by Delete; they are
     * not shown to the user. The try-with-resources block closes Requery's {@link Result} cursor.</p>
     */
    private void showWatchings() {
        // The typed Requery equivalent of the SQL INNER JOIN in README.md.
        try (Result<Tuple> rows = data
                .select(StudentEntity.NAME, MovieEntity.TITLE, WatchingEntity.RATING,
                        WatchingEntity.STUDENT_ID, WatchingEntity.MOVIE_ID)
                .from(Student.class)
                .join(Watching.class).on(WatchingEntity.STUDENT_ID.eq(StudentEntity.ID))
                .join(Movie.class).on(MovieEntity.ID.eq(WatchingEntity.MOVIE_ID))
                .orderBy(StudentEntity.ID, MovieEntity.ID)
                .get()) {
            adapter.setRows(rows.toList());
        }
    }

    /**
     * Deletes the clicked Watching relationship and immediately reruns the join.
     *
     * <p>The tuple carries {@code studentId} and {@code movieId} even though RecyclerView displays
     * only the student's name, movie title, and rating. Together the two IDs uniquely identify the
     * association row.</p>
     *
     * @param row join tuple represented by the clicked RecyclerView row
     */
    private void deleteWatching(Tuple row) {
        Database.deleteWatching(data,
                row.get(WatchingEntity.STUDENT_ID), row.get(WatchingEntity.MOVIE_ID));
        showWatchings();
        toast("Watching deleted");
    }

    /** Shows a one-field modal and inserts a Student when its name is non-empty. */
    private void showAddStudent() {
        EditText name = field("Name");
        new AlertDialog.Builder(this)
                .setTitle("Add Student")
                .setView(form(name))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String value = name.getText().toString().trim();
                    if (value.isEmpty()) {
                        toast("Enter a student name");
                    } else {
                        Database.addStudent(data, value);
                        toast("Student added");
                    }
                })
                .show();
    }

    /** Shows a two-field modal and inserts a Movie when both values are non-empty. */
    private void showAddMovie() {
        EditText title = field("Title");
        EditText genre = field("Genre");
        new AlertDialog.Builder(this)
                .setTitle("Add Movie")
                .setView(form(title, genre))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String movieTitle = title.getText().toString().trim();
                    String movieGenre = genre.getText().toString().trim();
                    if (movieTitle.isEmpty() || movieGenre.isEmpty()) {
                        toast("Enter a title and genre");
                    } else {
                        Database.addMovie(data, movieTitle, movieGenre);
                        toast("Movie added");
                    }
                })
                .show();
    }

    /**
     * Shows a modal for inserting the relationship entity.
     *
     * <p>The Student and Movie spinners are populated with ordinary typed Requery selects. The
     * selected entity objects are then assigned to a new Watching, which writes their IDs into the
     * foreign-key columns. A successful insert reruns the join so its effect is immediately visible.
     * The composite primary key rejects a duplicate Student–Movie pair.</p>
     */
    private void showAddWatching() {
        List<Student> students;
        List<Movie> movies;
        try (Result<Student> rows = data.select(Student.class)
                .orderBy(StudentEntity.NAME).get()) {
            students = rows.toList();
        }
        try (Result<Movie> rows = data.select(Movie.class)
                .orderBy(MovieEntity.TITLE).get()) {
            movies = rows.toList();
        }

        Spinner student = spinner(namesOf(students));
        Spinner movie = spinner(titlesOf(movies));
        EditText rating = field("Rating (1-10)");
        rating.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle("Add Watching")
                .setView(form(label("Student"), student, label("Movie"), movie, rating))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    int score;
                    try {
                        score = Integer.parseInt(rating.getText().toString());
                    } catch (NumberFormatException e) {
                        toast("Enter a rating from 1 to 10");
                        return;
                    }
                    if (score < 1 || score > 10) {
                        toast("Enter a rating from 1 to 10");
                        return;
                    }
                    try {
                        Database.addWatching(data,
                                students.get(student.getSelectedItemPosition()),
                                movies.get(movie.getSelectedItemPosition()), score);
                        showWatchings();
                        toast("Watching added");
                    } catch (PersistenceException e) {
                        // SQLite enforces Watching's composite primary key.
                        toast("That student already watched that movie");
                    }
                })
                .show();
    }

    /**
     * Creates one plain single-line dialog input without requiring another XML layout.
     *
     * @param hint short description shown while the field is empty
     * @return the new input view
     */
    private EditText field(String hint) {
        EditText field = new EditText(this);
        field.setHint(hint);
        field.setSingleLine();
        return field;
    }

    /**
     * Creates a small label for a programmatic dialog form.
     *
     * @param text label text
     * @return the new label view
     */
    private TextView label(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        return label;
    }

    /**
     * Creates a spinner backed by display strings while the caller retains the matching entities.
     *
     * @param values values displayed by the spinner
     * @return the configured spinner
     */
    private Spinner spinner(List<String> values) {
        Spinner spinner = new Spinner(this);
        spinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, values));
        return spinner;
    }

    /**
     * Stacks dialog controls vertically and adds density-independent horizontal padding.
     *
     * <p>Building these tiny forms in Java avoids three additional dialog XML files and keeps View
     * Binding focused on the Activity's real screen.</p>
     *
     * @param fields controls to place in the form
     * @return the dialog's content view
     */
    private LinearLayout form(View... fields) {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (24 * getResources().getDisplayMetrics().density);
        form.setPadding(padding, 0, padding, 0);
        for (View field : fields) form.addView(field);
        return form;
    }

    /**
     * Extracts spinner labels while preserving the Student list for the later insert.
     *
     * @param students students returned by Requery
     * @return names in the same order
     */
    private List<String> namesOf(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students) names.add(student.getName());
        return names;
    }

    /**
     * Extracts spinner labels while preserving the Movie list for the later insert.
     *
     * @param movies movies returned by Requery
     * @return titles in the same order
     */
    private List<String> titlesOf(List<Movie> movies) {
        List<String> titles = new ArrayList<>();
        for (Movie movie : movies) titles.add(movie.getTitle());
        return titles;
    }

    /**
     * Shows brief feedback without adding status views to the teaching screen.
     *
     * @param message feedback to display
     */
    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
