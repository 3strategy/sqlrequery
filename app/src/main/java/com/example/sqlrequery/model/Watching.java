package com.example.sqlrequery.model;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Defines the rated association between one {@link Student} and one {@link Movie}.
 *
 * <p>This is deliberately an entity rather than a plain {@code @ManyToMany} collection because the
 * relationship owns data of its own: {@link #getRating() rating}. Requery therefore creates a real
 * {@code Watching} junction table that students can inspect and query.</p>
 *
 * <p>The two {@link Key} annotations form the composite primary key
 * {@code (studentId, movieId)}. Each {@link ManyToOne} also creates a foreign key, making the object
 * model read as {@code Student 1 -- * Watching * -- 1 Movie}. Requery additionally generates the
 * scalar {@code WatchingEntity.STUDENT_ID} and {@code WatchingEntity.MOVIE_ID} attributes used by
 * the explicit joins in the Activity.</p>
 */
@Entity
@Table(name = "Watching")
public interface Watching extends Persistable {
    /** Returns the student side of this relationship and maps it to {@code studentId}. */
    @Key @ManyToOne
    @Column(name = "studentId", nullable = false)
    Student getStudent();

    /** Returns the movie side of this relationship and maps it to {@code movieId}. */
    @Key @ManyToOne
    @Column(name = "movieId", nullable = false)
    Movie getMovie();

    /** Returns the 1–10 rating stored on the relationship itself. */
    int getRating();
}
