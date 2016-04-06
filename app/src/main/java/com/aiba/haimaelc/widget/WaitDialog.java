package com.aiba.haimaelc.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;

public class WaitDialog {

    private Context context;
    private Dialog dialog;
    private Display display;
    private View wait_dialog_layout;
    private TextView messageText;
    private ImageView loadingAnim;
    private AnimationDrawable animationDrawable;

    public WaitDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public WaitDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_wait_dialog, null);
        // 获取自定义Dialog布局中的控件
        messageText = (TextView) view.findViewById(R.id.waitMessage);
        wait_dialog_layout = view.findViewById(R.id.wait_dialog_layout);
        loadingAnim = (ImageView) view.findViewById(R.id.loadingAnim);
        animationDrawable = (AnimationDrawable) loadingAnim.getDrawable();
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        // 调整dialog背景大小
        wait_dialog_layout.setLayoutParams(new FrameLayout.LayoutParams(
                (int) (display.getWidth() * 0.5), FrameLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public WaitDialog setMessage(String message) {
        messageText.setText(message);
        return this;
    }

    public void show() {
        try {
            dialog.show();
            animationDrawable.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    public void dismiss() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
