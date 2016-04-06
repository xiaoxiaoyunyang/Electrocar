package com.aiba.haimaelc.http;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.aiba.haimaelc.BuildConfig;
import com.aiba.haimaelc.SysApp;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.NetworkUtil;
import com.aiba.haimaelc.tools.ProgressManager;
import com.aiba.haimaelc.tools.ToastException;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;

public class ApiRequest extends AsyncTask<Object, Void, Boolean> {

    private final static int DEFAULT_SOCKET_TIMEOUT = 30000;
    private final static String KEY = "hmxnyno1";
    public final static String client_id = "haima_android";
    private String ERROR;
    private boolean mShowDialog = false;
    private String mShowMsg;
    private Context context;
    private Gson gson;
    private String apiUrl;
    private String method;
    private LinkedHashMap<String, String> values;
    private ReturnCallBack callBack;
    private String return_data = "";
    private int return_code = 0;
    private int from = 0;
    private String deviceId;
    public static String access_token;

    public ApiRequest(Context context, String apiUrl, String method, LinkedHashMap<String, String> values) {
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.values = values;
        gson = new Gson();
    }

    public ApiRequest(Context context, String apiUrl, String method, LinkedHashMap<String, String> values, int from) {
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.values = values;
        this.from = from;
        gson = new Gson();
    }

    private String doConnect() throws ToastException {
        if (!NetworkUtil.isNetWorkEnable()) {
            throw new ToastException("请打开网络连接");
        }
        String responseResult = "";
        if (access_token != null) {
            values.put("access_token", access_token);
        } else {
            values.put("client_id", client_id);
            values.put("device_id", getDeviceId());
        }
        values.put("app_version", BuildConfig.VERSION_NAME);
        values.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        values.put("secret", generateSignature(values.get("timestamp")));
        LogUtils.logE(values.get("secret") + "");
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            StringBuilder content = new StringBuilder();
            for (String mapKey : values.keySet()) {
                content.append(
                        URLEncoder.encode(mapKey, "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(String.valueOf(values.get(mapKey)), "UTF-8")).append("&");
            }
            if (content.length() > 0) {
                content.deleteCharAt(content.length() - 1);
            }
            URL url;
            if ("GET".equals(method) || "DELETE".equals(method)) {
                url = new URL(apiUrl + "?" + content);
            } else {
                url = new URL(apiUrl);
            }
            LogUtils.logE(apiUrl);
            LogUtils.logE(method);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(DEFAULT_SOCKET_TIMEOUT); // 设置连接超时
            conn.setReadTimeout(DEFAULT_SOCKET_TIMEOUT);
            conn.setRequestMethod(method); // 设定请求方式
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if ("GET".equals(method) || "DELETE".equals(method)) {
                conn.setDoOutput(false);
                conn.connect();
            } else {
                conn.setDoOutput(true);
                conn.connect();
                out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(content.toString());
                out.flush();
                out.close();
            }
            int a = conn.getResponseCode();
            LogUtils.logE("返回码" + a);
            if (a == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseResult += line;
                }
            } else {
                throw new ToastException("HTTP错误码：" + a);
            }
        } catch (ToastException e1) {
            throw e1;
        } catch (SocketTimeoutException e2) {
            throw new ToastException("HTTP请求超时");
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new ToastException("HTTP请求错误");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        JSONObject return_content;
        try {
            if (responseResult.contains("{") && !responseResult.startsWith("{")) {
                responseResult = responseResult.substring(responseResult.indexOf("{"));
            }
            LogUtils.logE("responseResult =>" + responseResult);
            return_content = new JSONObject(responseResult);
            String return_msg = return_content.optString("msg");
            return_code = return_content.getInt("code");
            return_data = return_content.optString("data");
            if (return_code != 0) {
                throw new ToastException(return_msg);
            }
        } catch (JSONException e) {
            throw new ToastException("返回值格式错误");
        }
        return responseResult;
    }

    public static String generateSignature(String timestamp) {
        return MD5(timestamp + KEY);
    }

//    private static String getData(LinkedHashMap<String, String> pairs) {
//        StringBuilder sb = new StringBuilder();
//        Object[] allkey = pairs.keySet().toArray();
//        Arrays.sort(allkey);//传参排序
//        for (Object mapKey : allkey) {
//            sb.append(pairs.get(mapKey));
//        }
//        return sb.toString();
//    }

    public static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] bytes = mdInst.digest();
            StringBuilder hash = new StringBuilder();
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xFF & aByte);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            return hash.toString().toUpperCase(Locale.ENGLISH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return s;
        }
    }

    public ApiRequest showProgress(boolean showProgress) {
        this.mShowDialog = showProgress;
        return this;
    }

    public ApiRequest showProgress(boolean showProgress, String message) {
        this.mShowDialog = showProgress;
        this.mShowMsg = message;
        return this;
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase(Locale.ENGLISH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mShowDialog) {
            if (TextUtils.isEmpty(mShowMsg)) {
                ProgressManager.showProgress(context);
            } else {
                ProgressManager.showProgress(context, mShowMsg);
            }
        }
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        try {
            doConnect();
        } catch (ToastException e) {
            e.printStackTrace();
            ERROR = e.getMessage();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            try {
                if (!TextUtils.isEmpty(return_data)) {
                    LogUtils.logE(return_data);
                    if (callBack.getmType() == String.class) {
                        callBack.onSuccess(from, return_data);
                    } else {
                        Object objects = gson.fromJson(return_data, callBack.getmType());
                        callBack.onSuccess(from, objects);
                    }
                } else {
                    callBack.onSuccess(from, null);
                }
            } catch (JsonParseException e) {//Json解析的错误
                callBack.onError(0, from, "数据异常");
            } catch (Exception e) {
                callBack.onError(1, from, "未知错误");
                e.printStackTrace();
            }
        } else {
            callBack.onError(return_code, from, ERROR);
        }
        if (mShowDialog) {
            ProgressManager.closeProgress();
        }
    }

    public void callApi(ReturnCallBack callBack) {
        this.callBack = callBack;
        this.executeOnExecutor(THREAD_POOL_EXECUTOR);
    }

    private String getDeviceId() {
        if (deviceId != null)
            return deviceId;
        try {
            TelephonyManager tm = (TelephonyManager) SysApp.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        } catch (Exception e) {
            Random random = new Random(System.currentTimeMillis());
            deviceId = "random" + System.currentTimeMillis()
                    + random.nextLong();
        }
        if (deviceId == null) {
            Random random = new Random(System.currentTimeMillis());
            deviceId = "random" + System.currentTimeMillis()
                    + random.nextLong();
        }
        return deviceId;
    }
}