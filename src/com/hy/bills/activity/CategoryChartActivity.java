package com.hy.bills.activity;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hy.bills.MainApplication;
import com.hy.bills.service.CategoryService;
import com.hy.bills.service.CategoryService.CategoryStatistics;

public class CategoryChartActivity extends BaseActivity {
	private static final String TAG = "CategoryChartActivity";

	private CategoryService categoryService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		hideFooterLayout();

		initVariables();

		View pieView = statisticCategory();
		appendBodyView(pieView);
	}

	private void initVariables() {
		MainApplication application = (MainApplication) getApplicationContext();
		categoryService = application.getCategoryService();
	}

	private View statisticCategory() {
		int id = getIntent().getExtras().getInt("categoryId");
		List<CategoryStatistics> statsList = categoryService.getCategoryStatistics(id);
		int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
		DefaultRenderer renderer = buildCategoryRenderer(colors, statsList);
		View view = ChartFactory.getPieChartView(this, buildCategoryDataset("消费类别统计", statsList), renderer);

		return view;
	}

	private DefaultRenderer buildCategoryRenderer(int[] colors, List<CategoryStatistics> statsList) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setZoomButtonsVisible(true);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setLabelsColor(Color.BLUE);
		renderer.setMargins(new int[] { 20, 30, 15, 10 });

		for (int i = 0; i < statsList.size(); i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i % colors.length]);
			renderer.addSeriesRenderer(r);
		}

		return renderer;
	}
	
	private CategorySeries buildCategoryDataset(String title, List<CategoryStatistics> statsList) {
		CategorySeries series = new CategorySeries(title);
		for (CategoryStatistics stat : statsList) {
			series.add("数量： " + stat.categoryName, stat.count);
		}
		
		return series;
	}
}
