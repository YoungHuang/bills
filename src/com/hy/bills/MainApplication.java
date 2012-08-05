package com.hy.bills;

import android.app.Application;

import com.hy.bills.db.SQLiteHelper;
import com.hy.bills.service.DataBaseBackupService;
import com.hy.bills.service.UserService;

public class MainApplication extends Application {
	private UserService userService;
	private DataBaseBackupService dataBaseBackupService;

	@Override
	public void onCreate() {
		super.onCreate();
		// Must initialize database before initialize services which use database
		initDB();
		initVariables();
	}

	private void initVariables() {
		userService = new UserService();
		dataBaseBackupService = new DataBaseBackupService(this);
	}

	private void initDB() {
		SQLiteHelper.setContext(this);
	}
	
	public UserService getUserService() {
		return userService;
	}
	
	public DataBaseBackupService getDataBaseBackupService() {
		return dataBaseBackupService;
	}
}
