package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;

import java.lang.reflect.Field;

public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private ProgressBar mProgress;
    private String type;
    private String url;
    private int percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        type = intent.getStringExtra(AppConst.CONTENT_TYPE);
        if (TextUtils.isEmpty(type)) {
            finish();
            return;
        }
        if (AppConst.SURVEY_DETAIL.equals(type)) {
            url = intent.getStringExtra(AppConst.URL);
        }
        initView();
    }

    private void initView() {
        setTitleBarBack();
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                percent = 0;
                mProgress.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                handler.sendEmptyMessageDelayed(0, 300 / 10);
                return true;
            }
        });
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mProgress = (ProgressBar) findViewById(R.id.web_progress);
        mProgress.setMax(100);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebChromeClient(new WebViewClient());
        switch (type) {
            case AppConst.DRIVE_HABIT:
                setTitleText("驾驶习惯");
                mWebView.loadUrl("http://scrm.ihaima.com/sync/car/drivingskills");
                break;
            case AppConst.MAINTAIN_HELP:
                setTitleText("保养说明");
                mWebView.loadUrl("http://scrm.ihaima.com/sync/car/m6");
                break;
            case AppConst.APP_HELP:
                setTitleText("使用帮助");
                mWebView.loadUrl("http://scrm.ihaima.com/sync/car/help");
                break;
            case AppConst.SURVEY_DETAIL:
                setTitleText("调查问卷");
                if (url != null) {
                    mWebView.loadUrl(url);
                }
                break;
            case AppConst.OFFICIAL_WEBSITE:
                setTitleText("官方网站");
                mWebView.loadUrl("http://www.ihaima.com/");
                break;
            case AppConst.AGREEMENT:
                setTitleText("使用协议");
                mWebView.loadUrl("http://scrm.ihaima.com/sync/car/htmlpage2");
                break;
        }
        handler.sendEmptyMessageDelayed(0, 300 / 10);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                mProgress.setVisibility(View.GONE);
            } else {
                percent++;
                if (percent < 25) {
                    mProgress.setProgress(percent * 4);
                    handler.sendEmptyMessageDelayed(0, 500 / 25);
                }
            }
        }
    };

    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                handler.sendEmptyMessageDelayed(newProgress, 500 - percent * 20);
            }
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //http://www.tuicool.com/articles/QbqURr
        mWebView.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);//进入后台主动释放JS
        releaseAllWebViewCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true); // 允许加载javascript
    }

    /**
     * 解决WebView内存泄漏的Bug
     */
    public void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
