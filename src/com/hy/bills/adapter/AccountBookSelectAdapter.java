package com.hy.bills.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.bills.activity.R;
import com.hy.bills.domain.AccountBook;
import com.hy.bills.service.AccountBookService;

public class AccountBookSelectAdapter extends BaseAdapter {
	private List<AccountBook> accountBookList;
	private Context context;

	public AccountBookSelectAdapter(Context context, AccountBookService accountBookService) {
		accountBookList = accountBookService.findAll();
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(
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