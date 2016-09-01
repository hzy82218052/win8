package cn.mr.ams.android.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;
import cn.mr.ams.android.R;
import cn.mr.ams.android.app.BaseAmsActivity;
import cn.mr.ams.android.app.GlobalAmsApplication;

@SuppressWarnings("deprecation")
public class OpenBusinessActivity extends BaseAmsActivity implements OnCheckedChangeListener, OnClickListener{

	private RadioGroup mainTab;
	private TabHost tabhost;
	private Intent search, systemInfo, personInfo, more;

	private View subTitle;//包含三个RadioButton的副栏
	private RadioGroup subTitleTab;
	private RadioButton btLeft, btCenter, btRight;
	private boolean left = false;
	private boolean center = false;
	private boolean right = false;

	private PopupWindow window;//悬浮框
	private List<String> listStr = new ArrayList<String>(Arrays.asList("日", "月", "神", "教"));

	private RadioGroup rg_distance, rg_time, rg_cycle;
	private RadioButton rb_five_hm, rb_one_km, rb_two_km, rb_five_km, rb_full_city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_business);
		LocalActivityManager activityGroup = new LocalActivityManager(this, true);
		activityGroup.dispatchCreate(savedInstanceState);
		tabhost = (TabHost)findViewById(R.id.tabhost);
		tabhost.setup(activityGroup);
		mainTab = (RadioGroup) findViewById(R.id.main_tab);

		mainTab.setOnCheckedChangeListener(this);


		subTitle = (View)findViewById(R.id.subtitle_test);
		subTitleTab  = (RadioGroup)subTitle.findViewById(R.id.subtitle_radio_group);
		btLeft = (RadioButton)subTitleTab.findViewById(R.id.subtitle_left_bt);
		btCenter = (RadioButton)subTitleTab.findViewById(R.id.subtitle_center_bt);
		btRight = (RadioButton)subTitleTab.findViewById(R.id.subtitle_right_bt);
		btLeft.setOnClickListener(this);
		btRight.setOnClickListener(this);
		btCenter.setOnClickListener(this);

		search = new Intent(this, SearchActivity.class);
		tabhost.addTab(tabhost.newTabSpec("search")
				.setIndicator(getResources().getString(R.string.search), getResources()
						.getDrawable(R.drawable.icon_1_n)).setContent(search));

		systemInfo = new Intent(this, SystemInfoActivity.class);
		tabhost.addTab(tabhost.newTabSpec("systemInfo")
				.setIndicator(getResources().getString(R.string.system_info), getResources()
						.getDrawable(R.drawable.icon_2_n)).setContent(systemInfo));

		personInfo = new Intent(this, PersonInfoActivity.class);
		tabhost.addTab(tabhost.newTabSpec("personInfo")
				.setIndicator(getResources().getString(R.string.person_info), getResources()
						.getDrawable(R.drawable.icon_3_n)).setContent(personInfo));

		more = new Intent(this, MoreActivity.class);
		tabhost.addTab(tabhost.newTabSpec("more")
				.setIndicator(getResources().getString(R.string.more), getResources()
						.getDrawable(R.drawable.icon_5_n)).setContent(more));

		/**
		 * 获取Tab栏（BottomBar）的高度
		 * 并将其保存到GlobalVariableApplication中，作为全局变量使用
		 */
		ViewTreeObserver vto2 = mainTab.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mainTab.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				GlobalAmsApplication application = (GlobalAmsApplication) OpenBusinessActivity.this.getApplicationContext();
				application.setBottomBarHeight(mainTab.getHeight());
			}
		});


	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){
			case R.id.search:
				this.tabhost.setCurrentTabByTag("search");
				break;
			case R.id.system_info:
				this.tabhost.setCurrentTabByTag("systemInfo");
				break;
			case R.id.person_info:
				this.tabhost.setCurrentTabByTag("personInfo");
				break;
			case R.id.more:
				this.tabhost.setCurrentTabByTag("more");
				break;
			case R.id.btn_distance_five_hm:{
				Toast.makeText(OpenBusinessActivity.this, "500m", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_distance_one_km:{
				Toast.makeText(OpenBusinessActivity.this, "1km", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_distance_two_km:{
				Toast.makeText(OpenBusinessActivity.this, "2km", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_distance_five_km:{
				Toast.makeText(OpenBusinessActivity.this, "5km", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_distance_full_city:{
				Toast.makeText(OpenBusinessActivity.this, "全城", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_time_last:{
				Toast.makeText(OpenBusinessActivity.this, "最新", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_time_old:{
				Toast.makeText(OpenBusinessActivity.this, "较早", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_cycle_day:{
				Toast.makeText(OpenBusinessActivity.this, "日", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_cycle_week:{
				Toast.makeText(OpenBusinessActivity.this, "周", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_cycle_half_month:{
				Toast.makeText(OpenBusinessActivity.this, "半月", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_cycle_month:{
				Toast.makeText(OpenBusinessActivity.this, "月", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_cycle_half_year:{
				Toast.makeText(OpenBusinessActivity.this, "半年", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.btn_cycle_year:{
				Toast.makeText(OpenBusinessActivity.this, "年", Toast.LENGTH_SHORT).show();
				break;
			}
			default: {
				Toast.makeText(OpenBusinessActivity.this, "默认属于出错", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.v("onClick>>>>>>>>>>>>", "RadioButton被点击");
		switch(v.getId()){
			case R.id.subtitle_left_bt:{

				if(left){
					left = false;
					subTitleTab.clearCheck();
					Log.v("Left>>>>>", "dismiss");
				}else{
					if(null != window && window.isShowing()){
						window.dismiss();
						window = null;
					}
					// 显示 popupWindow
					makePopupWindow(OpenBusinessActivity.this, subTitleTab, initPopView(OpenBusinessActivity.this, listStr, LinearLayout.HORIZONTAL));
					int[] xy = getLocation(subTitleTab);
					window.showAtLocation(subTitleTab,Gravity.LEFT|Gravity.TOP,xy[0],xy[1]+subTitleTab.getHeight());
					left = true;
					center = false;
					right = false;
					Log.v("Left>>>>>", "show");
				}
				break;
			}
			case R.id.subtitle_center_bt:{

				if(center){
					center = false;
					subTitleTab.clearCheck();
					Log.v("center>>>>>", "dismiss");
				}else{
					if(null != window && window.isShowing()){
						window.dismiss();
						window = null;
					}
					// 显示 popupWindow
					makePopupWindow(OpenBusinessActivity.this, v, initPopView(OpenBusinessActivity.this, listStr, LinearLayout.VERTICAL));
					int[] xy = getLocation(v);
					window.showAtLocation(v,Gravity.LEFT|Gravity.TOP,xy[0],xy[1]+v.getHeight());
					left = false;
					center = true;
					right = false;
					Log.v("center>>>>>", "show");
				}

				break;
			}
			case R.id.subtitle_right_bt:{

				if(right){
					right = false;
					subTitleTab.clearCheck();
					Log.v("right>>>>>", "dismiss");
				}else{
					if(null != window && window.isShowing()){
						window.dismiss();
						window = null;
					}
					// 显示 popupWindow
					makePopupWindow(OpenBusinessActivity.this, subTitleTab, initPopView(OpenBusinessActivity.this, R.layout.pop_cycle));
					int[] xy = getLocation(subTitleTab);
					window.showAtLocation(subTitleTab,Gravity.LEFT|Gravity.TOP,xy[0],xy[1]+subTitleTab.getHeight());
					left = false;
					center = false;
					right = true;
					Log.v("right>>>>>", "show");
				}

				break;
			}
		}
	}
	private View initPopView(Context cx, List<String> strs, int orientation){
		LinearLayout mLLayout = new LinearLayout(this);
		mLLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		mLLayout.setOrientation(orientation);

		RadioGroup mRGroup = new RadioGroup(this);
		RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,RadioGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 15, 10, 15);
		mRGroup.setLayoutParams(lp);
		mRGroup.setOrientation(orientation);
		mRGroup.setGravity(Gravity.CENTER_VERTICAL);
		mRGroup.setBackgroundColor(this.getResources().getColor(R.color.home_tab_bg_normal));

		RadioButton mRBtn = null;
		for(int i = 0; i < strs.size(); i++){
			if(orientation == LinearLayout.HORIZONTAL){
				mRBtn = (RadioButton)LayoutInflater.from(cx).inflate(R.layout.radio_button_horizontal, null);
			}else{
				mRBtn = (RadioButton)LayoutInflater.from(cx).inflate(R.layout.radio_button_vertial, null);
			}

			RadioGroup.LayoutParams lps = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
			lps.weight = 1.0f;
			mRBtn.setLayoutParams(lps);
			mRBtn.setText(strs.get(i));
			final String str = mRBtn.getText().toString();
			mRBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(OpenBusinessActivity.this, str, Toast.LENGTH_SHORT).show();
				}
			});

			mRGroup.addView(mRBtn);
		}

		mRGroup.setOnCheckedChangeListener(this);
		mLLayout.addView(mRGroup);


		return mLLayout;
	}
	private View initPopView(Context cx, int layoutId){
		View view = View.inflate(cx, layoutId, null);
		switch (layoutId){
			case R.layout.pop_distance:{
				rg_distance = (RadioGroup) view.findViewById(R.id.pop_distance_tab);
				rg_distance.setOnCheckedChangeListener(OpenBusinessActivity.this);
				break;
			}
			case R.layout.pop_time:{
				rg_time = (RadioGroup) view.findViewById(R.id.pop_time_tab);
				rg_time.setOnCheckedChangeListener(OpenBusinessActivity.this);
				break;
			}
			case R.layout.pop_cycle:{
				rg_cycle = (RadioGroup) view.findViewById(R.id.pop_cycle_tab);
				rg_cycle.setOnCheckedChangeListener(OpenBusinessActivity.this);
				break;
			}
		}
		return view;
	}
	private int[] getLocation(View v){
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		return location;
	}
	// 创建一个包含自定义view的PopupWindow
	private PopupWindow makePopupWindow(Context cx, View v, View layoutView)
	{
		Log.v("makePopupWindow>>>>>", "show");
		window = new PopupWindow(cx);

		window.setContentView(layoutView);
		/**
		 * 监听popwindow，当PopWindow消失时，将副栏的TAB中的RadioButton恢复原状
		 */
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				left = false;
				center = false;
				right = false;
				subTitleTab.clearCheck();
				Log.v("setOnDismissListener", "dismiss");
			}
		});
		window.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_spinner_bg));
		window.setWidth(v.getWidth());
		window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

		//设置PopWindow弹出和隐藏的动画
		window.setAnimationStyle(R.style.pop_anim_in_up_to_down);

		// 设置PopupWindow外部区域是否可触摸
		window.setFocusable(true); //设置PopupWindow可获得焦点
		window.setTouchable(true); //设置PopupWindow可触摸
		window.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		return window;
	}


//    class PopListAdapter extends BaseAdapter{
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return listStr.size();
//		}
//
//		@Override
//		public String getItem(int position) {
//			// TODO Auto-generated method stub
//			return listStr.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_list_item, null);
//			TextView tv = (TextView) convertView.findViewById(R.id.tv_pop_list_item);
//			tv.setText(listStr.get(position));
//			return convertView;
//		}
//    	
//    }
}
