package com.aiba.haimaelc.tools;

import android.content.Context;

import com.aiba.haimaelc.widget.WaitDialog;

import java.util.ArrayList;

public class ProgressManager {

    private static WaitDialog mProgress;
    private static ArrayList<WaitDialog> mProgressList = new ArrayList<>();

    private static WaitDialog createDialog(Context context, String message) {
        mProgress = new WaitDialog(context).builder().setMessage(message);
        return mProgress;
    }

    public static WaitDialog showProgress(Context context) {
        return showProgress(context, "加载中...");
    }

    public static WaitDialog showProgress(Context context, String message) {
        closeProgress();
        WaitDialog waitDialog = createDialog(context, message);
        try {
            waitDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        mProgressList.add(waitDialog);
        return waitDialog;
    }


    public static void changeProgressShowMessage(String message) {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.setMessage(message);
        }
    }

    public static boolean isProgressShowing() {
        return mProgress != null && mProgress.isShowing();
    }

    public static boolean closeProgress() {
        if (!mProgressList.isEmpty()) {
            for (WaitDialog dialog : mProgressList) {
                if (dialog.isShowing()) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mProgressList.clear();
        }
        return true;
    }
}
