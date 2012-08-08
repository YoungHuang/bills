package com.hy.bills.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.hy.bills.MainApplication;
import com.hy.bills.adapter.GridViewAdapter;
import com.hy.bills.service.DataBaseBackupService;

public class StartActivity extends BaseActivity implements OnItemClickListener {
	private static final String TAG = "StartActivity";

	private DataBaseBackupService dataBaseBackupService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.start_activity);

		initVariables();

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

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		dataBaseBackupService = application.getDataBaseBackupService();
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
		case 3: // 账本管理
			startActivity(AccountBookActivity.class);
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

	// 数据备份
	private void showDatabaseBackupDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.database_backup_dialog, null);
		Button dbbackup = (Button) view.findViewById(R.id.dbbackup);
		Button dbrestore = (Button) view.findViewById(R.id.dbrestore);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.db_backup_title).setView(view).setIcon(R.drawable.database_backup)
				.setNegativeButton(R.string.cancel, null);
		final AlertDialog dialog = builder.show();
		
		// 数据备份
		dbbackup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dataBaseBackupService.databaseBackup()) {
					Toast.makeText(StartActivity.this, R.string.db_backup_success, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(StartActivity.this, R.string.db_backup_failed, Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
		// 数据还原
		dbrestore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dataBaseBackupService.databaseRestore()) {
					Toast.makeText(StartActivity.this, R.string.db_restore_success, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(StartActivity.this, R.string.db_restore_failed, Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
	}
}
