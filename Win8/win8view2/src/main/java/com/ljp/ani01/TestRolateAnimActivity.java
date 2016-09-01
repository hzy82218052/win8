package com.ljp.ani01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class TestRolateAnimActivity extends Activity
{
	MyImageView joke;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		joke = (MyImageView) findViewById(R.id.c_joke);
		joke.setOnClickIntent(new MyImageView.OnViewClickListener()
		{

			@Override
			public void onViewClick(MyImageView view)
			{
				Toast.makeText(TestRolateAnimActivity.this, "Joke", 1000).show();
			}
		});
	}
	
	
}