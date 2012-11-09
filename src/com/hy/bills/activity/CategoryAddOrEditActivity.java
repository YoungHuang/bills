package com.hy.bills.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.Category;
import com.hy.bills.service.CategoryService;
import com.hy.bills.utils.RegexUtils;

public class CategoryAddOrEditActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "CategoryAddOrEditActivity";

	private CategoryService categoryService;
	private Category category;

	private EditText categoryName;
	private Spinner parentCategories;

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

		initView();
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

	private void initView() {
		categoryName = (EditText) findViewById(R.id.categoryName);
		parentCategories = (Spinner) findViewById(R.id.parentCategories);
		Button saveButton = (Button) findViewById(R.id.saveButton);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		saveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		List rootCategories = categoryService.findAllRootCategories();
		rootCategories.add(0, "--请选择--");
		ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, rootCategories);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		parentCategories.setAdapter(arrayAdapter);

		if (category != null) {
			categoryName.setText(category.getName());

			int parentId = category.getParentId();
			if (parentId != 0) {
				int position = 0;
				for (int i = 1; i < arrayAdapter.getCount(); i++) {
					Category pCategory = (Category) arrayAdapter.getItem(i);
					if (pCategory.getId() == parentId) {
						position = arrayAdapter.getPosition(pCategory);
						break;
					}
				}
				parentCategories.setSelection(position);
			} else {
				int count = categoryService.getChildrenCountByParentId(parentId);
				if (count > 0) {
					parentCategories.setEnabled(false);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveButton:
			addOrEditCategory();

			break;
		case R.id.cancelButton:
			finish();
			break;
		}
	}

	private void addOrEditCategory() {
		String newName = categoryName.getText().toString().trim();
		boolean result = RegexUtils.isChineseLetterNum(newName);
		if (!result) {
			Toast.makeText(CategoryAddOrEditActivity.this, getString(R.string.chinese_english_num, newName),
					Toast.LENGTH_LONG).show();
			return;
		}

		try {
			boolean isCreate = false;
			if (category == null) { // 新建
				category = new Category();
				isCreate = true;
			}
			category.setName(newName);
			if (!parentCategories.getSelectedItem().toString().equals("--请选择--")) {
				Category pCategory = (Category) parentCategories.getSelectedItem();
				category.setParentId(pCategory.getId());
			}
			
			if (isCreate) { // 新建
				categoryService.save(category);
				Toast.makeText(CategoryAddOrEditActivity.this, getString(R.string.create_category_success, newName),
						Toast.LENGTH_SHORT).show();
			} else { // 更新
				categoryService.update(category);
				Toast.makeText(CategoryAddOrEditActivity.this, getString(R.string.edit_category_success, newName),
						Toast.LENGTH_SHORT).show();
			}
			
			finish();
		} catch (Exception e) {
			Log.e(TAG, "Create or save category error", e);
			Toast.makeText(CategoryAddOrEditActivity.this, R.string.database_error, Toast.LENGTH_LONG).show();
		}
	}
}
