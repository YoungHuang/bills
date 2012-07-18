package com.hy.bills.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hy.bills.ui.R;

public class MenuListAdapter extends BaseAdapter {
	private Context context;
	private String[] items;

	public MenuListAdapter(Context context, String[] items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.slidemenu_list_item, null);
			holder = new Holder();
			holder.menuName = (TextView) convertView.findViewById(R.id.menuName);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.menuName.setText(items[position]);

		return convertView;
	}

	private class Holder {
		TextView menuName;
	}
}
