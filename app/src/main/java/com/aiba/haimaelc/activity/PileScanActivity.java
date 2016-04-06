package com.aiba.haimaelc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.ChargePile;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.zxing.camera.CameraManager;
import com.aiba.haimaelc.zxing.decode.DecodeThread;
import com.aiba.haimaelc.zxing.utils.BeepManager;
import com.aiba.haimaelc.zxing.utils.CaptureActivityHandler;
import com.aiba.haimaelc.zxing.utils.InactivityTimer;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.reflect.Field;

public class PileScanActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = PileScanActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private View scanContainer, scanCropView, qr_code_editor;
    private ImageView scanLine;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;

    private EditText et_equipment_num;
    private ImageView iv_flash_light_switch;
    private String pile_equipment_num;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_pile_scan);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        qr_code_editor = findViewById(R.id.qr_code_editor);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(1500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("充电");
        TextView tv_title_bar_right = (TextView) findViewById(R.id.tv_title_bar_right);
        tv_title_bar_right.setVisibility(View.VISIBLE);
        tv_title_bar_right.setOnClickListener(this);
        tv_title_bar_right.setText("帮助");
        et_equipment_num = (EditText) findViewById(R.id.et_equipment_num);
        iv_flash_light_switch = (ImageView) findViewById(R.id.iv_flash_light_switch);
        iv_flash_light_switch.setOnClickListener(this);
        findViewById(R.id.bt_submit).setOnClickListener(this);
        findViewById(R.id.bt_scan_code).setOnClickListener(this);
        findViewById(R.id.bt_edit_code).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        scanLine.clearAnimation();
        scanLine.setVisibility(View.GONE);
        String rawResultText = rawResult.getText();
        if (TextUtils.isEmpty(rawResultText)) {
            ToastUtil.makeText("扫码失败");
            finish();
        } else {
            pile_equipment_num = rawResultText;
            LogUtils.logE(pile_equipment_num);
            getPileInfo();
        }
//        Intent resultIntent = new Intent();
//        bundle.putInt("width", mCropRect.width());
//        bundle.putInt("height", mCropRect.height());
//        bundle.putString("result", rawResult.getText());
//        resultIntent.putExtras(bundle);
//        this.setResult(RESULT_OK, resultIntent);
//        PileScanActivity.this.finish();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                pile_equipment_num = et_equipment_num.getText().toString().trim();
                if (TextUtils.isEmpty(pile_equipment_num)) {
                    ToastUtil.makeText("请输入请输入设备号码");
                    return;
                }
                getPileInfo();
                break;
            case R.id.bt_scan_code:
                qr_code_editor.setVisibility(View.GONE);
                scanContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_edit_code:
                qr_code_editor.setVisibility(View.VISIBLE);
                scanContainer.setVisibility(View.GONE);
                break;
            case R.id.iv_flash_light_switch://闪光灯
                boolean isFlashLightOn = cameraManager.flashSwitch();
                if (isFlashLightOn) {
                    iv_flash_light_switch.setImageResource(R.mipmap.bt_flashlight_on);
                } else {
                    iv_flash_light_switch.setImageResource(R.mipmap.bt_flashlight_off);
                }
                break;
            case R.id.tv_title_bar_right:
                // TODO: 2016/3/16 充电使用帮助
                break;
        }
    }

    private void getPileInfo() {
        // TODO: 2016/3/16 根据设备号码获取电桩
        ChargePile pile = new ChargePile();
        pile.pile_name = "2号桩";
        pile.pile_id = "12";
        pile.station_name = "测试充电站1";
        pile.station_id = "31";
        Intent intent = new Intent(PileScanActivity.this, ChargeDetailActivity.class);
        intent.putExtra(AppConst.PILE_DETAIL, pile);
        startActivity(intent);
        finish();
    }
}