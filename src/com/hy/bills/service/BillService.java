package com.hy.bills.service;

import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.Bill;
import com.hy.bills.domain.User;

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
	
	public String[] getTotalAmountAndCount(Date billDate, Integer accountBookId) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select ifnull(sum(amount), 0) as totalAmount, count(amount) as count from Bill where billDate=? and accountBookId =?";
		Cursor cursor = db.rawQuery(sql, new String[] { billDate.toString(), accountBookId.toString() });
		try {
			String[] ret = new String[2];
			if (cursor.moveToFirst()) {
				ret[0] = cursor.getString(cursor.getColumnIndex("totalAmount"));
				ret[1] = cursor.getString(cursor.getColumnIndex("count"));
			}
			return ret;
		} finally {
			cursor.close();
		}
	}
	
	public List<Bill> findAllByAccountBookId(Integer accountBookId) {
		// TODO Auto-generated method stub
		return null;
	}
}
