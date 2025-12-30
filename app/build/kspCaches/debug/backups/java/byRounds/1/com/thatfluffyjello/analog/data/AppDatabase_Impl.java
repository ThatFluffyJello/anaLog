package com.thatfluffyjello.analog.data;

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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PhotoLogDao _photoLogDao;

  private volatile FilmRollDao _filmRollDao;

  private volatile CameraDao _cameraDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `photo_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `imagePath` TEXT NOT NULL, `latitude` REAL, `longitude` REAL, `cameraModel` TEXT NOT NULL, `lensModel` TEXT NOT NULL, `filmStock` TEXT NOT NULL, `iso` INTEGER NOT NULL, `aperture` TEXT NOT NULL, `shutterSpeed` TEXT NOT NULL, `notes` TEXT NOT NULL, `rollId` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `film_rolls` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `cameraModel` TEXT NOT NULL, `lensModel` TEXT NOT NULL, `filmStock` TEXT NOT NULL, `iso` INTEGER NOT NULL, `dateStarted` INTEGER NOT NULL, `dateCompleted` INTEGER, `isActive` INTEGER NOT NULL, `cameraId` INTEGER, `exposures` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cameras` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `type` TEXT NOT NULL, `format` TEXT NOT NULL, `year` INTEGER, `lenses` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '40bff2370a408f53ef7829789dc91bcf')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `photo_logs`");
        db.execSQL("DROP TABLE IF EXISTS `film_rolls`");
        db.execSQL("DROP TABLE IF EXISTS `cameras`");
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
        final HashMap<String, TableInfo.Column> _columnsPhotoLogs = new HashMap<String, TableInfo.Column>(13);
        _columnsPhotoLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("imagePath", new TableInfo.Column("imagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("cameraModel", new TableInfo.Column("cameraModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("lensModel", new TableInfo.Column("lensModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("filmStock", new TableInfo.Column("filmStock", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("iso", new TableInfo.Column("iso", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("aperture", new TableInfo.Column("aperture", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("shutterSpeed", new TableInfo.Column("shutterSpeed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoLogs.put("rollId", new TableInfo.Column("rollId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPhotoLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPhotoLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPhotoLogs = new TableInfo("photo_logs", _columnsPhotoLogs, _foreignKeysPhotoLogs, _indicesPhotoLogs);
        final TableInfo _existingPhotoLogs = TableInfo.read(db, "photo_logs");
        if (!_infoPhotoLogs.equals(_existingPhotoLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "photo_logs(com.thatfluffyjello.analog.data.PhotoLog).\n"
                  + " Expected:\n" + _infoPhotoLogs + "\n"
                  + " Found:\n" + _existingPhotoLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsFilmRolls = new HashMap<String, TableInfo.Column>(11);
        _columnsFilmRolls.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("cameraModel", new TableInfo.Column("cameraModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("lensModel", new TableInfo.Column("lensModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("filmStock", new TableInfo.Column("filmStock", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("iso", new TableInfo.Column("iso", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("dateStarted", new TableInfo.Column("dateStarted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("dateCompleted", new TableInfo.Column("dateCompleted", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("cameraId", new TableInfo.Column("cameraId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFilmRolls.put("exposures", new TableInfo.Column("exposures", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFilmRolls = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFilmRolls = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFilmRolls = new TableInfo("film_rolls", _columnsFilmRolls, _foreignKeysFilmRolls, _indicesFilmRolls);
        final TableInfo _existingFilmRolls = TableInfo.read(db, "film_rolls");
        if (!_infoFilmRolls.equals(_existingFilmRolls)) {
          return new RoomOpenHelper.ValidationResult(false, "film_rolls(com.thatfluffyjello.analog.data.FilmRoll).\n"
                  + " Expected:\n" + _infoFilmRolls + "\n"
                  + " Found:\n" + _existingFilmRolls);
        }
        final HashMap<String, TableInfo.Column> _columnsCameras = new HashMap<String, TableInfo.Column>(7);
        _columnsCameras.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCameras.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCameras.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCameras.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCameras.put("format", new TableInfo.Column("format", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCameras.put("year", new TableInfo.Column("year", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCameras.put("lenses", new TableInfo.Column("lenses", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCameras = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCameras = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCameras = new TableInfo("cameras", _columnsCameras, _foreignKeysCameras, _indicesCameras);
        final TableInfo _existingCameras = TableInfo.read(db, "cameras");
        if (!_infoCameras.equals(_existingCameras)) {
          return new RoomOpenHelper.ValidationResult(false, "cameras(com.thatfluffyjello.analog.data.Camera).\n"
                  + " Expected:\n" + _infoCameras + "\n"
                  + " Found:\n" + _existingCameras);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "40bff2370a408f53ef7829789dc91bcf", "c86827a6143d27e07e6b75a4d6ff3fe6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "photo_logs","film_rolls","cameras");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `photo_logs`");
      _db.execSQL("DELETE FROM `film_rolls`");
      _db.execSQL("DELETE FROM `cameras`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(PhotoLogDao.class, PhotoLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FilmRollDao.class, FilmRollDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CameraDao.class, CameraDao_Impl.getRequiredConverters());
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
  public PhotoLogDao photoLogDao() {
    if (_photoLogDao != null) {
      return _photoLogDao;
    } else {
      synchronized(this) {
        if(_photoLogDao == null) {
          _photoLogDao = new PhotoLogDao_Impl(this);
        }
        return _photoLogDao;
      }
    }
  }

  @Override
  public FilmRollDao filmRollDao() {
    if (_filmRollDao != null) {
      return _filmRollDao;
    } else {
      synchronized(this) {
        if(_filmRollDao == null) {
          _filmRollDao = new FilmRollDao_Impl(this);
        }
        return _filmRollDao;
      }
    }
  }

  @Override
  public CameraDao cameraDao() {
    if (_cameraDao != null) {
      return _cameraDao;
    } else {
      synchronized(this) {
        if(_cameraDao == null) {
          _cameraDao = new CameraDao_Impl(this);
        }
        return _cameraDao;
      }
    }
  }
}
