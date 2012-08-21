package com.hy.bills.service;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.Bill;

public class BillService extends DaoSupport<Bill> {
	private static final String TAG = "BillService";

	public static void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "createTable");
	}
	
	@Override
	public void save(Bill obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Bill obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bill find(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> getScrollData(Integer offset, Integer maxResult) {
		// TODO Auto-generated method stub
		return null;
	}
}
