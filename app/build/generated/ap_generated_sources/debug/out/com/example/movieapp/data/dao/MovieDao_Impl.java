package com.example.movieapp.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.movieapp.data.model.Movie;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MovieDao_Impl implements MovieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Movie> __insertionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter<Movie> __updateAdapterOfMovie;

  private final SharedSQLiteStatement __preparedStmtOfDecrementAvailableCopies;

  private final SharedSQLiteStatement __preparedStmtOfIncrementAvailableCopies;

  public MovieDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovie = new EntityInsertionAdapter<Movie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `movies` (`id`,`title`,`genre`,`releaseYear`,`originalLanguage`,`country`,`director`,`actors`,`posterUrl`,`trailerUrl`,`totalCopies`,`availableCopies`,`description`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Movie entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getGenre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getGenre());
        }
        statement.bindLong(4, entity.getReleaseYear());
        if (entity.getOriginalLanguage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOriginalLanguage());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCountry());
        }
        if (entity.getDirector() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDirector());
        }
        if (entity.getActors() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActors());
        }
        if (entity.getPosterUrl() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPosterUrl());
        }
        if (entity.getTrailerUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTrailerUrl());
        }
        statement.bindLong(11, entity.getTotalCopies());
        statement.bindLong(12, entity.getAvailableCopies());
        if (entity.getDescription() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getDescription());
        }
      }
    };
    this.__updateAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `movies` SET `id` = ?,`title` = ?,`genre` = ?,`releaseYear` = ?,`originalLanguage` = ?,`country` = ?,`director` = ?,`actors` = ?,`posterUrl` = ?,`trailerUrl` = ?,`totalCopies` = ?,`availableCopies` = ?,`description` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Movie entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getGenre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getGenre());
        }
        statement.bindLong(4, entity.getReleaseYear());
        if (entity.getOriginalLanguage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOriginalLanguage());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCountry());
        }
        if (entity.getDirector() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDirector());
        }
        if (entity.getActors() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActors());
        }
        if (entity.getPosterUrl() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPosterUrl());
        }
        if (entity.getTrailerUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTrailerUrl());
        }
        statement.bindLong(11, entity.getTotalCopies());
        statement.bindLong(12, entity.getAvailableCopies());
        if (entity.getDescription() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getDescription());
        }
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDecrementAvailableCopies = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE movies SET availableCopies = availableCopies - 1 WHERE id = ? AND availableCopies > 0";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementAvailableCopies = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE movies SET availableCopies = availableCopies + 1 WHERE id = ? AND availableCopies < totalCopies";
        return _query;
      }
    };
  }

  @Override
  public long insertMovie(final Movie movie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfMovie.insertAndReturnId(movie);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateMovie(final Movie movie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfMovie.handle(movie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int decrementAvailableCopies(final int movieId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDecrementAvailableCopies.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, movieId);
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDecrementAvailableCopies.release(_stmt);
    }
  }

  @Override
  public int incrementAvailableCopies(final int movieId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementAvailableCopies.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, movieId);
    try {
      __db.beginTransaction();
      try {
        final int _result = _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfIncrementAvailableCopies.release(_stmt);
    }
  }

  @Override
  public List<Movie> getAllMovies() {
    final String _sql = "SELECT * FROM movies";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
      final int _cursorIndexOfReleaseYear = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseYear");
      final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
      final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
      final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
      final int _cursorIndexOfActors = CursorUtil.getColumnIndexOrThrow(_cursor, "actors");
      final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
      final int _cursorIndexOfTrailerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "trailerUrl");
      final int _cursorIndexOfTotalCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCopies");
      final int _cursorIndexOfAvailableCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCopies");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Movie _item;
        _item = new Movie();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpGenre;
        if (_cursor.isNull(_cursorIndexOfGenre)) {
          _tmpGenre = null;
        } else {
          _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
        }
        _item.setGenre(_tmpGenre);
        final int _tmpReleaseYear;
        _tmpReleaseYear = _cursor.getInt(_cursorIndexOfReleaseYear);
        _item.setReleaseYear(_tmpReleaseYear);
        final String _tmpOriginalLanguage;
        if (_cursor.isNull(_cursorIndexOfOriginalLanguage)) {
          _tmpOriginalLanguage = null;
        } else {
          _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
        }
        _item.setOriginalLanguage(_tmpOriginalLanguage);
        final String _tmpCountry;
        if (_cursor.isNull(_cursorIndexOfCountry)) {
          _tmpCountry = null;
        } else {
          _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        }
        _item.setCountry(_tmpCountry);
        final String _tmpDirector;
        if (_cursor.isNull(_cursorIndexOfDirector)) {
          _tmpDirector = null;
        } else {
          _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
        }
        _item.setDirector(_tmpDirector);
        final String _tmpActors;
        if (_cursor.isNull(_cursorIndexOfActors)) {
          _tmpActors = null;
        } else {
          _tmpActors = _cursor.getString(_cursorIndexOfActors);
        }
        _item.setActors(_tmpActors);
        final String _tmpPosterUrl;
        if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
          _tmpPosterUrl = null;
        } else {
          _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
        }
        _item.setPosterUrl(_tmpPosterUrl);
        final String _tmpTrailerUrl;
        if (_cursor.isNull(_cursorIndexOfTrailerUrl)) {
          _tmpTrailerUrl = null;
        } else {
          _tmpTrailerUrl = _cursor.getString(_cursorIndexOfTrailerUrl);
        }
        _item.setTrailerUrl(_tmpTrailerUrl);
        final int _tmpTotalCopies;
        _tmpTotalCopies = _cursor.getInt(_cursorIndexOfTotalCopies);
        _item.setTotalCopies(_tmpTotalCopies);
        final int _tmpAvailableCopies;
        _tmpAvailableCopies = _cursor.getInt(_cursorIndexOfAvailableCopies);
        _item.setAvailableCopies(_tmpAvailableCopies);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Movie getMovieById(final int movieId) {
    final String _sql = "SELECT * FROM movies WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, movieId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
      final int _cursorIndexOfReleaseYear = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseYear");
      final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
      final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
      final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
      final int _cursorIndexOfActors = CursorUtil.getColumnIndexOrThrow(_cursor, "actors");
      final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
      final int _cursorIndexOfTrailerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "trailerUrl");
      final int _cursorIndexOfTotalCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCopies");
      final int _cursorIndexOfAvailableCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCopies");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final Movie _result;
      if (_cursor.moveToFirst()) {
        _result = new Movie();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpGenre;
        if (_cursor.isNull(_cursorIndexOfGenre)) {
          _tmpGenre = null;
        } else {
          _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
        }
        _result.setGenre(_tmpGenre);
        final int _tmpReleaseYear;
        _tmpReleaseYear = _cursor.getInt(_cursorIndexOfReleaseYear);
        _result.setReleaseYear(_tmpReleaseYear);
        final String _tmpOriginalLanguage;
        if (_cursor.isNull(_cursorIndexOfOriginalLanguage)) {
          _tmpOriginalLanguage = null;
        } else {
          _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
        }
        _result.setOriginalLanguage(_tmpOriginalLanguage);
        final String _tmpCountry;
        if (_cursor.isNull(_cursorIndexOfCountry)) {
          _tmpCountry = null;
        } else {
          _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        }
        _result.setCountry(_tmpCountry);
        final String _tmpDirector;
        if (_cursor.isNull(_cursorIndexOfDirector)) {
          _tmpDirector = null;
        } else {
          _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
        }
        _result.setDirector(_tmpDirector);
        final String _tmpActors;
        if (_cursor.isNull(_cursorIndexOfActors)) {
          _tmpActors = null;
        } else {
          _tmpActors = _cursor.getString(_cursorIndexOfActors);
        }
        _result.setActors(_tmpActors);
        final String _tmpPosterUrl;
        if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
          _tmpPosterUrl = null;
        } else {
          _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
        }
        _result.setPosterUrl(_tmpPosterUrl);
        final String _tmpTrailerUrl;
        if (_cursor.isNull(_cursorIndexOfTrailerUrl)) {
          _tmpTrailerUrl = null;
        } else {
          _tmpTrailerUrl = _cursor.getString(_cursorIndexOfTrailerUrl);
        }
        _result.setTrailerUrl(_tmpTrailerUrl);
        final int _tmpTotalCopies;
        _tmpTotalCopies = _cursor.getInt(_cursorIndexOfTotalCopies);
        _result.setTotalCopies(_tmpTotalCopies);
        final int _tmpAvailableCopies;
        _tmpAvailableCopies = _cursor.getInt(_cursorIndexOfAvailableCopies);
        _result.setAvailableCopies(_tmpAvailableCopies);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _result.setDescription(_tmpDescription);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Movie> searchMovies(final String searchQuery) {
    final String _sql = "SELECT * FROM movies WHERE title LIKE '%' || ? || '%' OR genre LIKE '%' || ? || '%' OR director LIKE '%' || ? || '%' OR actors LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 2;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 3;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 4;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
      final int _cursorIndexOfReleaseYear = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseYear");
      final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
      final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
      final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
      final int _cursorIndexOfActors = CursorUtil.getColumnIndexOrThrow(_cursor, "actors");
      final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
      final int _cursorIndexOfTrailerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "trailerUrl");
      final int _cursorIndexOfTotalCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCopies");
      final int _cursorIndexOfAvailableCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCopies");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Movie _item;
        _item = new Movie();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpGenre;
        if (_cursor.isNull(_cursorIndexOfGenre)) {
          _tmpGenre = null;
        } else {
          _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
        }
        _item.setGenre(_tmpGenre);
        final int _tmpReleaseYear;
        _tmpReleaseYear = _cursor.getInt(_cursorIndexOfReleaseYear);
        _item.setReleaseYear(_tmpReleaseYear);
        final String _tmpOriginalLanguage;
        if (_cursor.isNull(_cursorIndexOfOriginalLanguage)) {
          _tmpOriginalLanguage = null;
        } else {
          _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
        }
        _item.setOriginalLanguage(_tmpOriginalLanguage);
        final String _tmpCountry;
        if (_cursor.isNull(_cursorIndexOfCountry)) {
          _tmpCountry = null;
        } else {
          _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        }
        _item.setCountry(_tmpCountry);
        final String _tmpDirector;
        if (_cursor.isNull(_cursorIndexOfDirector)) {
          _tmpDirector = null;
        } else {
          _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
        }
        _item.setDirector(_tmpDirector);
        final String _tmpActors;
        if (_cursor.isNull(_cursorIndexOfActors)) {
          _tmpActors = null;
        } else {
          _tmpActors = _cursor.getString(_cursorIndexOfActors);
        }
        _item.setActors(_tmpActors);
        final String _tmpPosterUrl;
        if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
          _tmpPosterUrl = null;
        } else {
          _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
        }
        _item.setPosterUrl(_tmpPosterUrl);
        final String _tmpTrailerUrl;
        if (_cursor.isNull(_cursorIndexOfTrailerUrl)) {
          _tmpTrailerUrl = null;
        } else {
          _tmpTrailerUrl = _cursor.getString(_cursorIndexOfTrailerUrl);
        }
        _item.setTrailerUrl(_tmpTrailerUrl);
        final int _tmpTotalCopies;
        _tmpTotalCopies = _cursor.getInt(_cursorIndexOfTotalCopies);
        _item.setTotalCopies(_tmpTotalCopies);
        final int _tmpAvailableCopies;
        _tmpAvailableCopies = _cursor.getInt(_cursorIndexOfAvailableCopies);
        _item.setAvailableCopies(_tmpAvailableCopies);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Movie> getMoviesByGenre(final String genre) {
    final String _sql = "SELECT * FROM movies WHERE genre = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (genre == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genre);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
      final int _cursorIndexOfReleaseYear = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseYear");
      final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
      final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
      final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
      final int _cursorIndexOfActors = CursorUtil.getColumnIndexOrThrow(_cursor, "actors");
      final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
      final int _cursorIndexOfTrailerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "trailerUrl");
      final int _cursorIndexOfTotalCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCopies");
      final int _cursorIndexOfAvailableCopies = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCopies");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Movie _item;
        _item = new Movie();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpGenre;
        if (_cursor.isNull(_cursorIndexOfGenre)) {
          _tmpGenre = null;
        } else {
          _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
        }
        _item.setGenre(_tmpGenre);
        final int _tmpReleaseYear;
        _tmpReleaseYear = _cursor.getInt(_cursorIndexOfReleaseYear);
        _item.setReleaseYear(_tmpReleaseYear);
        final String _tmpOriginalLanguage;
        if (_cursor.isNull(_cursorIndexOfOriginalLanguage)) {
          _tmpOriginalLanguage = null;
        } else {
          _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
        }
        _item.setOriginalLanguage(_tmpOriginalLanguage);
        final String _tmpCountry;
        if (_cursor.isNull(_cursorIndexOfCountry)) {
          _tmpCountry = null;
        } else {
          _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
        }
        _item.setCountry(_tmpCountry);
        final String _tmpDirector;
        if (_cursor.isNull(_cursorIndexOfDirector)) {
          _tmpDirector = null;
        } else {
          _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
        }
        _item.setDirector(_tmpDirector);
        final String _tmpActors;
        if (_cursor.isNull(_cursorIndexOfActors)) {
          _tmpActors = null;
        } else {
          _tmpActors = _cursor.getString(_cursorIndexOfActors);
        }
        _item.setActors(_tmpActors);
        final String _tmpPosterUrl;
        if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
          _tmpPosterUrl = null;
        } else {
          _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
        }
        _item.setPosterUrl(_tmpPosterUrl);
        final String _tmpTrailerUrl;
        if (_cursor.isNull(_cursorIndexOfTrailerUrl)) {
          _tmpTrailerUrl = null;
        } else {
          _tmpTrailerUrl = _cursor.getString(_cursorIndexOfTrailerUrl);
        }
        _item.setTrailerUrl(_tmpTrailerUrl);
        final int _tmpTotalCopies;
        _tmpTotalCopies = _cursor.getInt(_cursorIndexOfTotalCopies);
        _item.setTotalCopies(_tmpTotalCopies);
        final int _tmpAvailableCopies;
        _tmpAvailableCopies = _cursor.getInt(_cursorIndexOfAvailableCopies);
        _item.setAvailableCopies(_tmpAvailableCopies);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getAvailableCopies(final int movieId) {
    final String _sql = "SELECT availableCopies FROM movies WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, movieId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
