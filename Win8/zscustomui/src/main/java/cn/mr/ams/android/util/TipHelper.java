package cn.mr.ams.android.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;
/**
 * 处理手机震动的工具类
 * @author zhangshuo
 *
 */
public class TipHelper {
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

}
