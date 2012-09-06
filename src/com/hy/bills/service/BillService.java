package com.hy.bills.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hy.bills.domain.Bill;
import com.hy.bills.utils.DateUtils;

public class BillService extends DaoSupport<Bill> {
	private static final String TAG = "BillService";

	public static void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "createTable");
		StringBuilder sql = new StringBuilder();
		sql.append("create table Bill(id integer primary key autoincrement, status integer not null,");
		sql.append(" accountBookId integer not null, accountBookName varchar(20) not null, categoryId integer not null, categoryName varchar(20) not null,");
		sql.append(" billWayId integer, addressId integer, amount decimal not null, billDate datetime not null, billType varchar(20) not null,");
		sql.append(" userIds varchar(255), comment varchar(255), createDate datetime not null)");
		db.execSQL(sql.toString());
	}

	@Override
	public void save(Bill bill) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("insert into Bill(status, accountBookId, accountBookName, categoryId, categoryName, billWayId, addressId, amount, billDate, billType, userIds, comment, createDate)");
		sql.append(" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		db.execSQL(
				sql.toString(),
				new Object[] { bill.getStatus(), bill.getAccountBookId(), bill.getAccountBookName(),
						bill.getCategoryId(), bill.getCategoryName(), bill.getBillWayId(), bill.getAddressId(),
						bill.getAmount(), DateUtils.formatDate(bill.getBillDate(), "yyyy-MM-dd"), bill.getBillType(),
						bill.getUserIds(), bill.getComment(),
						DateUtils.formatDate(bill.getCreateDate(), "yyyy-MM-dd HH:mm:ss") });
	}

	@Override
	public void update(Bill bill) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("update Bill set status=?, accountBookId=?, accountBookName=?, categoryId=?, categoryName=?, billWayId=?, addressId=?, amount=?, billDate=?, billType=?, userIds=?, comment=? where id=?)");
		sql.append(" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		db.execSQL(
				sql.toString(),
				new Object[] { bill.getStatus(), bill.getAccountBookId(), bill.getAccountBookName(),
						bill.getCategoryId(), bill.getCategoryName(), bill.getBillWayId(), bill.getAddressId(),
						bill.getAmount(), DateUtils.formatDate(bill.getBillDate(), "yyyy-MM-dd"), bill.getBillType(),
						bill.getUserIds(), bill.getComment(), bill.getId() });
	}

	@Override
	public void delete(Integer id) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String sql = "delete from Bill where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	@Override
	public Bill find(Integer id) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Bill where id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { id.toString() });
		try {
			Bill bill = null;
			if (cursor.moveToFirst()) {
				bill = parseModel(cursor);
			}
			return bill;
		} finally {
			cursor.close();
		}
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

	public List<Bill> findAllByAccountBookIdOrderByDate(Integer accountBookId) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Bill where accountBookId=? order by billDate";
		Cursor cursor = db.rawQuery(sql, new String[] { accountBookId.toString() });
		try {
			List<Bill> billList = new ArrayList<Bill>();
			while (cursor.moveToNext()) {
				billList.add(parseModel(cursor));
			}

			return billList;
		} finally {
			cursor.close();
		}
	}
	
	public List<Bill> findAllByAccountBookId(Integer accountBookId) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String sql = "select * from Bill";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			List<Bill> billList = new ArrayList<Bill>();
			while (cursor.moveToNext()) {
				billList.add(parseModel(cursor));
			}

			return billList;
		} finally {
			cursor.close();
		}
	}

	private Bill parseModel(Cursor cursor) {
		Bill bill = new Bill();
		bill.setId(cursor.getInt(cursor.getColumnIndex("id")));
		bill.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
		bill.setAccountBookId(cursor.getInt(cursor.getColumnIndex("accountBookId")));
		bill.setAccountBookName(cursor.getString(cursor.getColumnIndex("accountBookName")));
		bill.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
		bill.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryName")));
		bill.setBillWayId(cursor.getInt(cursor.getColumnIndex("billWayId")));
		bill.setAddressId(cursor.getInt(cursor.getColumnIndex("addressId")));
		bill.setAmount(new BigDecimal(cursor.getString(cursor.getColumnIndex("amount"))));
		bill.setBillDate(DateUtils.parseDate(cursor.getString(cursor.getColumnIndex("billDate")), "yyyy-MM-dd"));
		bill.setBillType(cursor.getString(cursor.getColumnIndex("billType")));
		bill.setUserIds(cursor.getString(cursor.getColumnIndex("userIds")));
		bill.setComment(cursor.getString(cursor.getColumnIndex("comment")));
		bill.setCreateDate(DateUtils.parseDate(cursor.getString(cursor.getColumnIndex("createDate")),
				"yyyy-MM-dd HH:mm:ss"));

		return bill;
	}
}
