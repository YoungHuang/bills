package com.hy.bills.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.bills.MainApplication;
import com.hy.bills.adapter.AccountBookSelectAdapter;
import com.hy.bills.domain.AccountBook;
import com.hy.bills.domain.Bill;
import com.hy.bills.service.AccountBookService;
import com.hy.bills.service.BillService;
import com.hy.bills.service.UserService;
import com.hy.bills.utils.DateUtils;

public class BillActivity extends BaseActivity {
	private static final String TAG = "BillActivity";

	private BillService billService;
	private AccountBookService accountBookService;
	private UserService userService;
	private BillListAdapter billListAdapter;

	private AccountBook accountBook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.bill_activity);

		initVariables();

		accountBook = accountBookService.getDefaultAccountBook();
		ListView billListView = (ListView) findViewById(R.id.billListView);
		billListAdapter = new BillListAdapter();
		billListView.setAdapter(billListAdapter);

		// 设置ContextMenu
		registerForContextMenu(billListView);

		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuBillActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) { // 切换账本
					showAccountBookSelectDialog();
				}
			}
		});

		// 设置标题
		setTitle();
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		billService = application.getBillService();
		accountBookService = application.getAccountBookService();
		userService = application.getUserService();
	}

	private void setTitle() {
		String title = getString(R.string.bill_activity_title, accountBook.getName(), billListAdapter.getCount());
		setTitle(title);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		Bill bill = (Bill) billListAdapter.getItem(adapterContextMenuInfo.position);
		menu.setHeaderIcon(R.drawable.payout_small_icon);
		menu.setHeaderTitle(bill.getCategoryName());

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_delete_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		Bill bill = (Bill) billListAdapter.getItem(adapterContextMenuInfo.position);
		switch (item.getItemId()) {
		case R.id.edit:
			Intent intent = new Intent(this, BillAddOrEditActivity.class);
			intent.putExtra("billId", bill.getId());
			startActivityForResult(intent, 1);

			break;
		case R.id.delete:
			deleteBill(bill);
			break;
		default:
			return super.onContextItemSelected(item);
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		billListAdapter.dataChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showAccountBookSelectDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.account_book_select_dialog, null);
		ListView accoutBookList = (ListView) view.findViewById(R.id.accoutBookList);
		final AccountBookSelectAdapter adapter = new AccountBookSelectAdapter(this, accountBookService);
		accoutBookList.setAdapter(adapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_account_book).setIcon(R.drawable.account_book_big_icon).setView(view);
		builder.setNegativeButton(R.string.cancel, null);
		final AlertDialog dialog = builder.create();

		accoutBookList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				accountBook = (AccountBook) adapter.getItem(position);
				billListAdapter.dataChanged();

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void deleteBill(final Bill bill) {
		String title = getString(R.string.delete_prompt);
		String message = getString(R.string.bill_delete_confirm, bill.getCategoryName());
		showAlertDialog(title, message, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				billService.delete(bill.getId());
				Toast.makeText(BillActivity.this,
						getString(R.string.delete_bill_success, bill.getCategoryName()),
						Toast.LENGTH_SHORT).show();

				billListAdapter.dataChanged();
			}
		});
	}

	private class BillListAdapter extends BaseAdapter {
		private List<Bill> billList;

		public BillListAdapter() {
			billList = billService.findAllByAccountBookIdOrderByDate(accountBook.getId());
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
				holder.billDate = (TextView) convertView.findViewById(R.id.billDate);
				holder.totalAmountAndCount = (TextView) convertView.findViewById(R.id.totalAmountAndCount);
				holder.billName = (TextView) convertView.findViewById(R.id.billName);
				holder.amount = (TextView) convertView.findViewById(R.id.amount);
				holder.billUsersAndType = (TextView) convertView.findViewById(R.id.billUsersAndType);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			Bill bill = billList.get(position);
			boolean showDateNav = false;

			if (position > 0) {
				Bill preBill = billList.get(position - 1);
				String date = DateUtils.formatDate(bill.getBillDate(), "yyyy-MM-dd");
				String preDate = DateUtils.formatDate(preBill.getBillDate(), "yyyy-MM-dd");
				if (!date.equals(preDate)) {
					showDateNav = true;
				}
			}

			// 日期分割栏
			if (position == 0 || showDateNav) {
				convertView.findViewById(R.id.dateNav).setVisibility(View.VISIBLE);
				holder.billDate.setText(DateUtils.formatDate(bill.getBillDate(), "yyyy-MM-dd"));
				String[] arr = billService.getTotalAmountAndCount(bill.getBillDate(), bill.getAccountBookId());
				holder.totalAmountAndCount.setText(getString(R.string.total_amount_and_count, arr[0], arr[1]));
			}

			holder.billName.setText(bill.getCategoryName());
			holder.amount.setText(bill.getAmount().toString());
			String userNames = userService.getUserNamesStringByIds(bill.getUserIds());
			holder.billUsersAndType.setText(userNames + " " + bill.getBillType());

			return convertView;
		}

		private class Holder {
			TextView billDate;
			TextView totalAmountAndCount;
			TextView billName;
			TextView amount;
			TextView billUsersAndType;
		}

		public void dataChanged() {
			billList = billService.findAllByAccountBookIdOrderByDate(accountBook.getId());
			this.notifyDataSetChanged();
			
			setTitle();
		}
	}
}
