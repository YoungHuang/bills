package com.hy.bills.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Toast;

import com.hy.bills.MainApplication;
import com.hy.bills.adapter.CategoryExListViewAdapter;
import com.hy.bills.domain.Category;
import com.hy.bills.service.CategoryService;

public class CategoryActivity extends BaseActivity {
	private static final String TAG = "CategoryActivity";

	private CategoryService categoryService;
	private CategoryExListViewAdapter categoryExListViewAdapter;

	private Category category;

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
					Intent intent = new Intent(CategoryActivity.this, CategoryChartActivity.class);
					intent.putExtra("categoryId", category.getId());
					startActivity(intent);
				}
			}
		});

		// 显示类别列表
		ExpandableListView categoryExListView = (ExpandableListView) this.findViewById(R.id.categoryExListView);
		categoryExListView.setGroupIndicator(null);
		categoryExListViewAdapter = new CategoryExListViewAdapter(this, categoryService);
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
			Intent intent = new Intent(this, CategoryAddOrEditActivity.class);
			intent.putExtra("categoryId", category.getId());
			startActivityForResult(intent, 1);
			
			break;
		case R.id.delete:
			deleteCategory();
			break;
		case R.id.statistic:

			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		categoryExListViewAdapter.dataChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void deleteCategory() {
		String title = getString(R.string.delete_prompt);
		String message = getString(R.string.category_delete_confirm, category.getName());
		showAlertDialog(title, message, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				categoryService.deleteChildrenByParentId(category.getId());
				categoryService.delete(category.getId());
				Toast.makeText(CategoryActivity.this, getString(R.string.delete_category_success, category.getName()),
						Toast.LENGTH_SHORT).show();

				categoryExListViewAdapter.dataChanged();
			}
		});
	}
}
