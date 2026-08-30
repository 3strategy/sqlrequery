package com.example.sqlrequery.model;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Defines one row in the SQLite {@code Student} table.
 *
 * <p>This interface is the class-first source of truth. Requery's annotation processor reads it
 * during the build and generates {@link StudentEntity}, including setters, typed query attributes,
 * and the metadata used to create the table. Extending {@link Persistable} lets generated student
 * objects be stored by Requery's {@code EntityDataStore}.</p>
 */
@Entity
@Table(name = "Student")
public interface Student extends Persistable {
    /**
     * Returns the SQLite primary key. {@link Generated} makes SQLite assign it on insert.
     */
    @Key @Generated
    int getId();

    /** Returns the student's display name. */
    String getName();
}
