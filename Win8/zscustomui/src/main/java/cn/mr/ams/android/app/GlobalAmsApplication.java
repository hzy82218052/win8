package cn.mr.ams.android.app;

import android.app.Application;

/**
 * 提供应用的全局访问
 * @author wangchangsen
 * @date 2013-4-2 上午11:29:04
 */
public class GlobalAmsApplication extends Application {
	
	private int bottomBarHeight;//TabHost的选项卡的高度
	
	public int getBottomBarHeight() {
		return bottomBarHeight;
	}

	public void setBottomBarHeight(int bottomBarHeight) {
		this.bottomBarHeight = bottomBarHeight;
	}

	public void onCreate() {
//		setTheme(R.style.AppTheme);
		super.onCreate();
		
//		setTheme(R.style.AppTheme);
	}
}
