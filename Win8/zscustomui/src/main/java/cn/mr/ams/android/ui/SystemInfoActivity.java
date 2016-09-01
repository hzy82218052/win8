package cn.mr.ams.android.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import cn.mr.ams.android.R;
import cn.mr.ams.android.ui.SubActionBar.OnSubActionClickListener;

public class SystemInfoActivity extends Activity {

    protected SubActionBar subTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        subTitle = (SubActionBar)findViewById(R.id.action_bar_subtitle);
        subTitle.setLeftText("哈哈");
        subTitle.setCenterText("嘎嘎");
        subTitle.setRightText("呵呵");
        subTitle.setLeftStrs(Arrays.asList("日", "月", "神", "教"));
        subTitle.setCenterStrs(Arrays.asList("九", "阴", "真", "经"));
        subTitle.setRightStrs(Arrays.asList("葵", "花", "宝", "典"));
        subTitle.setOnSubActionClickListener(new OnSubActionClickListener() {

            @Override
            public void onSubActionClick(String tag) {
                // TODO Auto-generated method stub
                Toast.makeText(SystemInfoActivity.this, tag, Toast.LENGTH_SHORT).show();
            }
        });
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
