package com.hy.bills.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hy.bills.MainApplication;
import com.hy.bills.adapter.AccountBookSelectAdapter;
import com.hy.bills.domain.AccountBook;
import com.hy.bills.service.AccountBookService;
import com.hy.bills.service.StatisticsService;

public class StatisticsActivity extends BaseActivity {
	private static final String TAG = "StatisticsActivity";
	private static final int STATISTICS = 1;

	private AccountBookService accountBookService;
	private StatisticsService statisticsService;
	private AccountBook accountBook;
	
	private ProgressDialog progressDialog;
	private TextView statisticsResultView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		appendBodyView(R.layout.statistics_activity);

		statisticsResultView = (TextView) findViewById(R.id.statisticsResult);
		initVariables();

		// 创建滑动菜单
		createSlideMenu(R.array.SlideMenuStatisticsActivity, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toggleSlideMenu();
				if (position == 0) { // 切换账本
					showAccountBookSelectDialog();
				} else if (position == 1) { // 导出表格

				}
			}
		});

		accountBook = accountBookService.getDefaultAccountBook();
		statistics();

		// 设置标题
		String title = getString(R.string.statistics_activity_title, accountBook.getName());
		setTitle(title);
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		accountBookService = application.getAccountBookService();
		statisticsService = application.getStatisticsService();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STATISTICS:
				String result = (String) msg.obj;
				statisticsResultView.setText(result);
				progressDialog.dismiss();
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
	};
	
	private class StatisticsThread extends Thread {
		@Override
		public void run() {
			String result = statisticsService.getBillStatistics(accountBook.getId());;
			Message message = new Message();
			message.what = STATISTICS;
			message.obj = result;
			handler.sendMessage(message);
		}
	}

	private void statistics() {
		showProgressDialog();
		new StatisticsThread().start();
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
				statistics();

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(R.string.statistics_progres_dialog_title);
		progressDialog.setMessage(getString(R.string.statistics_progress_dialog_message));
		progressDialog.show();
	}
}
