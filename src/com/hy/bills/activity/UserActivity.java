package com.hy.bills.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.User;
import com.hy.bills.service.UserService;

public class UserActivity extends BaseActivity {
	private static final String TAG = "UserActivity";

	private UserService userService;
	private UserListAdapter userListAdapter;

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
					showUserAddOrEditDialog(null);
				}
			}
		});

		// 显示用户列表
		ListView userListView = (ListView) findViewById(R.id.userList);
		userListAdapter = new UserListAdapter();
		userListView.setAdapter(userListAdapter);

		// 设置ContextMenu
		registerForContextMenu(userListView);

		// 设置标题
		String title = getString(R.string.user_activity_title, new Object[] { userListAdapter.getCount() });
		setTitle(title);
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		userService = application.getUserService();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		User user = (User) userListAdapter.getItem(adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.user_small_icon);
		menu.setHeaderTitle(user.getName());

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_delete_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		User user = (User) userListAdapter.getItem(adapterContextMenuInfo.position);
		switch (item.getItemId()) {
		case R.id.edit:
			editUser(user);
			break;
		case R.id.delete:
			deleteUser(user);
			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
	}

	private void showUserAddOrEditDialog(User user) {
		View view = LayoutInflater.from(this).inflate(R.layout.user_add_edit_dialog, null);
		EditText editText = (EditText) view.findViewById(R.id.userName);
		String title = "";
		if (user != null) {
			editText.setText(user.getName());
			title = getString(R.string.user_dialog_title, new Object[] { getString(R.string.edit) });
		} else {
			title = getString(R.string.user_dialog_title, new Object[] { getString(R.string.create) });
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title).setIcon(R.drawable.user_big_icon).setView(view);
		builder.setNeutralButton(R.string.save, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void editUser(User user) {
		// TODO Auto-generated method stub

	}

	private void deleteUser(User user) {
		// TODO Auto-generated method stub

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
