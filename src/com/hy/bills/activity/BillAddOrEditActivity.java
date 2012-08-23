package com.hy.bills.activity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.domain.AccountBook;
import com.hy.bills.domain.Bill;
import com.hy.bills.domain.Category;
import com.hy.bills.domain.User;
import com.hy.bills.service.AccountBookService;
import com.hy.bills.service.BillService;
import com.hy.bills.service.CategoryService;
import com.hy.bills.service.UserService;
import com.hy.bills.utils.DateUtils;

public class BillAddOrEditActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "BillAddOrEditActivity";

	private BillService billService;
	private AccountBookService accountBookService;
	private CategoryService categoryService;
	private UserService userService;

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

		// 设置标题
		setTitle();

		initView();
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		billService = application.getBillService();
		accountBookService = application.getAccountBookService();
		categoryService = application.getCategoryService();
		userService = application.getUserService();
	}

	private void setTitle() {
		// 设置标题
		String title;
		if (bill == null) {
			title = getString(R.string.bill_add_edit_title, getString(R.string.create));
		} else {
			title = getString(R.string.bill_add_edit_title, getString(R.string.edit));
		}
		setTitle(title);
	}

	private void initView() {
		EditText accountBookName = (EditText) findViewById(R.id.accountBookName);
		Button selectAccountbook = (Button) findViewById(R.id.selectAccountbook);
		EditText amount = (EditText) findViewById(R.id.amount);
		Button inputAmount = (Button) findViewById(R.id.inputAmount);
		AutoCompleteTextView categoryName = (AutoCompleteTextView) findViewById(R.id.categoryName);
		Button selectCategory = (Button) findViewById(R.id.selectCategory);
		EditText billDate = (EditText) findViewById(R.id.billDate);
		Button selectBillDate = (Button) findViewById(R.id.selectBillDate);
		EditText billType = (EditText) findViewById(R.id.billType);
		Button selectBillType = (Button) findViewById(R.id.selectBillType);
		EditText userList = (EditText) findViewById(R.id.userList);
		Button selectUsers = (Button) findViewById(R.id.selectUsers);
		EditText comment = (EditText) findViewById(R.id.comment);
		Button saveButton = (Button) findViewById(R.id.saveButton);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);

		selectAccountbook.setOnClickListener(this);
		inputAmount.setOnClickListener(this);
		selectCategory.setOnClickListener(this);
		selectBillDate.setOnClickListener(this);
		selectBillType.setOnClickListener(this);
		selectUsers.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		List<Category> categoryList = categoryService.findAll();
		String[] categoryNameList = new String[categoryList.size()];
		for (int i = 0; i < categoryList.size(); i++) {
			categoryNameList[i] = categoryList.get(i).getName();
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
				categoryNameList);
		categoryName.setAdapter(arrayAdapter);

		String[] billTypes = getResources().getStringArray(R.array.BillType);

		if (bill == null) {
			bill = new Bill();
			AccountBook accountBook = accountBookService.getDefaultAccountBook();
			accountBookName.setText(accountBook.getName());
			bill.setAccountBookId(accountBook.getId());
			bill.setAccountBookName(accountBook.getName());
			Date createDate = new Date();
			billDate.setText(DateUtils.formatDate(createDate, "yyyy-MM-dd"));
			bill.setBillDate(createDate);
			billType.setText(billTypes[0]);
			bill.setBillType(billTypes[0]);
		} else {
			accountBookName.setText(accountBookService.find(bill.getAccountBookId()).getName());
			amount.setText(bill.getAmount().toString());
			billDate.setText(DateUtils.formatDate(bill.getBillDate(), "yyyy-MM-dd"));
			billType.setText(bill.getBillType());
			userList.setText(getUserNamesStringByIds(bill.getUserIds()));
			comment.setText(bill.getComment());
		}
	}

	private String getUserNamesStringByIds(String userIds) {
		List<User> userList = userService.findAllByIds(userIds);
		StringBuilder str = new StringBuilder();
		for (User user : userList) {
			str.append(user.getName()).append(",");
		}

		String ret = "";
		if (str.length() > 0) {
			ret = str.substring(0, str.length() - 1);
		}

		return ret;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectAccountbook:
			showSelectAccountBookDialog();
			break;
		case R.id.inputAmount:
			showNumberDialog();
			break;
		case R.id.selectCategory:

			break;
		case R.id.selectBillDate:

			break;
		case R.id.selectBillType:

			break;
		case R.id.selectUsers:

			break;
		case R.id.saveButton:

			break;
		case R.id.cancelButton:

			break;
		}
	}

	private void showSelectAccountBookDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.account_book_select_dialog, null);
		ListView accoutBookList = (ListView) view.findViewById(R.id.accoutBookList);
		final AccountBookSelectAdapter adapter = new AccountBookSelectAdapter();
		accoutBookList.setAdapter(adapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_account_book).setIcon(R.drawable.account_book_big_icon).setView(view);
		builder.setNegativeButton(R.string.cancel, null);
		final AlertDialog dialog = builder.create();

		accoutBookList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AccountBook accountBook = (AccountBook) adapter.getItem(position);
				((EditText) view).setText(accountBook.getName());
				bill.setAccountBookId(accountBook.getId());
				bill.setAccountBookName(accountBook.getName());

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void showNumberDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.number_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		
		final EditText inputText = (EditText) findViewById(R.id.inputText);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = inputText.getText().toString();
				switch (v.getId()) {
				case R.id.btnDot:
					if (number.indexOf(".") == -1) {
						number += ".";
					}
					break;
				case R.id.btnOne:
					number += "1";
					break;
				case R.id.btnTwo:
					number += "2";
					break;
				case R.id.btnThree:
					number += "3";
					break;
				case R.id.btnFour:
					number += "4";
					break;
				case R.id.btnFive:
					number += "5";
					break;
				case R.id.btnSix:
					number += "6";
					break;
				case R.id.btnSeven:
					number += "7";
					break;
				case R.id.btnEight:
					number += "8";
					break;
				case R.id.btnNine:
					number += "9";
					break;
				case R.id.btnZero:
					number += "0";
					break;
				case R.id.btnReset:
					number = "";
					break;
				case R.id.btnOk:
					BigDecimal amount;
					if (!number.equals(".") && number.length() != 0) {
						amount = new BigDecimal(number);
					} else {
						amount = new BigDecimal(0);
					}
					bill.setAmount(amount);
					dialog.dismiss();
					break;
				default:
					break;
				}
				
				inputText.setText(number);
			}
		};
		view.findViewById(R.id.btnDot).setOnClickListener(listener);
		view.findViewById(R.id.btnZero).setOnClickListener(listener);
		view.findViewById(R.id.btnOne).setOnClickListener(listener);
		view.findViewById(R.id.btnTwo).setOnClickListener(listener);
		view.findViewById(R.id.btnThree).setOnClickListener(listener);
		view.findViewById(R.id.btnFour).setOnClickListener(listener);
		view.findViewById(R.id.btnFive).setOnClickListener(listener);
		view.findViewById(R.id.btnSix).setOnClickListener(listener);
		view.findViewById(R.id.btnSeven).setOnClickListener(listener);
		view.findViewById(R.id.btnEight).setOnClickListener(listener);
		view.findViewById(R.id.btnNine).setOnClickListener(listener);
		view.findViewById(R.id.btnReset).setOnClickListener(listener);
		view.findViewById(R.id.btnOk).setOnClickListener(listener);
		
		dialog.show();
	}

	private class AccountBookSelectAdapter extends BaseAdapter {
		private List<AccountBook> accountBookList;

		public AccountBookSelectAdapter() {
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
				convertView = LayoutInflater.from(BillAddOrEditActivity.this).inflate(
						R.layout.account_book_select_list_item, null);
				holder = new Holder();
				holder.accountBookIcon = (ImageView) convertView.findViewById(R.id.accountBookIcon);
				holder.accountBookName = (TextView) convertView.findViewById(R.id.accountBookName);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			AccountBook accountBook = accountBookList.get(position);
			if (accountBook.isDefault() == AccountBook.YES_DEFAULT) {
				holder.accountBookIcon.setImageResource(R.drawable.account_book_default);
			} else {
				holder.accountBookIcon.setImageResource(R.drawable.account_book_big_icon);
			}
			holder.accountBookName.setText(accountBook.getName());

			return convertView;
		}

		private class Holder {
			ImageView accountBookIcon;
			TextView accountBookName;
		}
	}
}
