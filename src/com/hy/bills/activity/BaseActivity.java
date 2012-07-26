package com.hy.bills.activity;

import android.app.Activity;
import android.content.Intent;

public class BaseActivity extends Activity {
	protected void startActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}
}
