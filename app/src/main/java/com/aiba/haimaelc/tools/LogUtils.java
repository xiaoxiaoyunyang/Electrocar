package com.aiba.haimaelc.tools;

import android.util.Log;

import com.aiba.haimaelc.BuildConfig;

public class LogUtils {
	public final static boolean DEBUG = BuildConfig.DEBUG;

	private final static String TAG = "haimaelc_log";

	public static void logV(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}
	public static void logD(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}
	public static void logI(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}
	public static void logW(String tag, String msg) {
		if (DEBUG) {
			Log.w(tag, msg);
		}
	}
	public static void logE(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void logV(String msg) {
		if (DEBUG) {
			Log.v(TAG, msg);
		}
	}
	public static void logD(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}
	public static void logI(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
	}
	public static void logW(String msg) {
		if (DEBUG) {
			Log.w(TAG, msg);
		}
	}
	public static void logE(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}
}
