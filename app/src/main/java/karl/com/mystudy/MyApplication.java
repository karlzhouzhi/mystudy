package karl.com.mystudy;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;

import karl.com.mystudy.testannotation.MyUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName(getApplicationContext());
        int pid = Process.myPid();
        int uid  = Process.myUid();
        Log.w("superkkone", "instance: " + this + ", processName: " + processName +",\n pid: " + pid + ", uid: " + uid);

    }

    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
