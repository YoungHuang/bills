package com.hy.bills.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.AccountBook;
import com.hy.bills.service.AccountBookService;
import com.hy.bills.service.BillService;

public class BillActivity extends BaseActivity {
	private static final String TAG = "BillActivity";
	
	private BillService billService;
	private AccountBookService accountBookService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.bill_activity);
		
		initVariables();
		
		AccountBook accountBook = accountBookService.getDefaultAccountBook();
		ListView billListView = (ListView) findViewById(R.id.billListView);
		
		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuBillActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) { // 切换账本
					
				}
			}
		});
	}
	
	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		billService = application.getBillService();
		accountBookService = application.getAccountBookService();
	}
}
