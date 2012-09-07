package com.hy.bills.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.Category;

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
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Category";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			List<Category> categoryList = new ArrayList<Category>();
			while (cursor.moveToNext()) {
				categoryList.add(parseModel(cursor));
			}

			return categoryList;
		} finally {
			cursor.close();
		}
	}

	@Override
	public List<Category> getScrollData(Integer offset, Integer maxResult) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Category limit ?,?";
		Cursor cursor = db.rawQuery(sql, new String[] { offset.toString(), maxResult.toString() });
		try {
			List<Category> categoryList = new ArrayList<Category>();
			while (cursor.moveToNext()) {
				categoryList.add(parseModel(cursor));
			}

			return categoryList;
		} finally {
			cursor.close();
		}
	}

	public void deleteChildrenByParentId(Integer parentId) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "delete from Category where parentId=?";
		db.execSQL(sql, new Object[] { parentId });
	}

	public int getCount() {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select count(*) from Category";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			int count = 0;
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			}
			return count;
		} finally {
			cursor.close();
		}
	}

	public List<Category> findAllRootCategories() {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Category where parentId=0";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			List<Category> categoryList = new ArrayList<Category>();
			while (cursor.moveToNext()) {
				categoryList.add(parseModel(cursor));
			}

			return categoryList;
		} finally {
			cursor.close();
		}
	}

	public List<Category> findAllChildrenByParentId(Integer parentId) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Category where parentId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { parentId.toString() });
		try {
			List<Category> categoryList = new ArrayList<Category>();
			while (cursor.moveToNext()) {
				categoryList.add(parseModel(cursor));
			}

			return categoryList;
		} finally {
			cursor.close();
		}
	}

	public int getChildrenCountByParentId(Integer parentId) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select count(*) from Category where parentId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { parentId.toString() });
		try {
			int count = 0;
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			}
			return count;
		} finally {
			cursor.close();
		}
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
	
	public List<CategoryStatistics> getCategoryStatistics(Integer id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select c.categoryName as categoryName, count(b.id) as count, sum(b.amount) as totalAmount  from Bill as b, Category as c where b.id=c.id and c.parentId=? group by c.id";
		Cursor cursor = db.rawQuery(sql, new String[] { id.toString() });
		try {
			List<CategoryStatistics> categoryStatsList = new ArrayList<CategoryStatistics>();
			while (cursor.moveToNext()) {
				CategoryStatistics stats = new CategoryStatistics();
				stats.categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
				stats.count = cursor.getInt(cursor.getColumnIndex("count"));
				stats.totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
				categoryStatsList.add(stats);
			}

			return categoryStatsList;
		} finally {
			cursor.close();
		}
	}
	
	public class CategoryStatistics {
		public String categoryName;
		public int count;
		public double totalAmount;
	}
}
