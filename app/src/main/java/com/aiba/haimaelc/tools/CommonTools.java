package com.aiba.haimaelc.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonTools {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int pxToDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }

    /**
     * 根据listview的item的总高度设置listview的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = getListViewChildrenHeight(listView);
        listView.setLayoutParams(params);
    }

    public static int getListViewChildrenHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 获得haima/media的文件夹
     *
     * @return File
     */
    public static File getMediaSdCacheDir() {
        return getSdCacheDir("media");
    }

    public static File getSdCacheDir(String dirName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File haimaDir = new File(externalStorageDirectory, "haima_elc");
            if (!haimaDir.exists()) {
                if (!haimaDir.mkdir()) {
                    return null;
                }
            }
            File dir = new File(haimaDir, dirName);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    return null;
                }
            }
            return dir;
        } else {
            return null;
        }
    }

    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String Upper(String str) {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String Lower(String str) {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static void doUmengUpdate(boolean autoUpdate, Context context, UmengUpdateListener update) {
        // 日志输出，发布时关闭
        UpdateConfig.setDebug(false);
//        UmengUpdateAgent.setDefault();//恢复默认设置
        /**
         * 更新内容默认弹出框提示
         */
        UmengUpdateAgent.setUpdateAutoPopup(false);
        // 所有网络下都提示更新(WIFI和流量)
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 全量更新 模式增量更新
        UmengUpdateAgent.setDeltaUpdate(false);
        /**
         * 使用通知栏高极样式 带有暂停/取消按钮
         * targetSdk >=14 才有效果
         */
        UmengUpdateAgent.setRichNotification(true);
        // 监听检测更新事件
        UmengUpdateAgent.setUpdateListener(update);
        /**
         * 更新提示样式
         * UpdateStatus.STYLE_DIALOG(默认)使用对话框更新提示
         * UpdateStatus.STYLE_NOTIFICATION 使用通知栏更新提示
         */
        UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
        if (autoUpdate) {
            UmengUpdateAgent.update(context);
        } else {
            // 手动检测更新 强制更新
            UmengUpdateAgent.forceUpdate(context);
        }
    }

    public static void setDrawableLeft(Context context, TextView view, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    public static void setDrawableTop(Context context, TextView view, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
    }

    public static void setDrawableRight(Context context, TextView view, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }

    public static void setDrawableBottom(Context context, TextView view, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, drawable);
    }

    public static boolean isSameLocation(Location l1, Location l2) {
        return (l1 != null && l2 != null) &&
                (l1.getLatitude() == l2.getLatitude() && l2.getLongitude() == l2.getLongitude());
    }
}
