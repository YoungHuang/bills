package com.hy.bills.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.Category;
import com.hy.bills.service.CategoryService;

public class CategoryActivity extends BaseActivity {
	private static final String TAG = "CategoryActivity";

	private CategoryService categoryService;
	private CategoryExListViewAdapter categoryExListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.category_activity);

		initVariables();

		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuCategoryActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) {
				}
			}
		});

		// 显示类别列表
		ExpandableListView categoryExListView = (ExpandableListView) this.findViewById(R.id.categoryExListView);
		categoryExListViewAdapter = new CategoryExListViewAdapter();
		categoryExListView.setAdapter(categoryExListViewAdapter);
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		categoryService = application.getCategoryService();
	}

	private class CategoryExListViewAdapter extends BaseExpandableListAdapter {
		List<Category> groupCategories;

		public CategoryExListViewAdapter() {
			groupCategories = categoryService.findAllRootCategories();
		}

		@Override
		public int getGroupCount() {
			return groupCategories.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			Category groupCategory = groupCategories.get(groupPosition);
			int count = categoryService.getChildrenCountByParentId(groupCategory.getId());

			return count;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupCategories.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			Category groupCategory = groupCategories.get(groupPosition);
			List<Category> childrenCategories = categoryService.findAllCategoriesByParentId(groupCategory.getId());

			return childrenCategories.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupHolder groupHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(CategoryActivity.this).inflate(null, null);
				groupHolder = new GroupHolder();
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}
			return null;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		class GroupHolder {
			TextView name;
			TextView count;
		}

		class ChildHolder {
			TextView name;
		}
	}
}
