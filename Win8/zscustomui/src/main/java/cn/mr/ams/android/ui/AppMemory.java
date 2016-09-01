package cn.mr.ams.android.ui;

import android.os.Debug.MemoryInfo;

/**
 * 每个程序的实体对象
 *@author zhangshuo
 *@date 2013-8-16 下午4:37:54
 *@version
 * description:
 */
public class AppMemory {

	private int id;
	private String name;
	private String packageName;
	private boolean isSystemApp;
	private MemoryInfo memoryInfo;
	
	public AppMemory(int id, String name, String pName, boolean type, MemoryInfo info){
		this.id = id;
		this.name = name;
		this.packageName = pName;
		this.isSystemApp = type;
		this.memoryInfo = info;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	public MemoryInfo getMemoryInfo() {
		return memoryInfo;
	}

	public void setMemoryInfo(MemoryInfo memoryInfo) {
		this.memoryInfo = memoryInfo;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	
	
	
}
