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
import com.example.movieapp.data.model.Rating;
import java.lang.Class;
import java.lang.Float;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RatingDao_Impl implements RatingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Rating> __insertionAdapterOfRating;

  private final EntityDeletionOrUpdateAdapter<Rating> __deletionAdapterOfRating;

  private final EntityDeletionOrUpdateAdapter<Rating> __updateAdapterOfRating;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRatingByUserAndMovie;

  public RatingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRating = new EntityInsertionAdapter<Rating>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ratings` (`id`,`userId`,`movieId`,`rating`,`comment`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rating entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getMovieId());
        statement.bindDouble(4, entity.getRating());
        if (entity.getComment() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getComment());
        }
        statement.bindLong(6, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfRating = new EntityDeletionOrUpdateAdapter<Rating>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `ratings` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rating entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRating = new EntityDeletionOrUpdateAdapter<Rating>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `ratings` SET `id` = ?,`userId` = ?,`movieId` = ?,`rating` = ?,`comment` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rating entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getMovieId());
        statement.bindDouble(4, entity.getRating());
        if (entity.getComment() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getComment());
        }
        statement.bindLong(6, entity.getTimestamp());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteRatingByUserAndMovie = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ratings WHERE userId = ? AND movieId = ?";
        return _query;
      }
    };
  }

  @Override
  public long insertRating(final Rating rating) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfRating.insertAndReturnId(rating);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRating(final Rating rating) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRating.handle(rating);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRating(final Rating rating) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRating.handle(rating);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRatingByUserAndMovie(final int userId, final int movieId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRatingByUserAndMovie.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, userId);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, movieId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteRatingByUserAndMovie.release(_stmt);
    }
  }

  @Override
  public Rating getRatingByUserAndMovie(final int userId, final int movieId) {
    final String _sql = "SELECT * FROM ratings WHERE userId = ? AND movieId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, movieId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final Rating _result;
      if (_cursor.moveToFirst()) {
        _result = new Rating();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _result.setMovieId(_tmpMovieId);
        final float _tmpRating;
        _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
        _result.setRating(_tmpRating);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _result.setComment(_tmpComment);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _result.setTimestamp(_tmpTimestamp);
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
  public List<Rating> getRatingsByMovie(final int movieId) {
    final String _sql = "SELECT * FROM ratings WHERE movieId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, movieId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Rating> _result = new ArrayList<Rating>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rating _item;
        _item = new Rating();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final float _tmpRating;
        _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Rating> getRatingsByUser(final int userId) {
    final String _sql = "SELECT * FROM ratings WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Rating> _result = new ArrayList<Rating>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rating _item;
        _item = new Rating();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final float _tmpRating;
        _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Float getAverageRatingForMovie(final int movieId) {
    final String _sql = "SELECT AVG(rating) FROM ratings WHERE movieId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, movieId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Float _result;
      if (_cursor.moveToFirst()) {
        final Float _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getFloat(0);
        }
        _result = _tmp;
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
  public int getRatingCountForMovie(final int movieId) {
    final String _sql = "SELECT COUNT(*) FROM ratings WHERE movieId = ?";
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

  @Override
  public List<Rating> getAllRatings() {
    final String _sql = "SELECT * FROM ratings ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Rating> _result = new ArrayList<Rating>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rating _item;
        _item = new Rating();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final float _tmpRating;
        _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Rating> getRecentRatingsByUser(final int userId, final int limit) {
    final String _sql = "SELECT * FROM ratings WHERE userId = ? ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Rating> _result = new ArrayList<Rating>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rating _item;
        _item = new Rating();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final float _tmpRating;
        _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Rating> getHighRatings(final float minRating) {
    final String _sql = "SELECT * FROM ratings WHERE rating >= ? ORDER BY rating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindDouble(_argIndex, minRating);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Rating> _result = new ArrayList<Rating>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rating _item;
        _item = new Rating();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final float _tmpRating;
        _tmpRating = _cursor.getFloat(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final String _tmpComment;
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _tmpComment = null;
        } else {
          _tmpComment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.setComment(_tmpComment);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RatingDao.MovieRatingAverage> getTopRatedMovies(final int minCount) {
    final String _sql = "SELECT movieId, AVG(rating) as avgRating FROM ratings GROUP BY movieId HAVING COUNT(*) >= ? ORDER BY avgRating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, minCount);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieId = 0;
      final int _cursorIndexOfAvgRating = 1;
      final List<RatingDao.MovieRatingAverage> _result = new ArrayList<RatingDao.MovieRatingAverage>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RatingDao.MovieRatingAverage _item;
        _item = new RatingDao.MovieRatingAverage();
        _item.movieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.avgRating = _cursor.getFloat(_cursorIndexOfAvgRating);
        _result.add(_item);
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
