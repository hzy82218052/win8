package cn.mr.ams.android.widget;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import cn.mr.ams.android.R;

/**
 * 自定义下拉列表
 *@author zhangshuo
 *@date 2013-8-8 下午3:36:13
 *@version
 * description:
 */
@SuppressLint("NewApi")
public class CustomSpinner extends LinearLayout implements OnClickListener{

	private Context mContext;
	private RadioGroup mRGroup;
	private RadioButton mRButton;
	private List<String> listStr = null;
	/**
	 * PopupWindow的内容视图
	 */
	private View popView = null;
	private ListView listView;

	private int btnTextColor;
	private int itemTextColor;
	private Drawable btnDrawable;
	private Drawable itemDrawable;
	/**
	 * 屏幕的宽
	 */
	private int displayWidth;
	/**
	 * 屏幕的高
	 */
	private int displayHeight;
	/**
	 * PopupWindow相对于描点的高度的倍数
	 */
	private int n;
	/**
	 * PopupWindow中ListView的item的宽度，即作为锚点的View的宽度
	 */
	private int itemWeight;
	/**
	 * PopupWindow中的ListView 的item的高度，即作为锚点的View的高度
	 */
	private int itemHeight;
	/**
	 * 监听PopupWindow中的item是否被点击的监听器
	 */
	private OnSpinnerItemClickListener onSpinnerItemClickListener;

	/**
	 * 当RadioButton被点击，弹出用来显示具体内容的悬浮框
	 */
	private PopupWindow popWindow;

	public CustomSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context, null, 0);
	}

	public CustomSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs, 0);
	}

	public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle){
		this.mContext = context;
		/**
		 * 初始化主题颜色
		 */
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinnerAttr, defStyle, 0);
		btnTextColor = a.getColor(R.styleable.CustomSpinnerAttr_buttonTextColor, getResources().getColor(R.color.subtitle_bg));
		itemTextColor = a.getColor(R.styleable.CustomSpinnerAttr_itemTextColor, getResources().getColor(R.color.almost_white));
		btnDrawable = a.getDrawable(R.styleable.CustomSpinnerAttr_buttonBackgroundDrawable);
		itemDrawable = a.getDrawable(R.styleable.CustomSpinnerAttr_itemBackgroundDrawable);
		System.out.println("btnDrawbale: " +btnDrawable + "   itemDrawble: " + itemDrawable);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.layout_spinner, this);

		mRGroup = (RadioGroup) linearLayout.findViewById(R.id.spinner_radio_group);
		mRButton = (RadioButton) mRGroup.findViewById(R.id.spinner_radiobutton);
//		mRButton.setBackground(btnDrawable);
		mRButton.setTextColor(btnTextColor);
		mRButton.setOnClickListener(this);

		int arrayID = a.getResourceId(R.styleable.CustomSpinnerAttr_contents, -1);
		if(arrayID == -1){
			Toast.makeText(mContext, "读取下拉条目内容失败", Toast.LENGTH_SHORT).show();
		}else{
			setListStr(Arrays.asList(getResources().getStringArray(arrayID)));
		}

		a.recycle();
		/*
		 * 获取屏幕的宽和高
		 */
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		displayWidth = displayMetrics.widthPixels;
		displayHeight = displayMetrics.heightPixels;
		Log.v("有木有displayWidth>>>>>>", String.valueOf(displayWidth));
		Log.v("有木有displayHeight>>>>>>", String.valueOf(displayHeight));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.spinner_radiobutton:{
				if(null == listStr){
					Toast.makeText(mContext, "对不起，没有数据", Toast.LENGTH_SHORT).show();
					mRGroup.clearCheck();
				}else{
					itemWeight = v.getWidth();
					itemHeight = v.getHeight();
					if(null == popView){
						popView = initPopView(listStr);
					}
					showPopWindow(v, popView);
				}
				break;
			}
		}
	}
	private View initPopView(List<String> listStrs) {
		final View popView = View.inflate(mContext, R.layout.pop_list, null);
		listView = (ListView)popView.findViewById(R.id.pop_list);
		SpinnerListAdapter adapter = new SpinnerListAdapter(mContext, listStrs, itemHeight, itemDrawable, itemTextColor);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(mItemClickListener);
		listView.setBackgroundColor(getResources().getColor(R.color.black));
//		listView.setSelector(android.R.color.transparent);

		popView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final int x = (int) event.getX();
				final int y = (int) event.getY();

				if ((event.getAction() == MotionEvent.ACTION_DOWN)
						&& ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
					hidePopWindow();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					hidePopWindow();
					return true;
				} else {
					return popView.onTouchEvent(event);
				}

			}
		});
		popView.setFocusableInTouchMode(true);//设置此属性是为了让popView的返回键监听器生效，否则无法监听
		popView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.v("返回键", "被按下！");
				if(keyCode == event.KEYCODE_BACK){
					if(popWindow != null && popWindow.isShowing()){
						hidePopWindow();
						return true;
					}
				}
				return false;
			}
		});
		return popView;
	}

	/**
	 * 将PopWindow初始化并显示
	 * @author zhangshuo
	 * @date 2013-4-23 上午11:26:22
	 * @version
	 *@param referView 作为锚点的View
	 *@param layoutView 作为PopWindow内部的布局样式
	 */
	private void showPopWindow(View referView, final View layoutView) {
		if (null == layoutView) {
			return ;
		}
		popWindow = new PopupWindow(layoutView, itemWeight,
				itemHeight*n);
		/**
		 * 监听popwindow，当PopWindow消失时，将副栏的TAB中的RadioButton恢复原状
		 */
		popWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mRGroup.clearCheck();
			}
		});

		/**
		 * 设置背景为空，解决点击PopupWindow中的RadioButton是RadioButton的背景变为透明的问题
		 */
		popWindow.setBackgroundDrawable(null);

		/**
		 * 去的锚点v的左上角的坐标，并存放到数组xy[]中
		 */
		int[] xy = getLocation(referView);

		/**
		 *   根据PopWindow的大小及描点坐标计算出PopWindow的弹出方向
		 *   并根据方向设置PopWindow弹出和隐藏的动画
		 */
		System.out.println("描点的Y值：   " + xy[1]);
		System.out.println("popWindow的高：  " + popWindow.getHeight());
		System.out.println("描点的高：   " + referView.getHeight());
		int height = xy[1] + popWindow.getHeight() + referView.getHeight();
		int locationX, locationY;
		if(height > displayHeight){
			//此时的PopWindow会在描点referView的上方弹出
			popWindow.setAnimationStyle(R.style.pop_anim_in_down_to_up);
			locationX = xy[0];
			locationY = xy[1] - popWindow.getHeight();
		}else{
			//此时的PopWindow会在描点referView的下方弹出
			popWindow.setAnimationStyle(R.style.pop_anim_in_up_to_down);
			locationX = xy[0];
			locationY = xy[1] + referView.getHeight();
		}

		/**
		 * 设置PopupWindow外部区域是否可触摸
		 */
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸

		/**
		 * 将PopWindow在相对于锚点的指定坐标显示出来
		 */
//        popWindow.showAsDropDown(referView);
		popWindow.showAtLocation(this, Gravity.NO_GRAVITY, locationX, locationY);
	}

	/**
	 * 隐藏PopWindow
	 * @author zhangshuo
	 * @date 2013-4-23 下午1:57:23
	 * @version
	 */
	public void hidePopWindow(){
		if(null != popWindow && popWindow.isShowing()){
			System.out.println("popWindow的Height：" + popWindow.getHeight() + " Y: " + getLocation(popView)[1]);
			popWindow.dismiss();
			popWindow = null;
		}
	}
	/**
	 * @author zhangshuo
	 * @date 2013-4-23 上午11:45:16
	 * @version
	 *@param v
	 *@return 返回参数View的坐标
	 */
	private int[] getLocation(View v){
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		return location;
	}

	public void setOnSpinnerItemClickListener(
			OnSpinnerItemClickListener onSpinnerItemClickListener) {
		this.onSpinnerItemClickListener = onSpinnerItemClickListener;
	}

	public interface OnSpinnerItemClickListener{
		public void onItemClick(String condition, int position);
	}

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			ListView listView = (ListView)parent;
			String condition = listView.getItemAtPosition(position).toString();
			if(onSpinnerItemClickListener != null)
				onSpinnerItemClickListener.onItemClick(condition, position);
			mRButton.setText(condition);
			hidePopWindow();
		}
	};
	/**
	 * 返回PopupWindow的内容
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:09:35
	 * @version
	 *@return
	 */
	public List<String> getListStr() {
		return listStr;
	}
	/**
	 * 设置PopupWindow中的内容
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:09:12
	 * @version
	 *@param listStr
	 */
	public void setListStr(List<String> listStr) {
		if(listStr.size() > 0){
			this.listStr = listStr;
			this.mRButton.setText(listStr.get(0));
			if(listStr.size() > 4){
				this.n = 4;
			}else{
				this.n = listStr.size();
			}
		}
	}
	/**
	 * 获取下拉按钮的字体颜色
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:10:33
	 * @version
	 *@return
	 */
	public int getBtnTextColor() {
		return btnTextColor;
	}
	/**
	 * 设置下拉按钮的字体颜色
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:11:15
	 * @version
	 *@param btnColor
	 */
	public void setBtnTextColor(int btnColor) {
		this.btnTextColor = btnColor;
		this.mRGroup.setBackgroundColor(btnColor);
	}
	/**
	 * 获取PopupWindow中条目的字体色
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:11:34
	 * @version
	 *@return
	 */
	public int getItemTextColor() {
		return itemTextColor;
	}
	/**
	 * 设置PopupWindow中条目的字体色
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:11:54
	 * @version
	 *@param itemColor
	 */
	public void setItemTextColor(int itemColor) {
		this.itemTextColor = itemColor;
		this.listView.setBackgroundColor(itemColor);
	}
	/**
	 * 得到下拉按钮的背景图片或Selector
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:12:19
	 * @version
	 *@return
	 */
	public Drawable getBtnDrawable() {
		return btnDrawable;
	}
	/**
	 * 设置下拉按钮的背景图片或Selector
	 * 此方法已无效
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:12:50
	 * @version
	 *@param btnDrawable
	 */
	public void setBtnDrawable(Drawable btnDrawable) {
		this.btnDrawable = btnDrawable;
		this.mRGroup.setBackground(btnDrawable);
	}
	/**
	 * 获取PopupWindow中条目的背景图片或Selector
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:13:05
	 * @version
	 *@return
	 */
	public Drawable getItemDrawable() {
		return itemDrawable;
	}
	/**
	 * 设置PopupWindow中条目的背景图片或Selector
	 * @author zhangshuo
	 * @date 2013-8-9 下午3:13:27
	 * @version
	 *@param itemDrawable
	 */
	public void setItemDrawable(Drawable itemDrawable) {
		this.itemDrawable = itemDrawable;
		this.listView.setBackground(itemDrawable);
	}

}
