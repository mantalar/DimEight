package com.example.dimeight.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.dimeight.dao.FriendDao;
import com.example.dimeight.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendDB extends SQLiteOpenHelper implements FriendDao {
    public static final String DBNAME = "vsga.db";
    public static final int DBVERSION = 2;

    public FriendDB(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists friend(" +
                "id integer primary key autoincrement," +
                "name text," +
                "address text," +
                "phone text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("drop table if exists friend");
            onCreate(db);
        }
    }

    @Override
    public long insert(Friend f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull("id");
        values.put("name", f.getName());
        values.put("address", f.getAddress());
        values.put("phone", f.getPhone());
        return db.insert("friend", null, values);
    }

    @Override
    public void update(Friend f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", f.getId());
        values.put("name", f.getName());
        values.put("address", f.getAddress());
        values.put("phone", f.getPhone());
        db.update("friend", values, "id=?", new String[]{String.valueOf(f.getId())});
    }

    @Override
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("friend", "id=?", new String[]{String.valueOf(id)});
    }

    @Override
    public Friend getAFriendById(int id) {
        Friend result = null;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("friend", null, "id=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if(c.moveToFirst())
                result = new Friend(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)
                );
        }
        return result;
    }

    @Override
    public List<Friend> getAllFriends() {
        List<Friend> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("friend", null, null, null, null, null, null)) {
            if(c.moveToNext())
                result.add(new Friend(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)));
        }
        return result;
    }
}
