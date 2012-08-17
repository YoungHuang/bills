package com.hy.bills.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
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
				if (position == 0) { // 新建类别
					Intent intent = new Intent(CategoryActivity.this, CategoryAddOrEditActivity.class);
					startActivityForResult(intent, 0);
				} else if (position == 1) { // 统计类别
					
				}
			}
		});

		// 显示类别列表
		ExpandableListView categoryExListView = (ExpandableListView) this.findViewById(R.id.categoryExListView);
		categoryExListViewAdapter = new CategoryExListViewAdapter();
		categoryExListView.setAdapter(categoryExListViewAdapter);

		// 设置ContextMenu
		registerForContextMenu(categoryExListView);

		// 设置标题
		String title = getString(R.string.category_activity_title, categoryService.getCount());
		setTitle(title);
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		categoryService = application.getCategoryService();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo expandableListContextMenuInfo = (ExpandableListContextMenuInfo) menuInfo;
		long packedPosition = expandableListContextMenuInfo.packedPosition;
		int type = ExpandableListView.getPackedPositionType(packedPosition);
		int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
		Category category = null;
		switch (type) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			category = (Category) categoryExListViewAdapter.getGroup(groupPosition);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
			category = (Category) categoryExListViewAdapter.getChild(groupPosition, childPosition);
			break;
		}

		menu.setHeaderIcon(R.drawable.category_small_icon);
		menu.setHeaderTitle(category.getName());

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit:
			
			break;
		case R.id.delete:
			
			break;
		case R.id.statistic:

			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
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
				convertView = LayoutInflater.from(CategoryActivity.this).inflate(R.layout.category_group_list_item,
						null);
				groupHolder = new GroupHolder();
				groupHolder.name = (TextView) convertView.findViewById(R.id.categoryName);
				groupHolder.count = (TextView) convertView.findViewById(R.id.count);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			Category category = groupCategories.get(groupPosition);
			groupHolder.name.setText(category.getName());
			int count = categoryService.getChildrenCountByParentId(category.getId());
			groupHolder.count.setText(getString(R.string.children_category_count, count));

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ChildHolder childHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(CategoryActivity.this).inflate(R.layout.category_child_list_item,
						null);
				childHolder = new ChildHolder();
				childHolder.name = (TextView) convertView.findViewById(R.id.categoryName);
				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}

			Category groupCategory = groupCategories.get(groupPosition);
			List<Category> childrenCategories = categoryService.findAllCategoriesByParentId(groupCategory.getId());
			Category childCategory = childrenCategories.get(childPosition);
			childHolder.name.setText(childCategory.getName());

			return convertView;
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
