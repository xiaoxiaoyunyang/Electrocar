package com.aiba.haimaelc.tools;

import android.view.Gravity;
import android.widget.Toast;

import com.aiba.haimaelc.SysApp;

public class ToastUtil {

    public static void makeText(String msg) {
        Toast toast = Toast.makeText(SysApp.getInstance(), msg + "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void makeLongText(String msg) {
        Toast toast = Toast.makeText(SysApp.getInstance(), msg + "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void makeText(int resid) {
        Toast toast = Toast.makeText(SysApp.getInstance(), resid, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
