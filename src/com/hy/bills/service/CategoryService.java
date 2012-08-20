package com.hy.bills.service;

import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.Category;
import com.hy.bills.domain.User;

public class CategoryService extends DaoSupport<Category> {
	private static final String TAG = "CategoryService";

	public static void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "createTable");

		db.execSQL("create table Category(id integer primary key autoincrement, name varchar(20) not null, status integer not null, type integer not null, parentId integer not null, createDate long not null)");
		// Insert testing data
		String sql = "insert into Category(name, status, type, parentId createDate) values(?, ?, ?, ?, ?)";
		for (int i = 0; i < 10; i++) {
			Category category = new Category();
			category.setName("Category" + i);
			db.execSQL(sql,
					new Object[] { category.getName(), category.getStatus(), category.getType(),
							category.getParentId(), System.currentTimeMillis() });
		}
	}

	@Override
	public void save(Category category) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "insert into Category(name, status, type, parentId createDate) values(?, ?, ?, ?, ?)";
		db.execSQL(sql,
				new Object[] { category.getName(), category.getStatus(), category.getType(), category.getParentId(),
						System.currentTimeMillis() });
	}

	@Override
	public void update(Category category) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "update Category set name=?, status=?, type=?, parentId=? where id=?";
		db.execSQL(sql,
				new Object[] { category.getName(), category.getStatus(), category.getType(), category.getParentId(),
						category.getId() });
	}

	@Override
	public void delete(Integer id) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "delete from Category where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	@Override
	public Category find(Integer id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Category where id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { id.toString() });
		try {
			Category category = null;
			if (cursor.moveToFirst()) {
				category = parseModel(cursor);
			}
			return category;
		} finally {
			cursor.close();
		}
	}

	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getScrollData(Integer offset, Integer maxResult) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteChildrenByParentId(Integer id) {
		// TODO Auto-generated method stub

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Category> findAllRootCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Category> findAllCategoriesByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getChildrenCountByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private Category parseModel(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getInt(cursor.getColumnIndex("id")));
		category.setName(cursor.getString(cursor.getColumnIndex("name")));
		category.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
		category.setType(cursor.getInt(cursor.getColumnIndex("type")));
		category.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
		category.setCreateDate(new Date(cursor.getLong(cursor.getColumnIndex("createDate"))));
		
		return category;
	}
}
