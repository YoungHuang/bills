package com.hy.bills;

import android.app.Application;

import com.hy.bills.db.SQLiteHelper;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		initDB();
	}

	private void initDB() {
		SQLiteHelper.setContext(this);
	}
	
}
