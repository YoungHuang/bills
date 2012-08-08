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

import com.hy.bills.domain.AccountBook;
import com.hy.bills.service.AccountBookService;

public class AccountBookActivity extends BaseActivity {
	private static final String TAG = "AccountBookActivity";

	private AccountBookService accountBookService;
	private AccountBookListAdapter accountBookListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.account_book_activity);

		initVariables();

		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuAccountBookActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) {
					// showAccountBookAddOrEditDialog(null);
				}
			}
		});

		// 显示账本列表
		ListView accoutBookListView = (ListView) findViewById(R.id.accoutBookList);
		accountBookListAdapter = new AccountBookListAdapter();
		accoutBookListView.setAdapter(accountBookListAdapter);

		// 设置ContextMenu
		registerForContextMenu(accoutBookListView);

		// 设置标题
		String title = getString(R.string.account_book_activity_title,
				new Object[] { accountBookListAdapter.getCount() });
		setTitle(title);
	}

	private void initVariables() {
		accountBookService = application.getAccountBookService();
	}

	private class AccountBookListAdapter extends BaseAdapter {
		private List<AccountBook> accountBookList;

		public AccountBookListAdapter() {
			accountBookList = accountBookService.findAll();
		}

		@Override
		public int getCount() {
			return accountBookList.size();
		}

		@Override
		public Object getItem(int position) {
			return accountBookList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(AccountBookActivity.this).inflate(null, null);
				holder = new Holder();
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			AccountBook accountBook = accountBookList.get(position);

			return convertView;
		}

		private class Holder {
			TextView name;
		}

		public void dataChanged() {
			accountBookList = accountBookService.findAll();
			this.notifyDataSetChanged();
		}
	}
}
