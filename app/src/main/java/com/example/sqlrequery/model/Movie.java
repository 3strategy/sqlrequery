package com.example.sqlrequery.model;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Defines one row in the SQLite {@code Movie} table.
 *
 * <p>Requery generates {@link MovieEntity} from this small interface. The generated class supplies
 * setters such as {@code setTitle}, as well as typed attributes such as {@code MovieEntity.TITLE}
 * that can be used in Java queries without textual column names.</p>
 */
@Entity
@Table(name = "Movie")
public interface Movie extends Persistable {
    /**
     * Returns the SQLite primary key. {@link Generated} makes SQLite assign it on insert.
     */
    @Key @Generated
    int getId();

    /** Returns the movie title shown in the joined result. */
    String getTitle();

    /** Returns the movie genre stored to demonstrate an ordinary non-key column. */
    String getGenre();
}
