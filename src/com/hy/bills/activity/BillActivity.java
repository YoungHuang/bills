package com.hy.bills.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.AccountBook;
import com.hy.bills.domain.Bill;
import com.hy.bills.service.AccountBookService;
import com.hy.bills.service.BillService;

public class BillActivity extends BaseActivity {
	private static final String TAG = "BillActivity";
	
	private BillService billService;
	private AccountBookService accountBookService;
	private BillListAdapter billListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.bill_activity);
		
		initVariables();
		
		AccountBook accountBook = accountBookService.getDefaultAccountBook();
		ListView billListView = (ListView) findViewById(R.id.billListView);
		billListAdapter = new BillListAdapter();
		billListView.setAdapter(billListAdapter);
		
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
	
	private class BillListAdapter extends BaseAdapter {
		private List<Bill> billList;

		public BillListAdapter() {
			billList = billService.findAll();
		}

		@Override
		public int getCount() {
			return billList.size();
		}

		@Override
		public Object getItem(int position) {
			return billList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(BillActivity.this).inflate(R.layout.bill_list_item, null);
				holder = new Holder();
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			Bill bill = billList.get(position);

			return convertView;
		}

		private class Holder {
			TextView billDate;
			TextView totalAmount;
			TextView billName;
			TextView amount;
			TextView billUsersAndType;
		}

		public void dataChanged() {
			billList = billService.findAll();
			this.notifyDataSetChanged();
		}
	}
}
