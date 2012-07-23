package com.hy.bills.service;

import com.hy.bills.db.SQLiteHelper;

public abstract class DaoSupport<T> implements DAO<T> {
	protected SQLiteHelper sqliteHelper = SQLiteHelper.getInstance();
}
