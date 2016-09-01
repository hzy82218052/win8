package cn.mr.ams.android.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import cn.mr.ams.android.R;
import cn.mr.ams.android.app.GlobalAmsApplication;

/**
 *@author zhangshuo
 *@date 2013-4-25 上午10:20:27
 *@version
 * description:
 */
public class PopupWindowHelper {

	/**
	 * 初始化PopWindow内部需要显示的布局View(有问题，在调用window.dismiss()的时候会报错)
	 * @author zhangshuo
	 * @date 2013-4-25 上午11:23:24
	 * @version
	 *@param context
	 *@param listStr	需要被ListView显示的list
	 *@param window		显示此布局View的PopWindow
	 *@param tv			当ListView的item被点击，需要动态改变显示数据的TextView
	 *@return			返回初始化完成的布局View
	 */
//	public static View initPopView(final Context context, final List<String> listStr, final PopupWindow window, final TextView tv){
//
//		Log.v("initPopView>>>>>>>>>>>>>>>>", "没反应？！");
//
//		View popView = View.inflate(context, R.layout.pop_list, null);
//
//		ListView popList = (ListView)popView.findViewById(R.id.pop_list);
//		PopListAdapter plAdapter = new PopListAdapter(context, listStr);
//		popList.setVerticalScrollBarEnabled(false);// ListView去掉下拉条
//		popList.setAdapter(plAdapter);
//		popList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				PopListAdapter adapter = new PopListAdapter(context, listStr);
//				tv.setText(adapter.getItem(arg2));
//				window.dismiss();
//			}
//		});
//		return popView;
//	}

	/**
	 * 初始化PopWindow的状态
	 * @author zhangshuo
	 * @date 2013-4-25 上午11:20:40
	 * @version
	 *@param context
	 *@param popView 	PopWindow内部显示的View
	 *@param v			初始化PopWindow高和宽的参考控件V
	 *@return			返回初始化完成的PopWindow
	 */
	public static PopupWindow makePopWindow(Context context, View popView, View v){

		Log.v("makePopWindow>>>>>>>>>>>>>>>>", "没反应？！");

		final PopupWindow window;

		window = new PopupWindow(context);

		window.setContentView(popView);

		window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_spinner_bg));
		window.setWidth(v.getWidth());
		window.setHeight(v.getHeight()*9);

		// 设置PopupWindow外部区域是否可触摸
		window.setFocusable(true); //设置PopupWindow可获得焦点
		window.setTouchable(true); //设置PopupWindow可触摸
		window.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		return window;
	}
	/**
	 * 初始化大小无参照View的PopWindow的状态
	 * @author zhangshuo
	 * @date 2013-4-25 上午11:20:40
	 * @version
	 *@param context
	 *@param popView 	PopWindow内部显示的View
	 *@return			返回初始化完成的PopWindow
	 */
	public static PopupWindow makePopWindow(Context context, View popView){

		Log.v("makePopWindow>>>>>>>>>>>>>>>>", "没反应？！");

		final PopupWindow window;

		window = new PopupWindow(popView);

		window.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_subtitle_transparent));
		window.setWidth(180);
		window.setHeight(310);

		// 设置PopupWindow外部区域是否可触摸
		window.setFocusable(true); //设置PopupWindow可获得焦点
		window.setTouchable(true); //设置PopupWindow可触摸
		window.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		return window;
	}

	/**
	 * 将PopWindow显示出来
	 * @author zhangshuo
	 * @date 2013-4-25 上午11:17:53
	 * @version
	 *@param window 需要被弹出的PopWindow
	 *@param v PopWindow弹出时的参考控件即锚点
	 */
	public static void showPopWindow(Context context, int displayHeight, PopupWindow window, View v){

		/**
		 * 获得TabActivity的底栏的高度（在Application中保存）
		 */
		GlobalAmsApplication application = (GlobalAmsApplication) context.getApplicationContext();
		int bottomBarHeight = application.getBottomBarHeight();

		/**
		 * 去的锚点v的左上角的坐标，并存放到数组xy[]中
		 */
		int[] xy = new int[2];
		v.getLocationOnScreen(xy);

		int height = window.getHeight() + xy[1];
		System.out.println("window.getHeight():" + window.getHeight());
		System.out.println("height:" + height + "xy[1]:" + xy[1]);
		int activityHeight = displayHeight - bottomBarHeight;
		System.out.println("activityHeight::::" + activityHeight + "::::::bottomBarHeight::::::"+bottomBarHeight);
		if(height >= activityHeight){
			window.setAnimationStyle(R.style.pop_anim_in_down_to_up);
		}else{
			window.setAnimationStyle(R.style.pop_anim_in_up_to_down);
		}

		window.showAsDropDown(v);
	}
	/**
	 * 隐藏PopWindow
	 * @author zhangshuo
	 * @date 2013-4-25 下午2:07:25
	 * @version
	 *@param window
	 */
	public static void hidePopWindow(PopupWindow window){
		if(null != window){
			window.dismiss();
			window = null;
		}
	}

}
