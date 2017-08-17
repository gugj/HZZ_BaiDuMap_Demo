package com.roch.hzz_baidumap_demo.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LAT_LON_ENTITY".
*/
public class LatLonEntityDao extends AbstractDao<LatLonEntity, Long> {

    public static final String TABLENAME = "LAT_LON_ENTITY";

    /**
     * Properties of entity LatLonEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Latitude = new Property(1, double.class, "latitude", false, "LATITUDE");
        public final static Property Lontitude = new Property(2, double.class, "lontitude", false, "LONTITUDE");
        public final static Property Time = new Property(3, String.class, "time", false, "TIME");
    };


    public LatLonEntityDao(DaoConfig config) {
        super(config);
    }
    
    public LatLonEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LAT_LON_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"LATITUDE\" REAL NOT NULL ," + // 1: latitude
                "\"LONTITUDE\" REAL NOT NULL ," + // 2: lontitude
                "\"TIME\" TEXT);"); // 3: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LAT_LON_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LatLonEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getLatitude());
        stmt.bindDouble(3, entity.getLontitude());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(4, time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LatLonEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getLatitude());
        stmt.bindDouble(3, entity.getLontitude());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(4, time);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LatLonEntity readEntity(Cursor cursor, int offset) {
        LatLonEntity entity = new LatLonEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getDouble(offset + 1), // latitude
            cursor.getDouble(offset + 2), // lontitude
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LatLonEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLatitude(cursor.getDouble(offset + 1));
        entity.setLontitude(cursor.getDouble(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LatLonEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LatLonEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}