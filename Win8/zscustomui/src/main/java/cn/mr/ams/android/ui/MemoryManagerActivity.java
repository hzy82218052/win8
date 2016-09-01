package cn.mr.ams.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.mr.ams.android.R;
import cn.mr.ams.android.app.BaseAmsActivity;

public class MemoryManagerActivity extends BaseAmsActivity implements android.view.View.OnClickListener{
	/**
	 * 显示程序列表的listView
	 */
	private ListView mListView;
	/**
	 * listView的适配器
	 */
	private AppMemoryListAdapter appAdapter;
	/**
	 * 顶栏
	 */
	private View title;
	private ImageButton titleBack, titleRefresh;
	private TextView titleName;
	/**
	 * 底栏
	 */
	private TextView freeMemory;
	private ActivityManager activityManager;
	/**
	 * 存放每个正在运行的程序的进程的信息
	 */
	private List<RunningAppProcessInfo> listProcess;
	/**
	 * 存放每个正在运行的程序的名称
	 */
	private List<String> listName;
	/**
	 * 存放每个正在运行的程勋的进程ID
	 */
	private int[] pIds;
	/**
	 * 存放每个正在运行的程序的进程的内存占用详情
	 */
	private MemoryInfo[] pMemoryInfos;
	private List<AppMemory> appList;
	/**
	 * 获取程序信息的帮助类
	 */
	private TaskInfo taskInfo;
	private MemoryHandler mHandler;
	private MemoryThread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_manager);
		taskInfo = new TaskInfo(this);
		mHandler = new MemoryHandler();
		mThread = new MemoryThread();
		initView();
		initListener();
		mThread.start();
	}
	private void initData() {
		// TODO Auto-generated method stub
		if(activityManager == null){
			activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		}
		if(listProcess != null){
			listProcess.clear();
			listProcess = null;
		}
		listProcess = activityManager.getRunningAppProcesses();//获取设备中所有运行的进程列表
		if(pIds != null){
			pIds = null;;
		}
		pIds = getAllProcessId(listProcess);//更具进程列表获得进程的id数组pIds
		if(listName != null){
			listName.clear();
			listName = null;
		}
		listName = getAllProcessName(listProcess);
		if(pMemoryInfos != null){
			pMemoryInfos = null;
		}
		pMemoryInfos = activityManager.getProcessMemoryInfo(pIds);//根据进程的pIds数组获取进程列表中的各进程的内存信息对象数组pMemoryInfos
		if(appList != null){
			appList.clear();
			appList = null;
		}
		appList = new ArrayList<AppMemory>();
		AppMemory app = null;
		for(int i = 0; i < pIds.length; i++){
			app = new AppMemory(pIds[i], listName.get(i), listProcess.get(i).processName,
					taskInfo.checkAppType(listProcess.get(i).processName), pMemoryInfos[i]);
			appList.add(app);
		}


	}
	private void initView(){
		title = (View) findViewById(R.id.title_memory);
		titleBack = (ImageButton) title.findViewById(R.id.title_left_bt);
		titleRefresh = (ImageButton) title.findViewById(R.id.title_right_bt);
		titleName = (TextView) title.findViewById(R.id.title_text);
		titleName.setText("内存管理");

		mListView = (ListView)findViewById(R.id.lv_memory_manager_app);

		freeMemory = (TextView)findViewById(R.id.free_memory);

	}
	private void initListener(){
		titleBack.setOnClickListener(this);
		titleRefresh.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final AppMemory appMemory = appList.get(position);
				AlertDialog.Builder builder =new AlertDialog.Builder(MemoryManagerActivity.this) ;
				if(appMemory.isSystemApp()){
					builder.setTitle(appMemory.getName() + "是系统程序，彻底关闭可能造成系统不稳定？") ;
					builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ActivityManager am = (ActivityManager) MemoryManagerActivity.this.getSystemService(Service.ACTIVITY_SERVICE);
							am.killBackgroundProcesses(appMemory.getPackageName());
							mThread = new MemoryThread();
							mThread.start();
						}

					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel() ;  // 取消显示对话框
						}

					});
				}else{
					builder.setTitle("确定要彻底关闭程序" + appMemory.getName() + " 吗？") ;
					builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ActivityManager am = (ActivityManager) MemoryManagerActivity.this.getSystemService(Service.ACTIVITY_SERVICE);
							am.killBackgroundProcesses(appMemory.getPackageName());
							mThread = new MemoryThread();
							mThread.start();
						}

					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel() ;  // 取消显示对话框
						}
					});
				}
				builder.create().show() ;
			}
		});
	}
	private void refreshView(){
		if(appAdapter == null){
			appAdapter = new AppMemoryListAdapter(MemoryManagerActivity.this, appList);
		}
		appAdapter.refreshData(appList);
		mListView.setAdapter(appAdapter);
		appAdapter.notifyDataSetChanged();

		displayBriefMemory();
	}

	private int[] getAllProcessId(List<RunningAppProcessInfo> processes){
		int[] ids = new int[processes.size()];
		for(int i = 0; i < processes.size(); i++){
			ids[i] = processes.get(i).pid;
		}
		return ids;
	}

	/**
	 * 获取所有进程的软件名称
	 * @author zhangshuo
	 * @date 2013-8-19 上午10:13:32
	 * @version
	 *@param listProcess
	 *@return
	 */
	private List<String> getAllProcessName(List<RunningAppProcessInfo> listProcess){
		List<String> listName = new ArrayList<String>();
		for(int i = 0; i < listProcess.size(); i++){
			String name = taskInfo.getAppName(listProcess.get(i).processName) + taskInfo.getAppVersion(listProcess.get(i).processName);
			listName.add(name);
		}

		return listName;
	}

	/**
	 * 获取系统的内存信息
	 * @author zhangshuo
	 * @date 2013-8-15 上午11:08:35
	 * @version
	 */
	private void displayBriefMemory() {
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		freeMemory.setText(Math.round((info.availMem/1024/1024f) * 100)/100f + "MB");
		Log.i("Tag","系统剩余内存:"+(info.availMem >> 10)+"k");
		Log.i("Tag","系统是否处于低内存运行："+info.lowMemory);
		Log.i("Tag","当系统剩余内存低于"+info.threshold+"时就看成低内存运行");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memory_manager, menu);
		return true;
	}

	class MemoryThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			initData();
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			mHandler.sendMessage(msg);
		}

	}
	class MemoryHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 1){
				refreshView();
			}else{
				Toast.makeText(MemoryManagerActivity.this, "获悉进程信息失败！", Toast.LENGTH_SHORT).show();
			}
		}

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.title_left_bt:{
				MemoryManagerActivity.this.finish();
				break;
			}
			case R.id.title_right_bt:{
				mThread = new MemoryThread();
				mThread.start();
				break;
			}
		}
	}

}
