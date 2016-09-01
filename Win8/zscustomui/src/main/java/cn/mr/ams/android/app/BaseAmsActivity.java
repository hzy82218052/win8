package cn.mr.ams.android.app;

import android.os.Bundle;
import android.widget.Toast;
import cn.mr.ams.android.R;

/**
 * 应用的基本界面
 * @author wangchangsen
 * @date 2013-4-2 上午11:28:36
 */
public class BaseAmsActivity extends ActionBarActivity {
	
	protected void onCreate(Bundle bundle) {
//		setTheme(R.style.AppTheme);
		boolean isThemeChange = getSharedPreferences("system_config", MODE_PRIVATE)
				.getBoolean("theme_value", true);
		if (isThemeChange) {
			setTheme(R.style.Theme_RealWorld);
		} else {
			setTheme(R.style.AppTheme);
		}
		
		super.onCreate(bundle);
	}

}
