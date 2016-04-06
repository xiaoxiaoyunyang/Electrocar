package com.aiba.haimaelc;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class SysApp extends Application {

    private List<Activity> mActivityList = new LinkedList<>();
    private static SysApp sysApp;

    public static SysApp getInstance() {
        return sysApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sysApp = this;
    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void exit() {
        for (Activity activity : mActivityList) {
            if (activity != null)
                activity.finish();
        }
        System.exit(0);
    }
}
