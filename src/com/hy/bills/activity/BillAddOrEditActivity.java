package com.hy.bills.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hy.bills.MainApplication;
import com.hy.bills.adapter.AccountBookSelectAdapter;
import com.hy.bills.adapter.CategoryExListViewAdapter;
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

	EditText accountBookName;
	EditText amount;
	AutoCompleteTextView categoryName;
	EditText billDate;
	EditText billType;
	EditText userListView;
	EditText comment;

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
		accountBookName = (EditText) findViewById(R.id.accountBookName);
		Button selectAccountbook = (Button) findViewById(R.id.selectAccountbook);
		amount = (EditText) findViewById(R.id.amount);
		Button inputAmount = (Button) findViewById(R.id.inputAmount);
		categoryName = (AutoCompleteTextView) findViewById(R.id.categoryName);
		Button selectCategory = (Button) findViewById(R.id.selectCategory);
		billDate = (EditText) findViewById(R.id.billDate);
		Button selectBillDate = (Button) findViewById(R.id.selectBillDate);
		billType = (EditText) findViewById(R.id.billType);
		Button selectBillType = (Button) findViewById(R.id.selectBillType);
		userListView = (EditText) findViewById(R.id.userList);
		Button selectUsers = (Button) findViewById(R.id.selectUsers);
		comment = (EditText) findViewById(R.id.comment);
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
			userListView.setText(userService.getUserNamesStringByIds(bill.getUserIds()));
			comment.setText(bill.getComment());
		}
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
			showSelectCategoryDialog();
			break;
		case R.id.selectBillDate:
			showSelectBillDate();
			break;
		case R.id.selectBillType:
			showSelectBillType();
			break;
		case R.id.selectUsers:
			showSelectUsers();
			break;
		case R.id.saveButton:
			addOrEditBill();
			break;
		case R.id.cancelButton:
			finish();
			break;
		}
	}

	private void addOrEditBill() {
		try {
			if (bill.getId() == null) { // 新建
				billService.save(bill);
			} else { // 更新
				billService.update(bill);
			}

			Toast.makeText(BillAddOrEditActivity.this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(TAG, "Create or save bill error", e);
			Toast.makeText(BillAddOrEditActivity.this, R.string.database_error, Toast.LENGTH_LONG).show();
		}
	}

	private void showSelectAccountBookDialog() {
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
				AccountBook accountBook = (AccountBook) adapter.getItem(position);
				accountBookName.setText(accountBook.getName());
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

		final EditText inputText = (EditText) view.findViewById(R.id.inputText);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = "";
				if (inputText.getText() != null) {
					number = inputText.getText().toString();
				}
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
					BillAddOrEditActivity.this.amount.setText(amount.toString());
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

	private void showSelectCategoryDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.category_select_dialog, null);
		ExpandableListView categoryListView = (ExpandableListView) view.findViewById(R.id.categoryList);
		final CategoryExListViewAdapter adapter = new CategoryExListViewAdapter(this, categoryService);
		categoryListView.setAdapter(adapter);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_category).setIcon(R.drawable.category_small_icon).setView(view);
		builder.setNegativeButton(R.string.cancel, null);
		final AlertDialog dialog = builder.create();

		categoryListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				Category category = (Category) adapter.getGroup(groupPosition);
				bill.setCategoryId(category.getId());
				bill.setCategoryName(category.getName());
				categoryName.setText(category.getName());

				dialog.dismiss();
				return true;
			}
		});
		categoryListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Category category = (Category) adapter.getChild(groupPosition, childPosition);
				bill.setCategoryId(category.getId());
				bill.setCategoryName(category.getName());
				categoryName.setText(category.getName());

				dialog.dismiss();
				return true;
			}
		});

		dialog.show();
	}

	private void showSelectBillDate() {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Date date = new Date(year, monthOfYear, dayOfMonth);
				bill.setBillDate(date);
				billDate.setText(DateUtils.formatDate(date, "yyyy-MM-dd"));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		dialog.show();
	}

	private void showSelectBillType() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String[] billTypes = getResources().getStringArray(R.array.BillType);
		builder.setTitle(R.string.select_bill_type);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setItems(billTypes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String type = billTypes[which];
				bill.setBillType(type);
				billType.setText(type);
			}
		});
		builder.show();
	}

	private void showSelectUsers() {
		final List<User> userList = userService.findAll();
		String[] userNames = new String[userList.size()];
		for (int i = 0; i < userList.size(); i++) {
			userNames[i] = userList.get(i).getName();
		}

		final List<User> selectedUsers = new ArrayList<User>();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_user);
		builder.setIcon(R.drawable.user_small_icon);
		builder.setMultiChoiceItems(userNames, new boolean[] {}, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				User user = userList.get(which);
				if (isChecked) {
					selectedUsers.add(user);
				} else {
					selectedUsers.remove(user);
				}
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuilder ids = new StringBuilder();
				for (User user : selectedUsers) {
					ids.append(user.getId()).append(",");
				}

				String idsStr = "";
				if (ids.length() > 0) {
					idsStr = ids.substring(0, ids.length() - 1);
				}
				bill.setUserIds(idsStr);

				userListView.setText(userService.getUserNamesStringByIds(idsStr));
			}
		});

		builder.setNegativeButton(R.string.cancel, null);

		builder.show();
	}
}
