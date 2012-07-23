package com.hy.bills.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "bills.db";
	private final static int DATABASE_VERSION = 1;
	
	private static SQLiteHelper instance;
	private static Context context;

	private SQLiteHelper() {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static SQLiteHelper getInstance() {
		if (instance != null)
			return instance;
		
		synchronized(SQLiteHelper.class) {
			instance = new SQLiteHelper();
			return instance;
		}
	}
	
	public static void setContext(Context context) {
		SQLiteHelper.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
