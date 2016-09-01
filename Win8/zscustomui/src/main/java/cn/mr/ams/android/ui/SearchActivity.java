package cn.mr.ams.android.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.mr.ams.android.R;
import cn.mr.ams.android.app.BaseAmsActivity;
import cn.mr.ams.android.util.PopListAdapter;
import cn.mr.ams.android.util.PopupWindowHelper;
import cn.mr.ams.android.widget.CustomSpinner;
import cn.mr.ams.android.widget.CustomSpinner.OnSpinnerItemClickListener;

public class SearchActivity extends BaseAmsActivity {

	private TextView tv1;

	private CustomSpinner spinner, et7, et8;

	private List<String> listStr = new ArrayList<String>(Arrays.asList("string 1", "string 2","string 3", "string 4"));
	private PopupWindow window;
	private int displayWidth;
	private int displayHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		tv1 = (TextView) findViewById(R.id.edit1);
		spinner = (CustomSpinner) findViewById(R.id.edit2);
		et7 = (CustomSpinner) findViewById(R.id.edit7);
		et8 = (CustomSpinner) findViewById(R.id.edit8);
		et8.setListStr(listStr);
		tv1.setText(listStr.get(0));
		tv1.setOnClickListener(clickListener);
		spinner.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {

			@Override
			public void onItemClick(String condition, int position) {
				// TODO Auto-generated method stub
				System.out.println("condition: " + condition + "position: " + position);
			}
		});

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		displayWidth = displayMetrics.widthPixels;
		displayHeight = displayMetrics.heightPixels;

		Log.v("displayWidth>>>>>>", String.valueOf(displayWidth));
		Log.v("displayHeight>>>>>>", String.valueOf(displayHeight));
	}

	OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				case R.id.edit1:{
					window = PopupWindowHelper.makePopWindow(SearchActivity.this, initPopView(tv1), v);
					PopupWindowHelper.showPopWindow(SearchActivity.this, displayHeight, window, v);
					break;
				}
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences preferences = getSharedPreferences("system_config", MODE_PRIVATE);
		boolean isChanged = preferences.getBoolean("theme_value", true);
		SharedPreferences.Editor editor = preferences.edit();
		switch (item.getItemId()) {
			case R.id.menu_settings: {

				if (isChanged) {
					isChanged = false;
				} else {
					isChanged = true;
				}
				editor.putBoolean("theme_value", isChanged);
				editor.commit();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
//		super.onBackPressed();
	}

	/**
	 * 初始化PopWindow内部需要显示的布局View
	 * @author zhangshuo
	 * @date 2013-4-25 上午11:23:24
	 * @version
	 *@param tv			当ListView的item被点击，需要动态改变显示数据的TextView
	 *@return			返回初始化完成的布局View
	 */
	private View initPopView(final TextView tv){

		Log.v("initPopView>>>>>>>", "没反应？！");

		View popView = View.inflate(SearchActivity.this, R.layout.pop_list, null);

		ListView popList = (ListView)popView.findViewById(R.id.pop_list);
		PopListAdapter plAdapter = new PopListAdapter(SearchActivity.this, listStr);
		popList.setVerticalScrollBarEnabled(false);// ListView去掉下拉条
		popList.setAdapter(plAdapter);
		popList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				// TODO Auto-generated method stub
				PopListAdapter adapter = new PopListAdapter(SearchActivity.this, listStr);
				tv.setText(adapter.getItem(arg2));
				PopupWindowHelper.hidePopWindow(window);
			}
		});
		return popView;
	}


}
