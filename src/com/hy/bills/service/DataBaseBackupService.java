package com.hy.bills.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hy.bills.activity.R;
import com.hy.bills.db.SQLiteHelper;

public class DataBaseBackupService {
	private static final String TAG = "DataBaseBackupService";
	
	private Context context;
	private File backupPath;
	
	public DataBaseBackupService(Context context) {
		this.context = context;
	}
	
	// 备份数据库
	public boolean databaseBackup() {
		if (checkSDCard()) {
			File dbPath = context.getDatabasePath(SQLiteHelper.DATABASE_NAME);
			try {
				copy(dbPath, backupPath);
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
	
	// 恢复数据库
	public void databaseRestore() {
		
	}
	
	// 检查SDCard，创建备份文件
	private boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (backupPath != null)
				return true;
			
			String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			backupPath = new File(sdCardDir + context.getPackageName() + SQLiteHelper.DATABASE_NAME);
			
			return true;
		} else {
			return false;
		}
	}
	
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
}
