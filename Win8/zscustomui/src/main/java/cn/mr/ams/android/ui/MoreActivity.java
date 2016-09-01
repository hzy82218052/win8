package cn.mr.ams.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import cn.mr.ams.android.R;

public class MoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
    }

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
}
