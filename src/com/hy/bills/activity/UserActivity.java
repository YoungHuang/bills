package com.hy.bills.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.User;
import com.hy.bills.service.UserService;

public class UserActivity extends BaseActivity {
	private static final String TAG = "UserActivity";

	private UserService userService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.user_activity);

		initVariables();

		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuStartActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) {
					// showUserAddOrEditDialog();
				}
			}
		});

		ListView userListView = (ListView) findViewById(R.id.userList);
		UserListAdapter userListAdapter = new UserListAdapter();
		userListView.setAdapter(userListAdapter);

		// 设置标题
		String title = getString(R.string.UserActivityTitle, new Object[] {userListAdapter.getCount()});
		setTitle(title);
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		userService = application.getUserService();
	}

	private class UserListAdapter extends BaseAdapter {
		private List<User> userList;

		public UserListAdapter() {
			userList = userService.findAll();
		}

		@Override
		public int getCount() {
			return userList.size();
		}

		@Override
		public Object getItem(int position) {
			return userList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(UserActivity.this).inflate(null, null);
				holder = new Holder();
				holder.userName = (TextView) convertView.findViewById(R.id.userName);
				convertView.setTag(convertView);
			} else {
				holder = (Holder) convertView.getTag();
			}

			User user = userList.get(position);
			holder.userName.setText(user.getName());

			return convertView;
		}

		private class Holder {
			TextView userName;
		}
	}
}
