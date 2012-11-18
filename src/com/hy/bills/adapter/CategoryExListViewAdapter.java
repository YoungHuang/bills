package com.hy.bills.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.bills.activity.R;
import com.hy.bills.domain.Category;
import com.hy.bills.service.CategoryService;

public class CategoryExListViewAdapter extends BaseExpandableListAdapter {
	private Context context;
	private CategoryService categoryService;
	List<Category> groupCategories;

	public CategoryExListViewAdapter(Context context, CategoryService categoryService) {
		this.context = context;
		this.categoryService = categoryService;
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
		List<Category> childrenCategories = categoryService.findAllChildrenByParentId(groupCategory.getId());

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
			convertView = LayoutInflater.from(context).inflate(R.layout.category_group_list_item, null);
			groupHolder = new GroupHolder();
			groupHolder.name = (TextView) convertView.findViewById(R.id.categoryName);
			groupHolder.count = (TextView) convertView.findViewById(R.id.count);
			groupHolder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		Category category = groupCategories.get(groupPosition);
		groupHolder.name.setText(category.getName());
		int count = categoryService.getChildrenCountByParentId(category.getId());
		groupHolder.count.setText(context.getString(R.string.children_category_count, count));

		if (isExpanded) {
			groupHolder.arrow.setImageResource(R.drawable.up_arr);
		} else {
			groupHolder.arrow.setImageResource(R.drawable.down_arr);
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ChildHolder childHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.category_child_list_item, null);
			childHolder = new ChildHolder();
			childHolder.name = (TextView) convertView.findViewById(R.id.categoryName);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		Category groupCategory = groupCategories.get(groupPosition);
		List<Category> childrenCategories = categoryService.findAllChildrenByParentId(groupCategory.getId());
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

	public void dataChanged() {
		groupCategories = categoryService.findAllRootCategories();
		this.notifyDataSetChanged();
	}

	class GroupHolder {
		TextView name;
		TextView count;
		ImageView arrow;
	}

	class ChildHolder {
		TextView name;
	}
}
