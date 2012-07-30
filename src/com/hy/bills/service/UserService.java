package com.hy.bills.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.User;

public class UserService extends DaoSupport<User> {
	private static final String TAG = "UserService";

	public static void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "createTable");
		db.execSQL("create table User(id integer primary key autoincrement, name varchar(20) not null, status integer not null, createDate long not null)");
		
		// Insert testing data
		String sql = "insert into User(name, status, createDate) values(?, ?, ?)";
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setName("User" + i);
			db.execSQL(sql, new Object[] { user.getName(), user.getStatus(), System.currentTimeMillis() });
		}
	}

	@Override
	public void save(User user) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "insert into User(name, status, createDate) values(?, ?, ?)";
		db.execSQL(sql, new Object[] { user.getName(), user.getStatus(), System.currentTimeMillis() });
	}

	@Override
	public void update(User user) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "update User set name=?, status=? where id=?";
		db.execSQL(sql, new Object[] { user.getName(), user.getStatus(), user.getId() });
	}

	@Override
	public void delete(Integer id) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "delete User where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	@Override
	public User find(Integer id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from User where id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { id.toString() });
		try {
			User user = parseModel(cursor);
			
			return user;
		} finally {
			cursor.close();
		}
	}

	@Override
	public List<User> getScrollData(Integer offset, Integer maxResult) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from User limit ?,?";
		Cursor cursor = db.rawQuery(sql, new String[] { offset.toString(), maxResult.toString() });
		try {
			List<User> userList = new ArrayList<User>();
			while (cursor.moveToNext()) {
				userList.add(parseModel(cursor));
			}
			
			return userList;
		} finally {
			cursor.close();
		}
	}

	private User parseModel(Cursor cursor) {
		User user = null;
		if (cursor.moveToFirst()) {
			user = new User();
			user.setId(cursor.getInt(cursor.getColumnIndex("id")));
			user.setName(cursor.getString(cursor.getColumnIndex("name")));
			user.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			user.setCreateDate(new Date(cursor.getLong(cursor.getColumnIndex("createDate"))));
		}

		return user;
	}

	@Override
	public List<User> findAll() {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from User";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			List<User> userList = new ArrayList<User>();
			while (cursor.moveToNext()) {
				userList.add(parseModel(cursor));
			}
			
			return userList;
		} finally {
			cursor.close();
		}
	}
}
