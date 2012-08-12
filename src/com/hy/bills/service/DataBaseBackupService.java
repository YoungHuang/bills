package com.hy.bills.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hy.bills.activity.R;
import com.hy.bills.db.SQLiteHelper;

public class DataBaseBackupService {
	private static final String TAG = "DataBaseBackupService";

	private final static String PREFERENCES_NAME = "DBBackupDate";
	private final static String BACKUP_DATE = "BackupDate";

	private Context context;
	private File backupPath;
	private File dbPath;

	public DataBaseBackupService(Context context) {
		this.context = context;
	}

	// 数据备份
	public boolean databaseBackup() {
		if (checkSDCard()) {
			try {
				copy(dbPath, backupPath);
				saveBackupDate(System.currentTimeMillis());
			} catch (IOException e) {
				Log.e(TAG, "databaseBackup", e);
				return false;
			}

			return true;
		} else {
			Toast.makeText(context, R.string.sdcard_error, Toast.LENGTH_SHORT);
			return false;
		}
	}

	// 数据还原
	public boolean databaseRestore() {
		long time = getBackupDate();
		if (time != 0 && checkSDCard()) {
			try {
				copy(backupPath, dbPath);
				return true;
			} catch (IOException e) {
				Log.e(TAG, "databaseRestore", e);
				return false;
			}
		}
		return false;
	}

	// 检查SDCard，创建备份文件
	private boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (backupPath != null && dbPath != null)
				return true;

			String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			File backDir = new File(sdCardDir + "/" + context.getPackageName() + "/");
			if (!backDir.exists()) {
				backDir.mkdirs();
			}
			backupPath = new File(sdCardDir + "/" + context.getPackageName() + "/" + SQLiteHelper.DATABASE_NAME);

			dbPath = context.getDatabasePath(SQLiteHelper.DATABASE_NAME);

			return true;
		} else {
			return false;
		}
	}

	// 复制文件
	private void copy(File from, File to) throws IOException {
		FileInputStream input = new FileInputStream(from);
		FileOutputStream output = new FileOutputStream(to);
		byte[] buffer = new byte[1024];
		int count;
		while ((count = input.read(buffer)) != -1) {
			output.write(buffer, 0, count);
		}
		input.close();
		output.close();
	}

	// 保存备份日期
	private void saveBackupDate(long time) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putLong(BACKUP_DATE, time).commit();
	}

	// 恢复备份日期
	private long getBackupDate() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

		return sharedPreferences.getLong(BACKUP_DATE, 0);
	}
}
