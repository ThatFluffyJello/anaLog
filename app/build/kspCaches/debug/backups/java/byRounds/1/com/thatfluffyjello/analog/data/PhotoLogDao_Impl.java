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
public final class PhotoLogDao_Impl implements PhotoLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PhotoLog> __insertionAdapterOfPhotoLog;

  private final EntityDeletionOrUpdateAdapter<PhotoLog> __deletionAdapterOfPhotoLog;

  private final EntityDeletionOrUpdateAdapter<PhotoLog> __updateAdapterOfPhotoLog;

  public PhotoLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPhotoLog = new EntityInsertionAdapter<PhotoLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `photo_logs` (`id`,`timestamp`,`imagePath`,`latitude`,`longitude`,`cameraModel`,`lensModel`,`filmStock`,`iso`,`aperture`,`shutterSpeed`,`notes`,`rollId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindString(3, entity.getImagePath());
        if (entity.getLatitude() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getLongitude());
        }
        statement.bindString(6, entity.getCameraModel());
        statement.bindString(7, entity.getLensModel());
        statement.bindString(8, entity.getFilmStock());
        statement.bindLong(9, entity.getIso());
        statement.bindString(10, entity.getAperture());
        statement.bindString(11, entity.getShutterSpeed());
        statement.bindString(12, entity.getNotes());
        if (entity.getRollId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getRollId());
        }
      }
    };
    this.__deletionAdapterOfPhotoLog = new EntityDeletionOrUpdateAdapter<PhotoLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `photo_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPhotoLog = new EntityDeletionOrUpdateAdapter<PhotoLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `photo_logs` SET `id` = ?,`timestamp` = ?,`imagePath` = ?,`latitude` = ?,`longitude` = ?,`cameraModel` = ?,`lensModel` = ?,`filmStock` = ?,`iso` = ?,`aperture` = ?,`shutterSpeed` = ?,`notes` = ?,`rollId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindString(3, entity.getImagePath());
        if (entity.getLatitude() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getLongitude());
        }
        statement.bindString(6, entity.getCameraModel());
        statement.bindString(7, entity.getLensModel());
        statement.bindString(8, entity.getFilmStock());
        statement.bindLong(9, entity.getIso());
        statement.bindString(10, entity.getAperture());
        statement.bindString(11, entity.getShutterSpeed());
        statement.bindString(12, entity.getNotes());
        if (entity.getRollId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getRollId());
        }
        statement.bindLong(14, entity.getId());
      }
    };
  }

  @Override
  public Object insertLog(final PhotoLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPhotoLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLog(final PhotoLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPhotoLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLog(final PhotoLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPhotoLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PhotoLog>> getAllLogs() {
    final String _sql = "SELECT * FROM photo_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  @Override
  public Object getLogById(final int id, final Continuation<? super PhotoLog> $completion) {
    final String _sql = "SELECT * FROM photo_logs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PhotoLog>() {
      @Override
      @Nullable
      public PhotoLog call() throws Exception {
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
          final PhotoLog _result;
          if (_cursor.moveToFirst()) {
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
            _result = new PhotoLog(_tmpId,_tmpTimestamp,_tmpImagePath,_tmpLatitude,_tmpLongitude,_tmpCameraModel,_tmpLensModel,_tmpFilmStock,_tmpIso,_tmpAperture,_tmpShutterSpeed,_tmpNotes,_tmpRollId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
