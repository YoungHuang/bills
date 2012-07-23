package com.hy.bills.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hy.bills.adapter.GridViewAdapter;
import com.hy.bills.adapter.MenuListAdapter;
import com.hy.bills.db.SQLiteHelper;

public class StartActivity extends Activity implements OnItemClickListener {
	private LinearLayout mainLayout;
	private RelativeLayout footerLayout;
	private boolean isClosed = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);

		initDB();
		
		// 隐藏后退按钮
		ImageView backBotton = (ImageView) this.findViewById(R.id.backButton);
		backBotton.setVisibility(View.GONE);

		GridView gridMenuView = (GridView) this.findViewById(R.id.gridMenu);
		GridViewAdapter gridViewAdapter = new GridViewAdapter(this);
		gridMenuView.setAdapter(gridViewAdapter);
		gridMenuView.setOnItemClickListener(this);

		mainLayout = (LinearLayout) this.findViewById(R.id.mainLayout);
		// 设置菜单键监听器
		mainLayout.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
					toggleSlideMenu();
				}
				
				return true;
			}
		});
		
		footerLayout = (RelativeLayout) this.findViewById(R.id.footer);

		// 创建滑动菜单
		createSlideMenu();
		RelativeLayout bottomBar = (RelativeLayout) this.findViewById(R.id.bottomBar);
		bottomBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSlideMenu();
			}
		});
	}

	private void initDB() {
		SQLiteHelper.setContext(this.getApplicationContext());
	}

	// 创建滑动菜单
	protected void createSlideMenu() {
		ListView menuListView = (ListView) this.findViewById(R.id.menuList);
		String[] items = getResources().getStringArray(R.array.SlideMenuActivityMain);
		MenuListAdapter menuListAdapter = new MenuListAdapter(this, items);
		menuListView.setAdapter(menuListAdapter);
	}

	// 菜单隐藏显示切换
	protected void toggleSlideMenu() {
		if (isClosed) {
			// Open menu
			footerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
			isClosed = false;
		} else {
			// Close menu
			footerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 68));
			isClosed = true;
		}
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
		case 5:

			break;
		default:
			break;
		}
	}
}
