package com.hy.bills.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.adapter.MenuListAdapter;

public class BaseActivity extends Activity {
	protected MainApplication application = (MainApplication) getApplicationContext();
	private RelativeLayout footerLayout;
	protected boolean isClosed = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity);

		footerLayout = (RelativeLayout) this.findViewById(R.id.footerLayout);
		
		ImageView backButton = (ImageView) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected void appendBodyView(int resource) {
		LinearLayout bodyLayout = (LinearLayout) findViewById(R.id.bodyLayout);
		View view = LayoutInflater.from(this).inflate(resource, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		bodyLayout.addView(view, layoutParams);
	}

	// 设置标题
	protected void setTitle(String title) {
		TextView textView = (TextView) findViewById(R.id.title);
		textView.setText(title);
	}
	
	// 隐藏后退按钮
	protected void hideBackBotton() {
		ImageView backButton = (ImageView) this.findViewById(R.id.backButton);
		backButton.setVisibility(View.GONE);
	}

	// 设置菜单键
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			toggleSlideMenu();
			
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	// 创建滑动菜单
	protected void createSlideMenu(int resource, OnItemClickListener listener) {
		ListView menuListView = (ListView) this.findViewById(R.id.menuList);
		String[] items = getResources().getStringArray(resource);
		MenuListAdapter menuListAdapter = new MenuListAdapter(this, items);
		menuListView.setAdapter(menuListAdapter);
		
		menuListView.setOnItemClickListener(listener);

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
		RelativeLayout.LayoutParams layoutParams = null;
		if (isClosed) {
			// Open menu
			layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.titleLayout);
			footerLayout.setLayoutParams(layoutParams);
			isClosed = false;
		} else {
			// Close menu
			layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 68);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			footerLayout.setLayoutParams(layoutParams);
			isClosed = true;
		}
	}

	protected void startActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}
	
	// 显示提醒对话框
	protected void showAlertDialog(String title, String message, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title).setMessage(message);
		builder.setPositiveButton(R.string.yes, listener);
		builder.setNegativeButton(R.string.no, null);
		builder.show();
	}
}
