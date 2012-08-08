package com.hy.bills.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.AccountBook;
import com.hy.bills.domain.User;

public class AccountBookService extends DaoSupport<AccountBook> {
	private static final String TAG = "AccountBookService";

	public static void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "createTable");
		db.execSQL("create table AccountBook(id integer primary key autoincrement, name varchar(20) not null, status integer not null, isDefault integer not null, createDate long not null)");

		// Insert testing data
		String sql = "insert into AccountBook(name, status, isDefault, createDate) values(?, ?, ?, ?)";
		for (int i = 0; i < 10; i++) {
			AccountBook accountBook = new AccountBook();
			accountBook.setName("accountbook" + i);
			db.execSQL(sql, new Object[] { accountBook.getName(), accountBook.getStatus(), accountBook.isDefault(), System.currentTimeMillis() });
		}
	}

	@Override
	public void save(AccountBook accountBook) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "insert into AccountBook(name, status, isDefault, createDate) values(?, ?, ?, ?)";
		db.execSQL(sql, new Object[] { accountBook.getName(), accountBook.getStatus(), accountBook.isDefault(), System.currentTimeMillis() });
	}

	@Override
	public void update(AccountBook accountBook) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "update AccountBook set name=?, status=?, isDefault=? where id=?";
		db.execSQL(sql, new Object[] { accountBook.getName(), accountBook.getStatus(), accountBook.isDefault(), accountBook.getId() });
	}

	@Override
	public void delete(Integer id) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "delete from AccountBook where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	@Override
	public AccountBook find(Integer id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from AccountBook where id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { id.toString() });
		try {
			AccountBook accountBook = null;
			if (cursor.moveToFirst()) {
				accountBook = parseModel(cursor);
			}
				return accountBook;
		} finally {
			cursor.close();
		}
	}

	@Override
	public List<AccountBook> findAll() {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from AccountBook";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			List<AccountBook> accountBookList = new ArrayList<AccountBook>();
			while (cursor.moveToNext()) {
				accountBookList.add(parseModel(cursor));
			}

			return accountBookList;
		} finally {
			cursor.close();
		}
	}

	@Override
	public List<AccountBook> getScrollData(Integer offset, Integer maxResult) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private AccountBook parseModel(Cursor cursor) {
		AccountBook accountBook = null;
		accountBook = new AccountBook();
		accountBook.setId(cursor.getInt(cursor.getColumnIndex("id")));
		accountBook.setName(cursor.getString(cursor.getColumnIndex("name")));
		accountBook.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
		accountBook.setDefault(cursor.getInt(cursor.getColumnIndex("isDefault")));
		accountBook.setCreateDate(new Date(cursor.getLong(cursor.getColumnIndex("createDate"))));

		return accountBook;
	}
}
