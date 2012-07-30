package com.hy.bills;

import android.app.Application;

import com.hy.bills.db.SQLiteHelper;
import com.hy.bills.service.UserService;

public class MainApplication extends Application {
	private UserService userService;

	@Override
	public void onCreate() {
		super.onCreate();
		initVariables();
		initDB();
	}

	private void initVariables() {
		userService = new UserService();
	}

	private void initDB() {
		SQLiteHelper.setContext(this);
	}
	
	public UserService getUserService() {
		return userService;
	}
}
