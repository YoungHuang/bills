package com.hy.bills.activity;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.activity.AccountBookActivity.AccountBookListAdapter.Holder;
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

	private Bill bill;
	private BillService billService;
	private AccountBookService accountBookService;
	private CategoryService categoryService;
	private UserService userService;

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
			AccountBook accountBook = accountBookService.getDefaultAccountBook();
			accountBookName.setText(accountBook.getName());
			billDate.setText(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
			billType.setText(billTypes[0]);
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
				convertView = LayoutInflater.from(BillAddOrEditActivity.this).inflate(R.layout.account_book_list_item,
						null);
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
