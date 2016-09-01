package cn.mr.ams.android.ui;

import cn.mr.ams.android.R;
import cn.mr.ams.android.R.layout;
import cn.mr.ams.android.R.menu;
import cn.mr.ams.android.app.BaseAmsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 登录界面
 * @author wangchangsen
 * @date 2013-4-2 上午11:31:22
 */
public class LoginActivity extends BaseAmsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
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
}
