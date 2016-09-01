package cn.mr.ams.android.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * 获取程序的各种信息的工具类
 *@author zhangshuo
 *@date 2013-8-16 下午5:39:50
 *@version
 * description:
 */
public class TaskInfo {
    Context context ;
    PackageManager pm ;
    public TaskInfo(Context context) {
        this.context = context;
        pm = context.getPackageManager();
    }

    /**
     * 根据包名判断程序是否是系统程序
     * 是系统程序返回true，用户程序返回false
     * @author zhangshuo
     * @date 2013-8-19 下午3:33:38
     * @version
     *@param packName
     *@return
     */
    public boolean checkAppType(String packName){
        boolean flag = true;
        ApplicationInfo info;
        try {
            info = pm.getApplicationInfo(packName, 0);
            if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
                flag = false;
            }else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                flag = false;
            }
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    /*
     * 根据包名 查询 图标
     */
    public Drawable getAppIcon(String packname){
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public Drawable getAppIcon2(String packName){
        System.out.println("什么情况Start？");
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        System.out.println("什么情况？");
        for (ResolveInfo reInfo : resolveInfos) {
            System.out.println("包名对比：reInfo packName:" + reInfo.activityInfo.packageName + " packName: " + packName);
            if(reInfo.activityInfo.packageName == packName){
                System.out.println("获得图片： " + reInfo.loadIcon(pm));
                return reInfo.loadIcon(pm);
            }
        }
        return null;
    }

    /*
     *获取程序的版本号
     */
    public String getAppVersion(String packname){

        try {
            PackageInfo packinfo =    pm.getPackageInfo(packname, 0);
            if(null == packinfo.versionName){
                return "";
            }else{
                return packinfo.versionName;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /*
     * 获取程序的名字
     */
    public String getAppName(String packname){
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            if(null == info.loadLabel(pm).toString()){
                return packname;
            }else{
                return info.loadLabel(pm).toString();
            }
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return packname;
        }
    }
    /*
     * 获取程序的权限
     */
    public String[] getAppPremission(String packname){
        try {
            PackageInfo packinfo =    pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /*
     * 获取程序的签名
     */
    public String getAppSignature(String packname){
        try {
            PackageInfo packinfo =    pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取到所有的权限
            return packinfo.signatures[0].toCharsString();

        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}  
