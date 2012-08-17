package com.hy.bills.activity;

import android.os.Bundle;
import android.util.Log;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.Category;
import com.hy.bills.service.CategoryService;

public class CategoryAddOrEditActivity extends BaseActivity {
	private static final String TAG = "CategoryAddOrEditActivity";
	
	private CategoryService categoryService;
	private Category category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.category_add_edit_activity);
		
		// 隐藏底栏
		hideFooterLayout();
		
		initVariables();
		
		
		int id = this.getIntent().getIntExtra("categoryId", -1);
		if (id != -1) {
			category = categoryService.find(id);
		}
		
		// 设置标题
		setTitle();
	}
	
	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		categoryService = application.getCategoryService();
	}
	
	private void setTitle() {
		// 设置标题
		String title;
		if (category == null) {
			title = getString(R.string.category_add_edit_title, getString(R.string.create));
		} else {
			title = getString(R.string.category_add_edit_title, getString(R.string.edit));
		}
		setTitle(title);
	}
}
