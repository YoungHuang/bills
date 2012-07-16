package com.hy.bills.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hy.bills.adapter.GridViewAdapter;

public class StartActivity extends Activity implements OnItemClickListener {
	private LinearLayout mainLayout;
	private GridViewAdapter gridViewAdapter;
	private boolean isClosed = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);

		// 隐藏后退按钮
		ImageView backBotton = (ImageView) this.findViewById(R.id.backButton);
		backBotton.setVisibility(View.GONE);

		GridView gridMenu = (GridView) this.findViewById(R.id.gridMenu);
		gridViewAdapter = new GridViewAdapter(this);
		gridMenu.setAdapter(gridViewAdapter);
		gridMenu.setOnItemClickListener(this);
		
		mainLayout = (LinearLayout) this.findViewById(R.id.mainLayout);
	}
	
	protected void createSlideMenu() {
		
	}
	
	protected void toggleSlideMenu() {
		if(isClosed) {
			// Open menu
			
		} else {
			// Close menu
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:

			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:

			break;
		default:
			break;
		}
	}
}
