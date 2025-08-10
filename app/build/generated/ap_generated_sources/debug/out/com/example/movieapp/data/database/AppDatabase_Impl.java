package com.example.movieapp.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.movieapp.data.dao.MovieDao;
import com.example.movieapp.data.dao.MovieDao_Impl;
import com.example.movieapp.data.dao.RatingDao;
import com.example.movieapp.data.dao.RatingDao_Impl;
import com.example.movieapp.data.dao.RentalDao;
import com.example.movieapp.data.dao.RentalDao_Impl;
import com.example.movieapp.data.dao.UserDao;
import com.example.movieapp.data.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile MovieDao _movieDao;

  private volatile RentalDao _rentalDao;

  private volatile RatingDao _ratingDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `email` TEXT, `passwordHash` TEXT, `registrationDate` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `movies` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `genre` TEXT, `releaseYear` INTEGER NOT NULL, `originalLanguage` TEXT, `country` TEXT, `director` TEXT, `actors` TEXT, `posterUrl` TEXT, `trailerUrl` TEXT, `totalCopies` INTEGER NOT NULL, `availableCopies` INTEGER NOT NULL, `description` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rentals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `movieId` INTEGER NOT NULL, `rentalDate` TEXT, `returnDate` TEXT, `status` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ratings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `movieId` INTEGER NOT NULL, `rating` REAL NOT NULL, `comment` TEXT, `timestamp` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`movieId`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_ratings_userId_movieId` ON `ratings` (`userId`, `movieId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ratings_userId` ON `ratings` (`userId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ratings_movieId` ON `ratings` (`movieId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0c93f8ff0c2284f262ecc89f2ecc0785')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `movies`");
        db.execSQL("DROP TABLE IF EXISTS `rentals`");
        db.execSQL("DROP TABLE IF EXISTS `ratings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(5);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("registrationDate", new TableInfo.Column("registrationDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.example.movieapp.data.model.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsMovies = new HashMap<String, TableInfo.Column>(13);
        _columnsMovies.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("genre", new TableInfo.Column("genre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("releaseYear", new TableInfo.Column("releaseYear", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("originalLanguage", new TableInfo.Column("originalLanguage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("country", new TableInfo.Column("country", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("director", new TableInfo.Column("director", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("actors", new TableInfo.Column("actors", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("posterUrl", new TableInfo.Column("posterUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("trailerUrl", new TableInfo.Column("trailerUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("totalCopies", new TableInfo.Column("totalCopies", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("availableCopies", new TableInfo.Column("availableCopies", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovies.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovies = new TableInfo("movies", _columnsMovies, _foreignKeysMovies, _indicesMovies);
        final TableInfo _existingMovies = TableInfo.read(db, "movies");
        if (!_infoMovies.equals(_existingMovies)) {
          return new RoomOpenHelper.ValidationResult(false, "movies(com.example.movieapp.data.model.Movie).\n"
                  + " Expected:\n" + _infoMovies + "\n"
                  + " Found:\n" + _existingMovies);
        }
        final HashMap<String, TableInfo.Column> _columnsRentals = new HashMap<String, TableInfo.Column>(6);
        _columnsRentals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("movieId", new TableInfo.Column("movieId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("rentalDate", new TableInfo.Column("rentalDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("returnDate", new TableInfo.Column("returnDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRentals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRentals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRentals = new TableInfo("rentals", _columnsRentals, _foreignKeysRentals, _indicesRentals);
        final TableInfo _existingRentals = TableInfo.read(db, "rentals");
        if (!_infoRentals.equals(_existingRentals)) {
          return new RoomOpenHelper.ValidationResult(false, "rentals(com.example.movieapp.data.model.Rental).\n"
                  + " Expected:\n" + _infoRentals + "\n"
                  + " Found:\n" + _existingRentals);
        }
        final HashMap<String, TableInfo.Column> _columnsRatings = new HashMap<String, TableInfo.Column>(6);
        _columnsRatings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRatings.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRatings.put("movieId", new TableInfo.Column("movieId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRatings.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRatings.put("comment", new TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRatings.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRatings = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRatings.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        _foreignKeysRatings.add(new TableInfo.ForeignKey("movies", "CASCADE", "NO ACTION", Arrays.asList("movieId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRatings = new HashSet<TableInfo.Index>(3);
        _indicesRatings.add(new TableInfo.Index("index_ratings_userId_movieId", true, Arrays.asList("userId", "movieId"), Arrays.asList("ASC", "ASC")));
        _indicesRatings.add(new TableInfo.Index("index_ratings_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        _indicesRatings.add(new TableInfo.Index("index_ratings_movieId", false, Arrays.asList("movieId"), Arrays.asList("ASC")));
        final TableInfo _infoRatings = new TableInfo("ratings", _columnsRatings, _foreignKeysRatings, _indicesRatings);
        final TableInfo _existingRatings = TableInfo.read(db, "ratings");
        if (!_infoRatings.equals(_existingRatings)) {
          return new RoomOpenHelper.ValidationResult(false, "ratings(com.example.movieapp.data.model.Rating).\n"
                  + " Expected:\n" + _infoRatings + "\n"
                  + " Found:\n" + _existingRatings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0c93f8ff0c2284f262ecc89f2ecc0785", "79c805324c210e2e70b05b0608fd1b63");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","movies","rentals","ratings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `movies`");
      _db.execSQL("DELETE FROM `rentals`");
      _db.execSQL("DELETE FROM `ratings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MovieDao.class, MovieDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RentalDao.class, RentalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RatingDao.class, RatingDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public MovieDao movieDao() {
    if (_movieDao != null) {
      return _movieDao;
    } else {
      synchronized(this) {
        if(_movieDao == null) {
          _movieDao = new MovieDao_Impl(this);
        }
        return _movieDao;
      }
    }
  }

  @Override
  public RentalDao rentalDao() {
    if (_rentalDao != null) {
      return _rentalDao;
    } else {
      synchronized(this) {
        if(_rentalDao == null) {
          _rentalDao = new RentalDao_Impl(this);
        }
        return _rentalDao;
      }
    }
  }

  @Override
  public RatingDao ratingDao() {
    if (_ratingDao != null) {
      return _ratingDao;
    } else {
      synchronized(this) {
        if(_ratingDao == null) {
          _ratingDao = new RatingDao_Impl(this);
        }
        return _ratingDao;
      }
    }
  }
}
