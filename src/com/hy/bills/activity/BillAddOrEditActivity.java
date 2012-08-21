package com.hy.bills.activity;

import android.os.Bundle;
import android.util.Log;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.Bill;
import com.hy.bills.service.BillService;

public class BillAddOrEditActivity extends BaseActivity {
	private static final String TAG = "BillAddOrEditActivity";
	
	private BillService billService;
	private Bill bill;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.bill_add_edit_activity);
		
		// 隐藏底栏
		hideFooterLayout();
		
		initVariables();
		
		int id = this.getIntent().getIntExtra("billId", -1);
		if (id != -1) {
			bill = billService.find(id);
		}
	}
	
	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		billService = application.getBillService();
	}
}
