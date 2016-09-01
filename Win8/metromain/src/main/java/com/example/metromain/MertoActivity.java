/**   
 * Copyright © 2014 All rights reserved.
 * 
 * @Title: MertoActivity.java 
 * @Prject: MetroMain
 * @Package: com.example.metromain 
 * @Description: TODO
 * @author: raot raotao.bj@cabletech.com.cn 
 * @date: 2014年9月26日 上午10:44:21 
 * @version: V1.0   
 */
package com.example.metromain;

import java.util.ArrayList;

import com.example.metromain.MertoItemView.OnMertoItemViewListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

/**
 * @ClassName: MertoActivity
 * @Description: TODO
 * @author: raot raotao.bj@cabletech.com.cn
 * @date: 2014年9月26日 上午10:44:21
 */
public class MertoActivity extends Activity {

	private Vibrator vibrator;// 长按震动效果
	private boolean isMove = false;// 是否移动
	private Button addBt;
	private FrameLayout mertoContent;
	private LinearLayout itemLayout1, itemLayout2, itemLayout3, itemLayout4;
	private MertoItemView item0, item1, item2, item3, item4, item5, item6,
			item7, item8, item9, item10, item11, item12;
	private ArrayList<MertoBean> mertoBeans = new ArrayList<MertoBean>();
	private ArrayList<MertoBean> startBeans;// 保存itme变换前的内容
	private ArrayList<MertoItemView> mertoItemViews;
	private int tag = -1;// 要变换图标
	private int moveTag = -1;// 当前移动到的位置
	private MertoItemView positionView;// 当前要移动的布局
	private MertoItemView moveView;// 移动中的布局

	private OnMertoItemViewListener onMertoItemViewListener = new OnMertoItemViewListener() {

		@Override
		public void onClick(MertoItemView v) {
			// TODO Auto-generated method stub
			Toast.makeText(MertoActivity.this,
					mertoBeans.get((Integer) v.getTag()).getName(),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public boolean onMove(MertoItemView v, MotionEvent e1, MotionEvent e2) {
			// TODO Auto-generated method stub
			if (!isMove) {
				return false;
			}
			int[] moveLocation = new int[2];
			v.getLocationOnScreen(moveLocation);
			if (moveView == null) {
				tag = (Integer) v.getTag();
				startBeans = new ArrayList<MertoBean>(mertoBeans);
				moveView = new MertoItemView(MertoActivity.this);
				moveView.setIcon(v.getIcon());
				moveView.setText(v.getText());
				moveView.setTextColor(v.getTextColor());
				moveView.setTextSize(v.getTextSize());
				LayoutParams moveParams = new LayoutParams(
						v.getWidth(), v.getHeight());
				moveParams.setMargins((int) (e1.getRawX() - e1.getX()),
						(int) (e1.getRawY() - e1.getY()), 0, 0);
				moveParams.gravity = Gravity.TOP | Gravity.LEFT;
				moveView.setLayoutParams(moveParams);
				moveView.setBackgroundDrawable(v.getBackground());
				mertoContent.addView(moveView);
				moveView.getBackground().setAlpha(200);
			}
			setParams(
					(int) (e1.getRawX() - e1.getX() + e2.getRawX() - e1
							.getRawX()),
					(int) (e1.getRawY() - e1.getY() + e2.getRawY() - e1
							.getRawY()) - addBt.getHeight() - 40);
			changeData((int) e2.getRawX(), (int) e2.getRawY());
			return true;
		}

		@Override
		public void onLongClick(MertoItemView v) {
			// TODO Auto-generated method stub
			vibrator.vibrate(100);
			isMove = true;
		}

		@Override
		public void onUp(MertoItemView v) {
			// TODO Auto-generated method stub
			v.setVisibility(View.VISIBLE);
			if (isMove) {
				mertoContent.removeView(moveView);
				moveView = null;
				positionView.setVisibility(View.VISIBLE);
				tag = -1;
				moveTag = -1;
				setView();
				isMove = false;
			}
		}
	};

	private void setParams(int left, int top) {
		LayoutParams moveParams = (LayoutParams) moveView
				.getLayoutParams();
		moveParams.setMargins(left, top, 0, 0);
		moveView.setLayoutParams(moveParams);
	}

	private void changeData(int x, int y) {
		if (positionView != null) {
			positionView.setVisibility(View.VISIBLE);
		}
		changeTag(x, y);
		if (tag != -1 && moveTag != -1) {
			mertoBeans = new ArrayList<MertoBean>(startBeans);
			if (mertoBeans.size() < 5) {
				MertoBean mertoBean = new MertoBean(mertoBeans.get(tag));
				mertoBeans.set(tag, mertoBeans.get(moveTag));
				mertoBeans.set(moveTag, mertoBean);
			} else {
				MertoBean mertoBean = new MertoBean(mertoBeans.get(tag - 1));
				mertoBeans.set(tag - 1, mertoBeans.get(moveTag - 1));
				mertoBeans.set(moveTag - 1, mertoBean);
			}
			setView();
			positionView.setVisibility(View.INVISIBLE);
		}
	}

	private void changeTag(int x, int y) {
		moveTag = -1;
		for (int i = 0; i < mertoItemViews.size(); i++) {
			int[] location = new int[2];
			mertoItemViews.get(i).getLocationOnScreen(location);
			if (x > location[0]
					&& x < location[0] + mertoItemViews.get(i).getWidth()
					&& y > location[1]
					&& y < location[1] + mertoItemViews.get(i).getHeight()
					&& mertoItemViews.get(i).getVisibility() == View.VISIBLE) {
				moveTag = i;
				positionView = mertoItemViews.get(i);
				return;
			} else {
				moveTag = tag;
				positionView = mertoItemViews.get(tag);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.metro_activity);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		initView();
		setView();
	}

	private void initView() {
		addBt = (Button) findViewById(R.id.add_bt);
		mertoContent = (FrameLayout) findViewById(R.id.merto_content);
		itemLayout1 = (LinearLayout) findViewById(R.id.item_layout1);
		itemLayout2 = (LinearLayout) findViewById(R.id.item_layout2);
		itemLayout3 = (LinearLayout) findViewById(R.id.item_layout3);
		itemLayout4 = (LinearLayout) findViewById(R.id.item_layout4);
		item0 = (MertoItemView) findViewById(R.id.merto_0);
		item1 = (MertoItemView) findViewById(R.id.merto_1);
		item2 = (MertoItemView) findViewById(R.id.merto_2);
		item3 = (MertoItemView) findViewById(R.id.merto_3);
		item4 = (MertoItemView) findViewById(R.id.merto_4);
		item5 = (MertoItemView) findViewById(R.id.merto_5);
		item6 = (MertoItemView) findViewById(R.id.merto_6);
		item7 = (MertoItemView) findViewById(R.id.merto_7);
		item8 = (MertoItemView) findViewById(R.id.merto_8);
		item9 = (MertoItemView) findViewById(R.id.merto_9);
		item10 = (MertoItemView) findViewById(R.id.merto_10);
		item11 = (MertoItemView) findViewById(R.id.merto_11);
		item12 = (MertoItemView) findViewById(R.id.merto_12);
		item0.setTag(0);
		item1.setTag(1);
		item2.setTag(2);
		item3.setTag(3);
		item4.setTag(4);
		item5.setTag(5);
		item6.setTag(6);
		item7.setTag(7);
		item8.setTag(8);
		item9.setTag(9);
		item10.setTag(10);
		item11.setTag(11);
		item12.setTag(12);
		mertoItemViews = new ArrayList<MertoItemView>();
		mertoItemViews.add(item0);
		mertoItemViews.add(item1);
		mertoItemViews.add(item2);
		mertoItemViews.add(item3);
		mertoItemViews.add(item4);
		mertoItemViews.add(item5);
		mertoItemViews.add(item6);
		mertoItemViews.add(item7);
		mertoItemViews.add(item8);
		mertoItemViews.add(item9);
		mertoItemViews.add(item10);
		mertoItemViews.add(item11);
		mertoItemViews.add(item12);

		item0.setOnMertoItemViewListener(onMertoItemViewListener);
		item1.setOnMertoItemViewListener(onMertoItemViewListener);
		item2.setOnMertoItemViewListener(onMertoItemViewListener);
		item3.setOnMertoItemViewListener(onMertoItemViewListener);
		item4.setOnMertoItemViewListener(onMertoItemViewListener);
		item5.setOnMertoItemViewListener(onMertoItemViewListener);
		item6.setOnMertoItemViewListener(onMertoItemViewListener);
		item7.setOnMertoItemViewListener(onMertoItemViewListener);
		item8.setOnMertoItemViewListener(onMertoItemViewListener);
		item9.setOnMertoItemViewListener(onMertoItemViewListener);
		item10.setOnMertoItemViewListener(onMertoItemViewListener);
		item11.setOnMertoItemViewListener(onMertoItemViewListener);
		item12.setOnMertoItemViewListener(onMertoItemViewListener);
		addBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addData();
			}
		});
	}

	private void setView() {
		for (int i = 0; i < mertoItemViews.size(); i++) {
			if (mertoBeans.size() > i) {
				if (mertoBeans.size() < 5) {
					mertoItemViews.get(i).setVisibility(View.VISIBLE);
					mertoItemViews.get(i)
							.setIcon(mertoBeans.get(i).getIconId());
					mertoItemViews.get(i).setText(mertoBeans.get(i).getName());
				} else {
					mertoItemViews.get(i + 1).setVisibility(View.VISIBLE);
					mertoItemViews.get(i + 1).setVisibility(View.VISIBLE);
					mertoItemViews.get(i + 1).setIcon(
							mertoBeans.get(i).getIconId());
					mertoItemViews.get(i + 1).setText(
							mertoBeans.get(i).getName());
				}
			} else {
				if (mertoBeans.size() < 5) {
					mertoItemViews.get(i).setVisibility(View.GONE);
				} else {
					if (i < 12) {
						mertoItemViews.get(i + 1).setVisibility(View.GONE);
					}
				}
			}
		}
		if (mertoBeans.size() < 5) {
			item0.setVisibility(View.VISIBLE);
			itemLayout3.setVisibility(View.GONE);
			itemLayout4.setVisibility(View.GONE);
			if (mertoBeans.size() == 0) {
				item0.setVisibility(View.GONE);
			} else if (mertoBeans.size() == 2) {
				itemLayout1.setVisibility(View.GONE);
			} else {
				itemLayout1.setVisibility(View.VISIBLE);
				itemLayout2.setVisibility(View.GONE);
			}
			item7.setVisibility(View.INVISIBLE);
		} else {
			item0.setVisibility(View.GONE);
			itemLayout2.setVisibility(View.VISIBLE);
			itemLayout3.setVisibility(View.VISIBLE);
			itemLayout4.setVisibility(View.VISIBLE);
		}

	}

	private void addData() {
		MertoBean mertoBean = new MertoBean();
		if (mertoBeans.size() == 0) {
			mertoBean.setIconId(R.drawable.icon_mic);
			mertoBean.setName("语音拨测");
		} else if (mertoBeans.size() == 1) {
			mertoBean.setIconId(R.drawable.icon_ping);
			mertoBean.setName("Ping");
		} else if (mertoBeans.size() == 2) {
			mertoBean.setIconId(R.drawable.icon_network);
			mertoBean.setName("联网质量");
		} else if (mertoBeans.size() == 3) {
			mertoBean.setIconId(R.drawable.icon_signal);
			mertoBean.setName("WLAN信号强度");
		} else if (mertoBeans.size() == 4) {
			mertoBean.setIconId(R.drawable.icon_wlan);
			mertoBean.setName("WLAN认证");
		} else if (mertoBeans.size() == 5) {
			mertoBean.setIconId(R.drawable.icon_rssi);
			mertoBean.setName("RSSI信号强度");
		} else if (mertoBeans.size() == 6) {
			mertoBean.setIconId(R.drawable.icon_upload);
			mertoBean.setName("上传宽带");
		} else if (mertoBeans.size() == 7) {
			mertoBean.setIconId(R.drawable.icon_wlan);
			mertoBean.setName("WLAN认证");
		} else if (mertoBeans.size() == 8) {
			mertoBean.setIconId(R.drawable.icon_rssi);
			mertoBean.setName("RSSI信号强度");
		} else if (mertoBeans.size() == 9) {
			mertoBean.setIconId(R.drawable.icon_download);
			mertoBean.setName("下载宽带");
		} else if (mertoBeans.size() == 10) {
			mertoBean.setIconId(R.drawable.icon_signal);
			mertoBean.setName("WLAN信号强度");
		} else if (mertoBeans.size() == 11) {
			mertoBean.setIconId(R.drawable.icon_download);
			mertoBean.setName("下载宽带");
		} else {
			return;
		}
		mertoBeans.add(mertoBean);
		setView();
	}

}
