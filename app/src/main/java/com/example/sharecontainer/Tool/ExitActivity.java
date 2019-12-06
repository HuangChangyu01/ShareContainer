package com.example.sharecontainer.Tool;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class ExitActivity extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static ExitActivity instance;

    private ExitActivity() {
    }
    // synchronized修饰有 只有一个线程可以访问 其他等等
    public synchronized static ExitActivity getInstance() {
        if (null == instance) {
            instance = new ExitActivity();
        }
        return instance;
    }

    /**
     * 将 界面Activity 加入arraylist列表容器中
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void removeActivity(Activity activity){
        mList.remove(activity);
    }

    /**
     * 关闭列表中所有的Activity
     */
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
