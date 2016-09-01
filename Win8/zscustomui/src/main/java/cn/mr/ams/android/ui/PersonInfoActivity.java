package cn.mr.ams.android.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.mr.ams.android.R;
import cn.mr.ams.android.util.PopListWithTitleAdapter;
import cn.mr.ams.android.util.PopupWindowHelper;

public class PersonInfoActivity extends Activity {

	private TextView tv;
	private PopupWindow window;
	private int displayWidth, displayHeight;
	private List<String> listStr = new ArrayList<String>(Arrays.asList("经纬度采集", "查看工单","现场拍照", "上传照片", "施工完成", "施工延时"));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		tv = (TextView) findViewById(R.id.tv_person_info);
		tv.setOnClickListener(clickListener);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		displayWidth = displayMetrics.widthPixels;
		displayHeight = displayMetrics.heightPixels;
	}

	OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				case R.id.tv_person_info:{
					window = PopupWindowHelper.makePopWindow(PersonInfoActivity.this, initPopView(tv));
					PopupWindowHelper.showPopWindow(PersonInfoActivity.this, displayHeight, window, v);
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

		View popView = View.inflate(PersonInfoActivity.this, R.layout.pop_list_with_title, null);

		ListView popList = (ListView)popView.findViewById(R.id.pop_list_with_title);
		PopListWithTitleAdapter plAdapter = new PopListWithTitleAdapter(PersonInfoActivity.this, listStr);
		popList.setVerticalScrollBarEnabled(false);// ListView去掉下拉条
		popList.setAdapter(plAdapter);
		popList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				// TODO Auto-generated method stub
				PopListWithTitleAdapter adapter = new PopListWithTitleAdapter(PersonInfoActivity.this, listStr);
				tv.setText(adapter.getItem(arg2));
				PopupWindowHelper.hidePopWindow(window);
			}
		});
		return popView;
	}
}
