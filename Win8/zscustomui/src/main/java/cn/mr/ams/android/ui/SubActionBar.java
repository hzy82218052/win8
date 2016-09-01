package cn.mr.ams.android.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.mr.ams.android.R;

/**
 *@author zhangshuo
 *@date 2013-4-22 下午3:26:22
 *@version
 * description:包含三个选择按钮的副顶栏
 */
public class SubActionBar extends LinearLayout{

	private Context mContext;

	private RadioGroup mRGroup;

	private RadioButton mRBtnLeft;

	private RadioButton mRBtnCenter;

	private RadioButton mRBtnRight;

	private List<String> leftStrs = null;
	private List<String> centerStrs = null;
	private List<String> rightStrs = null;

	private View leftView = null;
	private View centerView = null;
	private View rightView = null;

	/**
	 * 当RadioButton被点击，弹出用来显示具体内容的悬浮框
	 */

	private PopupWindow window;

	private int subTitleColor;
	private int popColor;

	private OnSubActionClickListener onSubActionClickListener;

	public SubActionBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context, null, 0);
	}
	public SubActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs, 0);
	}
	@SuppressLint("NewApi")
	public SubActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs, defStyle);
	}
	private void init(Context context, AttributeSet attrs, int defStyle){
		this.mContext = context;

		/**
		 * 初始化主题颜色
		 */
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.subTitleAttr, defStyle, 0);
		subTitleColor = a.getColor(R.styleable.subTitleAttr_subTitleBackgroundColor, getResources().getColor(R.color.subtitle_bg));
		popColor = a.getColor(R.styleable.subTitleAttr_subTitlePopBackgroudColor, getResources().getColor(R.color.home_tab_bg_normal));

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.subtitle_three_button, this);

		mRGroup = (RadioGroup) linearLayout.findViewById(R.id.subtitle_radio_group);
		mRBtnLeft = (RadioButton) mRGroup.findViewById(R.id.subtitle_left_bt);
		mRBtnCenter = (RadioButton) mRGroup.findViewById(R.id.subtitle_center_bt);
		mRBtnRight = (RadioButton) mRGroup.findViewById(R.id.subtitle_right_bt);

		mRBtnLeft.setBackgroundColor(subTitleColor);
		mRBtnCenter.setBackgroundColor(subTitleColor);
		mRBtnRight.setBackgroundColor(subTitleColor);

		mRBtnLeft.setOnClickListener(mRBtnClickListener);
		mRBtnCenter.setOnClickListener(mRBtnClickListener);
		mRBtnRight.setOnClickListener(mRBtnClickListener);

		a.recycle();
	}

	public void setLeftText(CharSequence text){
		mRBtnLeft.setText(text);
	}
	public void setCenterText(CharSequence text){
		mRBtnCenter.setText(text);
	}
	public void setRightText(CharSequence text){
		mRBtnRight.setText(text);
	}

	public List<String> getLeftStrs() {
		return leftStrs;
	}
	public void setLeftStrs(List<String> leftStrs) {
		this.leftStrs = leftStrs;
	}
	public List<String> getCenterStrs() {
		return centerStrs;
	}
	public void setCenterStrs(List<String> centerStrs) {
		this.centerStrs = centerStrs;
	}
	public List<String> getRightStrs() {
		return rightStrs;
	}
	public void setRightStrs(List<String> rightStrs) {
		this.rightStrs = rightStrs;
	}
	/**
	 * 初始化PopWindow要显示的样子ContentView
	 * @author zhangshuo
	 * @date 2013-4-23 下午1:59:52
	 * @version
	 *@param orientation 标示是纵向还是横向
	 *@return PopWindow要显示的样子（ContentView）
	 */
	private View initPopView(List<String> listStrs, int orientation){
		final LinearLayout mLLayout = new LinearLayout(mContext);
		mLLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		mLLayout.setOrientation(orientation);

		final RadioGroup mRGroup = new RadioGroup(mContext);
		RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,RadioGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 15, 10, 15);
		mRGroup.setLayoutParams(lp);
		mRGroup.setOrientation(orientation);
		mRGroup.setGravity(Gravity.CENTER_VERTICAL);
		mRGroup.setBackgroundColor(this.getResources().getColor(R.color.home_tab_bg_normal));

		RadioButton mRBtn = null;
		for(int i = 0; i < listStrs.size(); i++){
			if(orientation == LinearLayout.HORIZONTAL){
				mRBtn = (RadioButton)LayoutInflater.from(mContext).inflate(R.layout.radio_button_horizontal, null);
			}else{
				mRBtn = (RadioButton)LayoutInflater.from(mContext).inflate(R.layout.radio_button_vertial, null);
			}

			RadioGroup.LayoutParams lps = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
			lps.weight = 1.0f;
			mRBtn.setLayoutParams(lps);
			mRBtn.setText(listStrs.get(i));
			mRBtn.setTag(listStrs.get(i));//设置标签，以便在onClickListener中进行分辨
			mRBtn.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:{
							mRGroup.clearCheck();
							break;
						}
					}
					return false;
				}
			});
			mRBtn.setOnClickListener(mRBtnClickListener);
			mRBtn.setBackgroundColor(popColor);
			mRGroup.addView(mRBtn);
		}
		mLLayout.addView(mRGroup);
		/**
		 * 重写PopupWindow的ContentView（及么LLayout）的TouchListener方法
		 * 解决设置PopupWindow的背景为null时，setOutSideTouchable(true)无效的问题即点击PopupWindow外的地方
		 * PopupWindow不会自动dismiss的问题
		 */
		mLLayout.setOnTouchListener(new OnTouchListener() {

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
					return mLLayout.onTouchEvent(event);
				}

			}
		});
		return mLLayout;
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

	/**
	 * 将PopWindow初始化并显示
	 * @author zhangshuo
	 * @date 2013-4-23 上午11:26:22
	 * @version
	 *@param referView 作为锚点的View
	 *@param layoutView 作为PopWindow内部的布局样式
	 */

	private void showPopWindow(View referView, View layoutView){

		window = new PopupWindow(layoutView, referView.getWidth(),
				LayoutParams.WRAP_CONTENT);

		/**
		 * 监听popwindow，当PopWindow消失时，将副栏的TAB中的RadioButton恢复原状
		 */
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mRGroup.clearCheck();
			}
		});

		/**
		 * 设置背景为空，解决点击PopupWindow中的RadioButton是RadioButton的背景变为透明的问题
		 */
		window.setBackgroundDrawable(null);

		/**
		 *   设置PopWindow弹出和隐藏的动画
		 */

		window.setAnimationStyle(R.style.pop_anim_in_up_to_down);

		/**
		 * 设置PopupWindow外部区域是否可触摸
		 */
		window.setFocusable(true); //设置PopupWindow可获得焦点
		window.setTouchable(true); //设置PopupWindow可触摸
		window.setOutsideTouchable(true); //设置非PopupWindow区域可触摸

		/**
		 * 将PopWindow在相对于锚点的指定坐标显示出来
		 */
		int[] xy = getLocation(referView);
		window.showAtLocation(referView,Gravity.LEFT|Gravity.TOP,xy[0],xy[1]+referView.getHeight());

		window.update();
	}

	/**
	 * 隐藏PopWindow
	 * @author zhangshuo
	 * @date 2013-4-23 下午1:57:23
	 * @version
	 */
	public void hidePopWindow(){
		if(null != window && window.isShowing()){
			window.dismiss();
			window = null;
		}
	}

	/**
	 * @author zhangshuo
	 * @date 2013-4-23 上午9:46:49
	 * @version
	 *@param listener
	 * 为subTitle上的三个RadioButton点击事件
	 */
	public void setOnSubActionClickListener(OnSubActionClickListener listener){
		onSubActionClickListener = listener;
	}

	public interface OnSubActionClickListener{
		public void onSubActionClick(String tag);
	}

	OnClickListener mRBtnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v("View的值：", v.getTag().toString());
			if(onSubActionClickListener != null){
				onSubActionClickListener.onSubActionClick(v.getTag().toString());
			}

			/**
			 * 重新定义RadioButton的点击样式
			 */
			switch(v.getId()){
				case R.id.subtitle_left_bt:{

					if(null == leftView){
						leftView = initPopView(leftStrs, LinearLayout.HORIZONTAL);
					}

					showPopWindow(mRGroup, leftView);

					break;
				}
				case R.id.subtitle_center_bt:{

					if(null == centerView){
						centerView = initPopView(centerStrs, LinearLayout.VERTICAL);
					}

					showPopWindow(v, centerView);

					break;
				}
				case R.id.subtitle_right_bt:{

					if(null == rightView){
						rightView = initPopView(rightStrs, LinearLayout.HORIZONTAL);
					}

					showPopWindow(mRGroup, rightView);

					break;
				}
				default:{
					hidePopWindow();
				}
			}
		}

	};
}
