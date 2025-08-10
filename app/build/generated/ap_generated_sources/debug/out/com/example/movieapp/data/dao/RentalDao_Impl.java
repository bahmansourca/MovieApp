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
import com.example.movieapp.data.model.Rental;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RentalDao_Impl implements RentalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Rental> __insertionAdapterOfRental;

  private final EntityDeletionOrUpdateAdapter<Rental> __updateAdapterOfRental;

  private final SharedSQLiteStatement __preparedStmtOfReturnRental;

  public RentalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRental = new EntityInsertionAdapter<Rental>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `rentals` (`id`,`userId`,`movieId`,`rentalDate`,`returnDate`,`status`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rental entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getMovieId());
        if (entity.getRentalDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRentalDate());
        }
        if (entity.getReturnDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getReturnDate());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
      }
    };
    this.__updateAdapterOfRental = new EntityDeletionOrUpdateAdapter<Rental>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rentals` SET `id` = ?,`userId` = ?,`movieId` = ?,`rentalDate` = ?,`returnDate` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Rental entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getMovieId());
        if (entity.getRentalDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRentalDate());
        }
        if (entity.getReturnDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getReturnDate());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfReturnRental = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE rentals SET returnDate = ?, status = 'RETURNED' WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insertRental(final Rental rental) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfRental.insertAndReturnId(rental);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRental(final Rental rental) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRental.handle(rental);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void returnRental(final int rentalId, final String returnDate) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfReturnRental.acquire();
    int _argIndex = 1;
    if (returnDate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, returnDate);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, rentalId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfReturnRental.release(_stmt);
    }
  }

  @Override
  public List<Rental> getRentalsByUserId(final int userId) {
    final String _sql = "SELECT * FROM rentals WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRentalDate = CursorUtil.getColumnIndexOrThrow(_cursor, "rentalDate");
      final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<Rental> _result = new ArrayList<Rental>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rental _item;
        _item = new Rental();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final String _tmpRentalDate;
        if (_cursor.isNull(_cursorIndexOfRentalDate)) {
          _tmpRentalDate = null;
        } else {
          _tmpRentalDate = _cursor.getString(_cursorIndexOfRentalDate);
        }
        _item.setRentalDate(_tmpRentalDate);
        final String _tmpReturnDate;
        if (_cursor.isNull(_cursorIndexOfReturnDate)) {
          _tmpReturnDate = null;
        } else {
          _tmpReturnDate = _cursor.getString(_cursorIndexOfReturnDate);
        }
        _item.setReturnDate(_tmpReturnDate);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Rental> getActiveRentalsByUserId(final int userId) {
    final String _sql = "SELECT * FROM rentals WHERE userId = ? AND status = 'ACTIVE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRentalDate = CursorUtil.getColumnIndexOrThrow(_cursor, "rentalDate");
      final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<Rental> _result = new ArrayList<Rental>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Rental _item;
        _item = new Rental();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final String _tmpRentalDate;
        if (_cursor.isNull(_cursorIndexOfRentalDate)) {
          _tmpRentalDate = null;
        } else {
          _tmpRentalDate = _cursor.getString(_cursorIndexOfRentalDate);
        }
        _item.setRentalDate(_tmpRentalDate);
        final String _tmpReturnDate;
        if (_cursor.isNull(_cursorIndexOfReturnDate)) {
          _tmpReturnDate = null;
        } else {
          _tmpReturnDate = _cursor.getString(_cursorIndexOfReturnDate);
        }
        _item.setReturnDate(_tmpReturnDate);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getActiveRentalCount(final int userId) {
    final String _sql = "SELECT COUNT(*) FROM rentals WHERE userId = ? AND status = 'ACTIVE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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
  public int getTotalRentalCount(final int userId) {
    final String _sql = "SELECT COUNT(*) FROM rentals WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
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
  public int checkIfUserHasActiveRental(final int userId, final int movieId) {
    final String _sql = "SELECT COUNT(*) FROM rentals WHERE userId = ? AND movieId = ? AND status = 'ACTIVE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
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
  public Rental getRentalById(final int rentalId) {
    final String _sql = "SELECT * FROM rentals WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rentalId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "movieId");
      final int _cursorIndexOfRentalDate = CursorUtil.getColumnIndexOrThrow(_cursor, "rentalDate");
      final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final Rental _result;
      if (_cursor.moveToFirst()) {
        _result = new Rental();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpMovieId;
        _tmpMovieId = _cursor.getInt(_cursorIndexOfMovieId);
        _result.setMovieId(_tmpMovieId);
        final String _tmpRentalDate;
        if (_cursor.isNull(_cursorIndexOfRentalDate)) {
          _tmpRentalDate = null;
        } else {
          _tmpRentalDate = _cursor.getString(_cursorIndexOfRentalDate);
        }
        _result.setRentalDate(_tmpRentalDate);
        final String _tmpReturnDate;
        if (_cursor.isNull(_cursorIndexOfReturnDate)) {
          _tmpReturnDate = null;
        } else {
          _tmpReturnDate = _cursor.getString(_cursorIndexOfReturnDate);
        }
        _result.setReturnDate(_tmpReturnDate);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
      } else {
        _result = null;
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
