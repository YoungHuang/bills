package com.hy.bills.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.bills.domain.AccountBook;
import com.hy.bills.domain.User;
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
					showAccountBookAddOrEditDialog(null);
				}
			}
		});

		// 显示账本列表
		ListView accountBookListView = (ListView) findViewById(R.id.accoutBookList);
		accountBookListAdapter = new AccountBookListAdapter();
		accountBookListView.setAdapter(accountBookListAdapter);

		// 设置ContextMenu
		registerForContextMenu(accountBookListView);

		// 设置标题
		String title = getString(R.string.account_book_activity_title,
				new Object[] { accountBookListAdapter.getCount() });
		setTitle(title);
	}

	private void initVariables() {
		accountBookService = application.getAccountBookService();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		AccountBook accountBook = (AccountBook) accountBookListAdapter.getItem(adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.account_book_small_icon);
		menu.setHeaderTitle(accountBook.getName());

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_delete_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		AccountBook accountBook = (AccountBook) accountBookListAdapter.getItem(adapterContextMenuInfo.position);
		switch (item.getItemId()) {
		case R.id.edit:
			showAccountBookAddOrEditDialog(accountBook);
			break;
		case R.id.delete:
			deleteAccountBook(accountBook);
			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
	}

	private void showAccountBookAddOrEditDialog(AccountBook accountBook) {

	}

	private void deleteAccountBook(final AccountBook accountBook) {
		String title = getString(R.string.delete_prompt);
		String message = getString(R.string.account_book_delete_confirm, new Object[] { accountBook.getName() });
		showAlertDialog(title, message, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				accountBookService.delete(accountBook.getId());
				Toast.makeText(AccountBookActivity.this,
						getString(R.string.delete_account_book_success, new Object[] { accountBook.getName() }),
						Toast.LENGTH_SHORT).show();

				accountBookListAdapter.dataChanged();
			}
		});
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
				convertView = LayoutInflater.from(AccountBookActivity.this).inflate(R.layout.account_book_list_item,
						null);
				holder = new Holder();
				holder.accountBookIcon = (ImageView) convertView.findViewById(R.id.accountBookIcon);
				holder.accountBookName = (TextView) convertView.findViewById(R.id.accountBookName);
				holder.totalCount = (TextView) convertView.findViewById(R.id.totalCount);
				holder.totalMoney = (TextView) convertView.findViewById(R.id.totalMoney);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			AccountBook accountBook = accountBookList.get(position);
			if (accountBook.isDefault() == AccountBook.YES_DEFAULT) {
				holder.accountBookIcon.setImageResource(R.drawable.account_book_default);
			}
			holder.accountBookName.setText(accountBook.getName());

			return convertView;
		}

		private class Holder {
			ImageView accountBookIcon;
			TextView accountBookName;
			TextView totalCount;
			TextView totalMoney;
		}

		public void dataChanged() {
			accountBookList = accountBookService.findAll();
			this.notifyDataSetChanged();
		}
	}
}
