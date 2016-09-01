package cn.mr.ams.android.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.mr.ams.android.R;
import cn.mr.ams.android.app.BaseAmsActivity;
import cn.mr.ams.android.util.TipHelper;
/**
 * win8风格的首页，每个方块均可拖动与其他方块交换其内容，交换完成，交换图标闪烁
 * @author zhangshuo
 * @date
 */
public class Win8StyleActivity extends BaseAmsActivity implements OnTouchListener, OnClickListener, OnLongClickListener{

	private WindowManager				windowManager;
	private WindowManager.LayoutParams	windowParams;

	public static int displayWidth;  //屏幕宽度         
	public static int displayHeight; //屏幕高度 

	// 标志长按控件动作是否激活
	private boolean isMove = false;

	private ImageView					dragImageView;		// 被拖动控件的preview

	private int						fromPoint = -1;//记录被拖动的View

	private int						toPoint = -1;//记录停止拖动时被碰撞的View

	private Drawable			temp_img;//缓存被拖动控件的ImageView的内容

	private String				temp_str;//缓存被拖动控件的TextView的内容

	//移动的位置(目前的确切意思还待考虑)
	private int							dragPointX;
	private int							dragPointY;
	//当前位置距离边界的位置(目前的确切意思还待考虑)
	private int							dragOffsetX;
	private int							dragOffsetY;
	private int x, y;
	//用于循环碰撞的数组
	private View[] views = new View[8];
	private TextView[] tvs = new TextView[8];
	private ImageView[] ivs = new ImageView[8];
	//用来显示经纬度、发包数
	private TextView bottom_bar_up, bottom_bar_down;
	//用于记录碰撞的面积	
	//图标出现和消失的过度动画
	private Animation flash;
	//	private Animation disappear;
	private Animation blink;
	private Animation original;
	/*
	 * 用于记录所有View的坐标的二位数组
	 * points[0][0]用于记录左上角的X
	 * points[0][1]用于记录左上角的Y
	 * points[0][2]用于记录右下角的X
	 * points[0][3]用于记录右下角的Y
	 */
	private int[][] points = new int[8][4];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//        DisplayMetrics dm = this.getResources().getDisplayMetrics();  
//		this.containerWidth = dm.widthPixels;  
//		this.containerHeight = dm.heightPixels-50; 

		initView();//初始化页面

		for(int i = 0; i < views.length; i++){
			views[i].setOnClickListener(this);//绑定点击（短按）监听器
			views[i].setOnLongClickListener(this);//绑定长按监听器
			views[i].setOnTouchListener(this);//绑定触摸监听器
		}
		flash = AnimationUtils.loadAnimation(this, R.anim.flash);
//		disappear = AnimationUtils.loadAnimation(this, R.anim.disappear);
		blink = AnimationUtils.loadAnimation(this, R.anim.blink);
		original = AnimationUtils.loadAnimation(this, R.anim.original);

		updateBottomBar(10, 22, 55555.5555, 5555.5555);

	}

	/**
	 * 触摸监听器
	 * (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		x = (int) event.getX();
		y = (int) event.getY();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(points[0][0] == 0){
					initPoints();
				}
				break;
			case MotionEvent.ACTION_MOVE:

				if(isMove){
					if(dragImageView == null){

						dragPointX = x;
						dragPointY = y;
						dragOffsetX = (int)event.getRawX() - x;
						dragOffsetY = (int)event.getRawY() - y;

						Log.v("getX", String.valueOf(x) + "=====" + String.valueOf(event.getX()));
						Log.v("getY", String.valueOf(y) + "=====" + String.valueOf(event.getY()));
						Log.v("getLeft", String.valueOf(v.getLeft()) + "=====" + String.valueOf(event.getX()-v.getLeft()));
						Log.v("getgetTop", String.valueOf(v.getTop()) + "=====" + String.valueOf(event.getY()-v.getTop()));
						Log.v("getRawX", String.valueOf(event.getRawX()) + "=====" + String.valueOf(event.getRawX() - event.getX()));
						Log.v("getRawY", String.valueOf(event.getRawY()) + "=====" + String.valueOf(event.getRawY() - event.getY()));

						v.destroyDrawingCache();
						v.setDrawingCacheEnabled(true);
						v.setDrawingCacheBackgroundColor(0x000000);
						Bitmap bm = Bitmap.createBitmap(v.getDrawingCache(true));
						Bitmap bitmap = Bitmap.createBitmap(bm, 8, 8, bm.getWidth()-8, bm.getHeight()-8);
						startDrag(bitmap, x, y);
//						v.startAnimation(blink);
						v.setVisibility(View.INVISIBLE);//隐藏当前被长按的控件
					}else{
						onDrag(x, y);
						hide();
						Log.v("OnTouch>>>>", "动了！！！");
					}
				}else{
					Log.v("OnTouch>>>>", "不移动");
				}
				break;
			case MotionEvent.ACTION_UP:
				isMove = false;
				stopDrag();
//				fromPoint = -1;
//				toPoint = -1;
				show();
				exchange();
				break;
		}
		return false;
	}
	/**
	 * 长按监听器
	 * (non-Javadoc)
	 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
	 */
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Log.v("onLongClick>>>>>", "LongClick");
		for(int i = 0; i < views.length; i ++){
			if(v.equals(views[i])){
				fromPoint = i;
				isMove = true;
				TipHelper.Vibrate(this, 100);//震动提醒

				temp_img = ivs[i].getDrawable();//初始化被拖动的View的 ImageView的缓存
				temp_str = tvs[i].getText().toString();//初始化被拖动的View的TextView的缓存

				return true;
			}
		}
		return true;
	}
	/**
	 * 短按监听器
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for(int i = 0; i < views.length; i ++){
			if(v.equals(views[i])){
				String str = tvs[i].getText().toString();
				Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
				if("内存管理".equals(str)){
					Intent it = new Intent();
					it.setClass(this, MemoryManagerActivity.class);
					startActivity(it);
				}else{
					Intent it = new Intent();
					it.setClass(this, OpenBusinessActivity.class);
					startActivity(it);
				}
			}
		}
	}
	/**
	 * 初始化页面所有控件
	 */
	private void initView(){

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		displayWidth = displayMetrics.widthPixels;
		displayHeight = displayMetrics.heightPixels;

		Log.v("displayWidth>>>>>>", String.valueOf(displayWidth));
		Log.v("displayHeight>>>>>>", String.valueOf(displayHeight));


		views[0] = (RelativeLayout) findViewById(R.id.rl1);
		tvs[0] = (TextView) views[0].findViewById(R.id.tv1);
		ivs[0] = (ImageView) views[0].findViewById(R.id.img1);
		views[1] = (RelativeLayout) findViewById(R.id.rl2);
		tvs[1] = (TextView) views[1].findViewById(R.id.tv2);
		ivs[1] = (ImageView) views[1].findViewById(R.id.img2);
		views[2] = (RelativeLayout) findViewById(R.id.rl3);
		tvs[2] = (TextView) views[2].findViewById(R.id.tv3);
		ivs[2] = (ImageView) views[2].findViewById(R.id.img3);
		views[3] = (RelativeLayout) findViewById(R.id.rl4);
		tvs[3] = (TextView) views[3].findViewById(R.id.tv4);
		ivs[3] = (ImageView) views[3].findViewById(R.id.img4);
		views[4] = (RelativeLayout) findViewById(R.id.rl5);
		tvs[4] = (TextView) views[4].findViewById(R.id.tv5);
		ivs[4] = (ImageView) views[4].findViewById(R.id.img5);
		views[5] = (RelativeLayout) findViewById(R.id.rl7);//1/2大小的relativelayout
		tvs[5] = (TextView) views[5].findViewById(R.id.tv7);
		ivs[5] = (ImageView) views[5].findViewById(R.id.img7);
		views[6] = (RelativeLayout) findViewById(R.id.rl8);//1/4大小的relativelayout
		tvs[6] = (TextView) views[6].findViewById(R.id.tv8);
		ivs[6] = (ImageView) views[6].findViewById(R.id.img8);
		views[7] = (RelativeLayout) findViewById(R.id.rl9);//1/4大小的relativelayout
		tvs[7] = (TextView) views[7].findViewById(R.id.tv9);
		ivs[7] = (ImageView) views[7].findViewById(R.id.img9);

		bottom_bar_up = (TextView)findViewById(R.id.bottom_bar_up);
		bottom_bar_down = (TextView)findViewById(R.id.bottom_bar_down);

		int[] power = {0, 1, 2, 3, 4, 5, 6, 7};
		for(int i = 0; i < power.length; i++){
			switch (power[i]){
				case 7:
					tvs[i].setText(getText(R.string.folder));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_folder));
					break;
				case 6:
					tvs[i].setText(getText(R.string.person_info));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_person_info));
					break;
				case 5:
					tvs[i].setText(getText(R.string.system_info));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_system_info));
					break;
				case 4:
					tvs[i].setText(getText(R.string.hidden_danger_report));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_hidden_danger_report));
					break;
				case 3:
					tvs[i].setText(getText(R.string.open));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_open));
					break;
				case 2:
					tvs[i].setText(getText(R.string.qr_code));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_qr_code));
					break;
				case 1:
					tvs[i].setText(getText(R.string.work_order));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_work_order));
					break;
				case 0:
					tvs[i].setText(getText(R.string.routing_inspection));
					ivs[i].setImageDrawable(getResources().getDrawable(R.drawable.icon_routing_inspection));
					break;
			}


		}
	}
	/**
	 * 生成被拖动控件的preView
	 */
	private void startDrag(Bitmap bm, int x, int y)
	{
		Log.v("startDrag>>>>>>", "startDrag");
		stopDrag();
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;

		//左上角相对于屏幕的坐标
		windowParams.x = x - dragPointX + dragOffsetX;
		windowParams.y = y - dragPointY + dragOffsetY - 40;

		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.alpha = 0.5f;

		ImageView iv = new ImageView(Win8StyleActivity.this);
		iv.setImageBitmap(bm);
		windowManager = (WindowManager) Win8StyleActivity.this.getSystemService(
				Context.WINDOW_SERVICE);
		windowManager.addView(iv, windowParams);

		dragImageView = iv;
	}

	/**
	 * 停止绘制，清空preView
	 */
	private void stopDrag()
	{
		if (dragImageView != null)
		{
			windowManager.removeView(dragImageView);
			dragImageView = null;
			Log.v("StopDrag>>>>>>>", "disappear");
		}
	}

	/**
	 * 拖动（Move）过程中不断调整preView的位置，以呈现拖动的效果
	 */
	private void onDrag(int x, int y) {
		Log.v("onDrag>>>>>>", "onDrag");
		if (dragImageView != null) {
			windowParams.alpha = 0.5f;

			windowParams.x = x - dragPointX + dragOffsetX;
			windowParams.y = y - dragPointY + dragOffsetY - 40;

			Log.v("x", String.valueOf(x));
			Log.v("y", String.valueOf(y));
			Log.v("windowParams.x", String.valueOf(windowParams.x));
			Log.v("windowParams.y", String.valueOf(windowParams.y));
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}

	/**
	 * 返回参数View的相对屏幕的绝对坐标值
	 * point[0]用于记录左上角的X
	 * point[1]用于记录左上角的Y
	 * point[2]用于记录右下角的X
	 * point[3]用于记录右下角的Y
	 */
	private int[] getPoint(View v){
		int point[] = new int[4];
		v.getLocationOnScreen(point);
		point[2] = point[0] + v.getWidth();
		point[3] = point[1] + v.getHeight();
		for(int i = 0; i < point.length; i++){
			System.out.println("point[" + i + "]::::::" + point[i]);
		}

		return point;
	}
	/**
	 * 返回被拖动View的中心的坐标
	 */
	private int[] getCentroPoint(View v){
		int point[] = new int[2];
		if(v != null){
			point = new int[2];
			v.getLocationOnScreen(point);
			point[0] = point[0] + v.getWidth()/2;
			point[1] = point[1] + v.getHeight()/2;
			for(int i = 0; i < point.length; i++){
				System.out.println("point[" + i + "]::::::" + point[i]);
			}
		}
		return point;
	}

	/**
	 * 判断被拖动的View的中心点在哪个View内部，就把哪个View隐藏
	 */
	private void hide(){
		int point[] = getCentroPoint(dragImageView);
		for(int i = 0; i < views.length; i++){
			if(point[0] > points[i][0] && point[1] > points[i][1] && point[0] < points[i][2] && point[1] < points[i][3]){
				if(views[i].isShown()){
					toPoint = i;
					onExchange();//将被拖拽的View的原始位置的数据设置为被覆盖的View的数据
					views[i].setVisibility(View.INVISIBLE);
				}
//				if(views[i].getAnimation() == null){
//					views[i].startAnimation(blink);
//					toPoint = i;
//				}

			}else{
//				if(views[i].getAnimation() != null && !views[i].getAnimation().equals(original)){
//					views[i].startAnimation(original);
//				}
//				views[i].clearAnimation();
//				views[i].invalidate();
//				toPoint = -1;
				if(toPoint == i){
					toPoint = -1;
					onExchange();//将被拖拽的View的原始位置的数据恢复初始数据
				}
				if(!views[i].isShown()){
					views[i].setVisibility(View.VISIBLE);
				}
			}
		}

	}
	/**
	 * 拖动结束，将所有的View都显示出来
	 */
	private void show(){
		for(int i = 0; i < views.length; i++){
			if(!views[i].isShown()){
//				toPoint = i;
				views[i].setVisibility(View.VISIBLE);
				views[i].startAnimation(flash);
			}
			views[i].setVisibility(View.VISIBLE);
//			if(views[i].getAnimation() != null && !views[i].getAnimation().equals(flash)){
//				views[i].startAnimation(flash);
//			}
		}
	}
	/**
	 * 初始化所有View的坐标并存放到8行4列的二维数组point[8][4]
	 */
	private void initPoints(){
		for(int i = 0; i < points.length; i++){
			points[i] = getPoint(views[i]);
		}
	}
	/**
	 * 拖拽结束，将fromView和toView的内容进行交换
	 * @return 交换返回true，不交换返回false
	 */
	private boolean exchange(){
		if(fromPoint != -1 && toPoint != -1 && fromPoint != toPoint ){
			tvs[fromPoint].setText(tvs[toPoint].getText());
			ivs[fromPoint].setImageDrawable(ivs[toPoint].getDrawable());
			tvs[toPoint].setText(temp_str);
			ivs[toPoint].setImageDrawable(temp_img);
			views[fromPoint].startAnimation(flash);
//			temp = toPoint;
			fromPoint = -1;
			toPoint = -1;
			return true;
		}else{
			fromPoint = -1;
			toPoint = -1;
			return false;
		}

	}
	/**
	 * 用于被拖动View的原始位置的内容的实时交换
	 * @author zhangshuo
	 * @date 2013-4-15 下午4:46:34
	 * @version
	 */
	private void onExchange(){
		if(fromPoint != -1 && toPoint != -1 && fromPoint != toPoint ){

			//将被拖拽的View的原始位置的数据设置为被覆盖的View的数据

			tvs[fromPoint].setText(tvs[toPoint].getText());
			ivs[fromPoint].setImageDrawable(ivs[toPoint].getDrawable());
			views[fromPoint].startAnimation(blink);

		}else{

			//将被拖拽的View的原始位置的数据恢复初始数据

			tvs[fromPoint].setText(temp_str);
			ivs[fromPoint].setImageDrawable(temp_img);

		}
	}
	/**
	 * 用来显示更新bottom_bar中的数据
	 */
	private void updateBottomBar(int sendPackageNo, int rate, double longitude, double dimension){
		String text_up = "发包数：" + sendPackageNo + " 什么率：" + rate + "%";
		String text_down = "定位：经度  " + longitude + " 维度  " + dimension;
		bottom_bar_up.setText(text_up);
		bottom_bar_down.setText(text_down);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

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
}