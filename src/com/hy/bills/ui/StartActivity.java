package com.hy.bills.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class StartActivity extends Activity {
	private GridViewAdapter gridViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
		
		// 隐藏后退按钮
		ImageView backBotton = (ImageView) this.findViewById(R.id.backButton);
		backBotton.setVisibility(View.GONE);
		
		GridView gridMenu = (GridView) this.findViewById(R.id.gridMenu);
		gridViewAdapter = new GridViewAdapter(this);
		gridMenu.setAdapter(gridViewAdapter);
	}

	class GridViewAdapter extends BaseAdapter {
		private Context context;

		private Integer[] menuIcons = { R.drawable.grid_payout,
				R.drawable.grid_bill, R.drawable.grid_report,
				R.drawable.grid_account_book, R.drawable.grid_category,
				R.drawable.grid_user, };
		
		private String[] menuLabels = new String[6];

		public GridViewAdapter(Context context) {
			this.context = context;
			menuLabels[0] = context.getString(R.string.addBill);
			menuLabels[1] = context.getString(R.string.queryBill);
			menuLabels[2] = context.getString(R.string.statisticsManage);
			menuLabels[3] = context.getString(R.string.bookManage);
			menuLabels[4] = context.getString(R.string.categoryManage);
			menuLabels[5] = context.getString(R.string.userManage);
		}

		@Override
		public int getCount() {
			return menuLabels.length;
		}

		@Override
		public Object getItem(int position) {
			return menuLabels[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.grid_menu_item, null);
				holder = new Holder();
				holder.menuIcon = (ImageView) convertView.findViewById(R.id.menuIcon);
				holder.menuLabel = (TextView) convertView.findViewById(R.id.menuLabel);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			
			holder.menuIcon.setImageResource(menuIcons[position]);
			holder.menuLabel.setText(menuLabels[position]);
			
			return convertView;
		}
		
		private class Holder {
			ImageView menuIcon;
			TextView menuLabel;
		}
	}
}
