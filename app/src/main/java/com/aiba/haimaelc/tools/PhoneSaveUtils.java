package com.aiba.haimaelc.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.SysApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhoneSaveUtils {

    private static SharedPreferences sp = SysApp.getInstance().getSharedPreferences(AppConst.PREFERENCE_SAVE, Context.MODE_PRIVATE);

    public static void putListData(String key, List<?> data) {
        SharedPreferences.Editor editor = sp.edit();
        String value = SceneList2String(data);
        editor.putString(key, value);
        editor.apply();
    }

    public static List getListData(String key) {
        List<?> list;
        String value = sp.getString(key, "");
//        LogUtils.logE("listdata ->" + value);
        list = String2SceneList(value);
        return list;
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static double getDouble(String key, double defaultValue) {
        return sp.getFloat(key, (float) defaultValue);
    }

    public static void putDouble(String key, double value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, (float) value);
        editor.apply();
    }

    public static void putMap(Map<String, Object> values) {
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = values.keySet();
        Iterator<String> iterator = set.iterator();
        Object object = null;
        String key = "";
        while (iterator.hasNext()) {
            key = iterator.next();
            object = values.get(key);
            if (object instanceof String) {
                editor.putString(key, (String) object);
                continue;
            }
            if (object instanceof Long) {
                editor.putLong(key, (long) object);
                continue;
            }
            if (object instanceof Boolean) {
                editor.putBoolean(key, (boolean) object);
                continue;
            }
            if (object instanceof Integer) {
                editor.putInt(key, (int) object);
                continue;
            }
        }
        editor.apply();
    }

    public static String SceneList2String(List<?> SceneList) {
        String SceneListString = "";
        if (SceneList == null || SceneList.size() == 0) {
            return SceneListString;
        }
        try {
            // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // 然后将得到的字符数据装载到ObjectOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(SceneList);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            SceneListString = new String(Base64.encode(
                    byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            // 关闭objectOutputStream
            objectOutputStream.close();
        } catch (IOException e) {
            return SceneListString;
        }
        return SceneListString;
    }

    //将字符串转换成list集合
    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString) {
        List<?> SceneList = new ArrayList();
        if (TextUtils.isEmpty(SceneListString)) {
            return SceneList;
        }
        try {
            byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            SceneList = (List<?>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            return SceneList;
        }
        return SceneList;
    }

}
