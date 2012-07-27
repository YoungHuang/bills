package com.hy.bills.activity;

import com.hy.bills.adapter.MenuListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class BaseActivity extends Activity {
	private LinearLayout mainLayout;
	private RelativeLayout footerLayout;
	protected boolean isClosed = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity);
		
		footerLayout = (RelativeLayout) this.findViewById(R.id.footer);
		
		setMenuKeyListener();
	}

	protected void appendBodyView(int resource) {
		LinearLayout bodyLayout = (LinearLayout) findViewById(R.id.bodyLayout);
		View view = LayoutInflater.from(this).inflate(resource, null);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		bodyLayout.addView(view, layoutParams);
	}

	// 隐藏后退按钮
	protected void hideBackBotton() {
		ImageView backBotton = (ImageView) this.findViewById(R.id.backButton);
		backBotton.setVisibility(View.GONE);
	}
	
	// 设置菜单键监听器
	private void setMenuKeyListener() {
		mainLayout = (LinearLayout) this.findViewById(R.id.mainLayout);
		
		mainLayout.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
					toggleSlideMenu();
				}
				
				return true;
			}
		});
	}
	
	// 创建滑动菜单
	protected void createSlideMenu(int resource) {
		ListView menuListView = (ListView) this.findViewById(R.id.menuList);
		String[] items = getResources().getStringArray(resource);
		MenuListAdapter menuListAdapter = new MenuListAdapter(this, items);
		menuListView.setAdapter(menuListAdapter);
		
		RelativeLayout bottomBar = (RelativeLayout) this.findViewById(R.id.bottomBar);
		bottomBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSlideMenu();
			}
		});
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
	
	protected void startActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}
}
