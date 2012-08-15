package com.hy.bills.service;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.Category;

public class CategoryService extends DaoSupport<Category> {
	private static final String TAG = "CategoryService";

	public static void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "createTable");
	}
	
	@Override
	public void save(Category category) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Category category) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Category find(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
}
