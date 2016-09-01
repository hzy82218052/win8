package win8.test.com.win8.ljp.ani;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import win8.test.com.win8.R;

public class TestRolateAnimActivity extends Activity {
    /** Called when the activity is first created. */
	MyImageView joke;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        joke=(MyImageView) findViewById(R.id.c_joke);
        joke.setOnClickIntent(new MyImageView.OnViewClick() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				Toast.makeText(TestRolateAnimActivity.this, "事件触发", Toast.LENGTH_SHORT).show();
				System.out.println("1");
			}
		});
    }
}