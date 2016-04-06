package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.BuildConfig;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysApp;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.tools.BitmapTools;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.ProgressManager;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.CustomerAlertDialog;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("设置");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("退出当前帐号");
        ImageView icon = (ImageView) findViewById(R.id.icon);
        icon.setOnClickListener(this);
        ((TextView) findViewById(R.id.version)).setText(String.format("版本 %s", BuildConfig.VERSION_NAME));
        findViewById(R.id.official_website).setOnClickListener(this);
        findViewById(R.id.contact_us).setOnClickListener(this);
        findViewById(R.id.grade).setOnClickListener(this);
        findViewById(R.id.agreement).setOnClickListener(this);
        findViewById(R.id.check_update).setOnClickListener(this);
        findViewById(R.id.app_help).setOnClickListener(this);
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
        if (bd != null) {
            Bitmap bitmap = bd.getBitmap();
            if (bitmap != null) {
                icon.setImageBitmap(BitmapTools.getCornerBitmap(bitmap));
            }
        }
    }

    private long[] mHits = new long[7];//存储时间的数组

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_submit:
                intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.putExtra(AppConst.LOGOUT, true);
                startActivity(intent);
                SysApp.getInstance().exit();
                break;
            case R.id.icon:
                //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
                    ToastUtil.makeText("1234567");
                }
                break;
            case R.id.official_website:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(AppConst.CONTENT_TYPE, AppConst.OFFICIAL_WEBSITE);
                startActivity(intent);
                break;
            case R.id.contact_us:
                // TODO: 2016/3/24 联系我们
                break;
            case R.id.grade:
                intent = new Intent("android.intent.action.VIEW",
                        Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
                break;
            case R.id.agreement:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(AppConst.CONTENT_TYPE, AppConst.AGREEMENT);
                startActivity(intent);
                break;
            case R.id.app_help:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(AppConst.CONTENT_TYPE, AppConst.APP_HELP);
                startActivity(intent);
                break;
            case R.id.check_update:
                ProgressManager.showProgress(this, "新版本检测中...");
                UmengUpdateListener update = new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int code, final UpdateResponse response) {
                        ProgressManager.closeProgress();
                        switch (code) {
                            case UpdateStatus.Yes: // has update
                                new CustomerAlertDialog(SettingActivity.this).builder()
                                        .setCancelable(false)
                                        .setTitle("有可用的新版本")
                                        .setMsg(response.updateLog)
                                        .setMsgLeft()
                                        .setNegativeButton(SysModel.isForceUpdate ? "退出程序" : "暂不更新", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (SysModel.isForceUpdate) {

                                                }
                                            }
                                        })
                                        .setPositiveButton("立即更新", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                UmengUpdateAgent.startDownload(SettingActivity.this, response);
                                            }
                                        })
                                        .show();
                                break;
                            case UpdateStatus.No: // has no update
                                new CustomerAlertDialog(SettingActivity.this).builder().setTitle("提示")
                                        .setMsg("已经是最新版本")
                                        .setPositiveButton("确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        }).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                ToastUtil.makeText("没有wifi连接， 只在wifi下更新");
                                break;
                            case UpdateStatus.Timeout: // time out
                                ToastUtil.makeText("超时");
                                break;
                        }
                    }
                };
                CommonTools.doUmengUpdate(false, this, update);
                break;
            default:
                break;
        }
    }
}
