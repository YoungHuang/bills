package com.hy.bills.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.hy.bills.adapter.GridViewAdapter;

public class StartActivity extends BaseActivity implements OnItemClickListener {
	private static final String TAG = "StartActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.start_activity);

		// 隐藏后退按钮
		hideBackBotton();
		
		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuStartActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) {
					showDatabaseBackupDialog();
				}
			}
		});

		GridView gridMenuView = (GridView) this.findViewById(R.id.gridMenu);
		GridViewAdapter gridViewAdapter = new GridViewAdapter(this);
		gridMenuView.setAdapter(gridViewAdapter);
		gridMenuView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:

			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5: // 人员管理
			startActivity(UserActivity.class);
			break;
		default:
			break;
		}
	}
	
	private void showDatabaseBackupDialog() {
		
	}
}
