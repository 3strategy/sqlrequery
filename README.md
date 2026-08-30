# Class Netflix: Android + Requery + SQLite

## 1. The three tables

```text
Student                 Movie                   Watching
-------                 -----                   --------
id                      id                      studentId
name                    title                   movieId
                        genre                   rating

Student 1 -------- * Watching * -------- 1 Movie
```

## 2. Why Watching is an entity

`Watching` is not just an invisible many-to-many link. The relationship has its own data—`rating`—so it is a real Java entity and a real SQLite junction table.

## 3. The SQL INNER JOIN

```sql
SELECT s.name, m.title, w.rating
FROM Student s
INNER JOIN Watching w
    ON w.studentId = s.id
INNER JOIN Movie m
    ON m.id = w.movieId;
```

## 4. The Requery equivalent

This is the exact typed query used in `MainActivity`:

```java
data.select(StudentEntity.NAME, MovieEntity.TITLE, WatchingEntity.RATING)
    .from(Student.class)
    .join(Watching.class).on(WatchingEntity.STUDENT_ID.eq(StudentEntity.ID))
    .join(Movie.class).on(MovieEntity.ID.eq(WatchingEntity.MOVIE_ID))
    .orderBy(StudentEntity.ID, MovieEntity.ID)
    .get();
```

There are no table or column-name strings in this Java query. Requery generated the typed attributes such as `STUDENT_ID` from the entity definitions. SQL logging is enabled, so the generated SQL is visible in Logcat.

## 5. Class-first idea

```text
Java entity definitions
        ↓
Requery annotation processor
        ↓
generated entities + Models.DEFAULT
        ↓
SQLite tables
```

## 6. Where to look

- `model/Student.java`, `Movie.java`, and `Watching.java`: the three tables and relationships.
- `Database.java`: opens SQLite and contains the Student, Movie, and Watching inserts.
- `MainActivity.java`: three small add dialogs, the typed INNER JOIN, and View Binding.
- `activity_main.xml`: the entire screen.

The three buttons add a Student, a Movie, or a Watching relationship. “Add Watching” chooses an existing student and movie and stores the relationship's rating; the joined result refreshes immediately.

The handwritten Java files contain the tutorial's final explanatory Javadocs. They focus on generated Requery code, SQLite mapping, association keys, inserts, typed queries, and the few deliberate UI tradeoffs.

Build with `gradlew.bat assembleDebug`. Requery 1.6.0 is loaded from Maven Central with `implementation` plus Java's standard `annotationProcessor` configuration.
