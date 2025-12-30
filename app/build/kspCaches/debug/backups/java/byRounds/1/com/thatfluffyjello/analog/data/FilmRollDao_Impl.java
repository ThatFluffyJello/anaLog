package com.thatfluffyjello.analog.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FilmRollDao_Impl implements FilmRollDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FilmRoll> __insertionAdapterOfFilmRoll;

  private final EntityDeletionOrUpdateAdapter<FilmRoll> __updateAdapterOfFilmRoll;

  public FilmRollDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFilmRoll = new EntityInsertionAdapter<FilmRoll>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `film_rolls` (`id`,`name`,`cameraModel`,`lensModel`,`filmStock`,`iso`,`dateStarted`,`dateCompleted`,`isActive`,`cameraId`,`exposures`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FilmRoll entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCameraModel());
        statement.bindString(4, entity.getLensModel());
        statement.bindString(5, entity.getFilmStock());
        statement.bindLong(6, entity.getIso());
        statement.bindLong(7, entity.getDateStarted());
        if (entity.getDateCompleted() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDateCompleted());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getCameraId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCameraId());
        }
        statement.bindLong(11, entity.getExposures());
      }
    };
    this.__updateAdapterOfFilmRoll = new EntityDeletionOrUpdateAdapter<FilmRoll>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `film_rolls` SET `id` = ?,`name` = ?,`cameraModel` = ?,`lensModel` = ?,`filmStock` = ?,`iso` = ?,`dateStarted` = ?,`dateCompleted` = ?,`isActive` = ?,`cameraId` = ?,`exposures` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FilmRoll entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCameraModel());
        statement.bindString(4, entity.getLensModel());
        statement.bindString(5, entity.getFilmStock());
        statement.bindLong(6, entity.getIso());
        statement.bindLong(7, entity.getDateStarted());
        if (entity.getDateCompleted() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDateCompleted());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getCameraId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCameraId());
        }
        statement.bindLong(11, entity.getExposures());
        statement.bindLong(12, entity.getId());
      }
    };
  }

  @Override
  public Object insertRoll(final FilmRoll roll, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFilmRoll.insert(roll);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRoll(final FilmRoll roll, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFilmRoll.handle(roll);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FilmRoll>> getAllRolls() {
    final String _sql = "SELECT * FROM film_rolls ORDER BY dateStarted DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"film_rolls"}, new Callable<List<FilmRoll>>() {
      @Override
      @NonNull
      public List<FilmRoll> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCameraModel = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraModel");
          final int _cursorIndexOfLensModel = CursorUtil.getColumnIndexOrThrow(_cursor, "lensModel");
          final int _cursorIndexOfFilmStock = CursorUtil.getColumnIndexOrThrow(_cursor, "filmStock");
          final int _cursorIndexOfIso = CursorUtil.getColumnIndexOrThrow(_cursor, "iso");
          final int _cursorIndexOfDateStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateStarted");
          final int _cursorIndexOfDateCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateCompleted");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCameraId = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraId");
          final int _cursorIndexOfExposures = CursorUtil.getColumnIndexOrThrow(_cursor, "exposures");
          final List<FilmRoll> _result = new ArrayList<FilmRoll>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FilmRoll _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCameraModel;
            _tmpCameraModel = _cursor.getString(_cursorIndexOfCameraModel);
            final String _tmpLensModel;
            _tmpLensModel = _cursor.getString(_cursorIndexOfLensModel);
            final String _tmpFilmStock;
            _tmpFilmStock = _cursor.getString(_cursorIndexOfFilmStock);
            final int _tmpIso;
            _tmpIso = _cursor.getInt(_cursorIndexOfIso);
            final long _tmpDateStarted;
            _tmpDateStarted = _cursor.getLong(_cursorIndexOfDateStarted);
            final Long _tmpDateCompleted;
            if (_cursor.isNull(_cursorIndexOfDateCompleted)) {
              _tmpDateCompleted = null;
            } else {
              _tmpDateCompleted = _cursor.getLong(_cursorIndexOfDateCompleted);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final Integer _tmpCameraId;
            if (_cursor.isNull(_cursorIndexOfCameraId)) {
              _tmpCameraId = null;
            } else {
              _tmpCameraId = _cursor.getInt(_cursorIndexOfCameraId);
            }
            final int _tmpExposures;
            _tmpExposures = _cursor.getInt(_cursorIndexOfExposures);
            _item = new FilmRoll(_tmpId,_tmpName,_tmpCameraModel,_tmpLensModel,_tmpFilmStock,_tmpIso,_tmpDateStarted,_tmpDateCompleted,_tmpIsActive,_tmpCameraId,_tmpExposures);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRollById(final int id, final Continuation<? super FilmRoll> $completion) {
    final String _sql = "SELECT * FROM film_rolls WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FilmRoll>() {
      @Override
      @Nullable
      public FilmRoll call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCameraModel = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraModel");
          final int _cursorIndexOfLensModel = CursorUtil.getColumnIndexOrThrow(_cursor, "lensModel");
          final int _cursorIndexOfFilmStock = CursorUtil.getColumnIndexOrThrow(_cursor, "filmStock");
          final int _cursorIndexOfIso = CursorUtil.getColumnIndexOrThrow(_cursor, "iso");
          final int _cursorIndexOfDateStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateStarted");
          final int _cursorIndexOfDateCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateCompleted");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCameraId = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraId");
          final int _cursorIndexOfExposures = CursorUtil.getColumnIndexOrThrow(_cursor, "exposures");
          final FilmRoll _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCameraModel;
            _tmpCameraModel = _cursor.getString(_cursorIndexOfCameraModel);
            final String _tmpLensModel;
            _tmpLensModel = _cursor.getString(_cursorIndexOfLensModel);
            final String _tmpFilmStock;
            _tmpFilmStock = _cursor.getString(_cursorIndexOfFilmStock);
            final int _tmpIso;
            _tmpIso = _cursor.getInt(_cursorIndexOfIso);
            final long _tmpDateStarted;
            _tmpDateStarted = _cursor.getLong(_cursorIndexOfDateStarted);
            final Long _tmpDateCompleted;
            if (_cursor.isNull(_cursorIndexOfDateCompleted)) {
              _tmpDateCompleted = null;
            } else {
              _tmpDateCompleted = _cursor.getLong(_cursorIndexOfDateCompleted);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final Integer _tmpCameraId;
            if (_cursor.isNull(_cursorIndexOfCameraId)) {
              _tmpCameraId = null;
            } else {
              _tmpCameraId = _cursor.getInt(_cursorIndexOfCameraId);
            }
            final int _tmpExposures;
            _tmpExposures = _cursor.getInt(_cursorIndexOfExposures);
            _result = new FilmRoll(_tmpId,_tmpName,_tmpCameraModel,_tmpLensModel,_tmpFilmStock,_tmpIso,_tmpDateStarted,_tmpDateCompleted,_tmpIsActive,_tmpCameraId,_tmpExposures);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FilmRoll>> getActiveRolls() {
    final String _sql = "SELECT * FROM film_rolls WHERE isActive = 1 ORDER BY dateStarted DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"film_rolls"}, new Callable<List<FilmRoll>>() {
      @Override
      @NonNull
      public List<FilmRoll> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCameraModel = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraModel");
          final int _cursorIndexOfLensModel = CursorUtil.getColumnIndexOrThrow(_cursor, "lensModel");
          final int _cursorIndexOfFilmStock = CursorUtil.getColumnIndexOrThrow(_cursor, "filmStock");
          final int _cursorIndexOfIso = CursorUtil.getColumnIndexOrThrow(_cursor, "iso");
          final int _cursorIndexOfDateStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateStarted");
          final int _cursorIndexOfDateCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "dateCompleted");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCameraId = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraId");
          final int _cursorIndexOfExposures = CursorUtil.getColumnIndexOrThrow(_cursor, "exposures");
          final List<FilmRoll> _result = new ArrayList<FilmRoll>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FilmRoll _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCameraModel;
            _tmpCameraModel = _cursor.getString(_cursorIndexOfCameraModel);
            final String _tmpLensModel;
            _tmpLensModel = _cursor.getString(_cursorIndexOfLensModel);
            final String _tmpFilmStock;
            _tmpFilmStock = _cursor.getString(_cursorIndexOfFilmStock);
            final int _tmpIso;
            _tmpIso = _cursor.getInt(_cursorIndexOfIso);
            final long _tmpDateStarted;
            _tmpDateStarted = _cursor.getLong(_cursorIndexOfDateStarted);
            final Long _tmpDateCompleted;
            if (_cursor.isNull(_cursorIndexOfDateCompleted)) {
              _tmpDateCompleted = null;
            } else {
              _tmpDateCompleted = _cursor.getLong(_cursorIndexOfDateCompleted);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final Integer _tmpCameraId;
            if (_cursor.isNull(_cursorIndexOfCameraId)) {
              _tmpCameraId = null;
            } else {
              _tmpCameraId = _cursor.getInt(_cursorIndexOfCameraId);
            }
            final int _tmpExposures;
            _tmpExposures = _cursor.getInt(_cursorIndexOfExposures);
            _item = new FilmRoll(_tmpId,_tmpName,_tmpCameraModel,_tmpLensModel,_tmpFilmStock,_tmpIso,_tmpDateStarted,_tmpDateCompleted,_tmpIsActive,_tmpCameraId,_tmpExposures);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PhotoLog>> getLogsForRoll(final int rollId) {
    final String _sql = "SELECT * FROM photo_logs WHERE rollId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rollId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"photo_logs"}, new Callable<List<PhotoLog>>() {
      @Override
      @NonNull
      public List<PhotoLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfCameraModel = CursorUtil.getColumnIndexOrThrow(_cursor, "cameraModel");
          final int _cursorIndexOfLensModel = CursorUtil.getColumnIndexOrThrow(_cursor, "lensModel");
          final int _cursorIndexOfFilmStock = CursorUtil.getColumnIndexOrThrow(_cursor, "filmStock");
          final int _cursorIndexOfIso = CursorUtil.getColumnIndexOrThrow(_cursor, "iso");
          final int _cursorIndexOfAperture = CursorUtil.getColumnIndexOrThrow(_cursor, "aperture");
          final int _cursorIndexOfShutterSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "shutterSpeed");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfRollId = CursorUtil.getColumnIndexOrThrow(_cursor, "rollId");
          final List<PhotoLog> _result = new ArrayList<PhotoLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PhotoLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpCameraModel;
            _tmpCameraModel = _cursor.getString(_cursorIndexOfCameraModel);
            final String _tmpLensModel;
            _tmpLensModel = _cursor.getString(_cursorIndexOfLensModel);
            final String _tmpFilmStock;
            _tmpFilmStock = _cursor.getString(_cursorIndexOfFilmStock);
            final int _tmpIso;
            _tmpIso = _cursor.getInt(_cursorIndexOfIso);
            final String _tmpAperture;
            _tmpAperture = _cursor.getString(_cursorIndexOfAperture);
            final String _tmpShutterSpeed;
            _tmpShutterSpeed = _cursor.getString(_cursorIndexOfShutterSpeed);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final Integer _tmpRollId;
            if (_cursor.isNull(_cursorIndexOfRollId)) {
              _tmpRollId = null;
            } else {
              _tmpRollId = _cursor.getInt(_cursorIndexOfRollId);
            }
            _item = new PhotoLog(_tmpId,_tmpTimestamp,_tmpImagePath,_tmpLatitude,_tmpLongitude,_tmpCameraModel,_tmpLensModel,_tmpFilmStock,_tmpIso,_tmpAperture,_tmpShutterSpeed,_tmpNotes,_tmpRollId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
