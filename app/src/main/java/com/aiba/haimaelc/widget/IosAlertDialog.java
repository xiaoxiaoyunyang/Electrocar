package com.aiba.haimaelc.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.aiba.haimaelc.R;

import java.util.Timer;
import java.util.TimerTask;

public class IosAlertDialog {

    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private EditText txt_input;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showInput = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean autoDismiss = true;

    public IosAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IosAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_alert_dialog_ios, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        txt_input = (EditText) view.findViewById(R.id.txt_input);
        txt_input.setVisibility(View.GONE);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(
                (int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        return this;
    }

    public IosAlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public IosAlertDialog setMsg(CharSequence msg) {
        showMsg = true;
        txt_msg.setText(msg);
        return this;
    }

    public IosAlertDialog setMsg(String msg) {
        showMsg = true;
        txt_msg.setText(msg);
        return this;
    }

    public IosAlertDialog setInputShow(boolean isShow) {
        showInput = isShow;
        return this;
    }

    public IosAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public IosAlertDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public IosAlertDialog setPositiveButton(String text, final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoDismiss)
                    dialog.dismiss();
                listener.onClick(v);
            }
        });
        return this;
    }

    public IosAlertDialog setNegativeButton(String text, final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoDismiss)
                    dialog.dismiss();
                listener.onClick(v);
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (showInput) {
            txt_input.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.bt_selector_alert_dialog_single);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoDismiss)
                        dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.bt_selector_alert_dialog_right);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.bt_selector_alert_dialog_left);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.bt_selector_alert_dialog_single);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.bt_selector_alert_dialog_single);
        }
    }

    public void show() {
        setLayout();
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInput() {
        return txt_input.getText().toString();
    }

    public IosAlertDialog setInput(String input, int type) {
        txt_input.setText(input);
        txt_input.setInputType(type);
        return this;
    }

    public IosAlertDialog setInputHint(String hint) {
        txt_input.setHint(hint);
        return this;
    }

    public IosAlertDialog setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return this;
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void showKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (txt_input != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置可获得焦点
                            txt_input.setFocusable(true);
                            txt_input.setFocusableInTouchMode(true);
                            //请求获得焦点
                            txt_input.requestFocus();
                            //调用系统输入法
                            InputMethodManager inputManager = (InputMethodManager) txt_input
                                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(txt_input, 0);
                        }
                    });
                }
            }
        }, 200);
    }

    public IosAlertDialog setMsgLeft() {
        txt_msg.setGravity(Gravity.START);
        return this;
    }
}
