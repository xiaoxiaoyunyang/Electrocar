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
import com.amap.api.maps.model.Text;

import java.util.Timer;
import java.util.TimerTask;

public class CustomerAlertDialog {

    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private ImageView img_msg;
    private EditText txt_input;
    private TextView btn_neg;
    private TextView btn_pos;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsgImg = false;
    private boolean showMsg = false;
    private boolean showInput = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean autoDismiss = true;

    public CustomerAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public CustomerAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_alert_dialog, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        img_msg = (ImageView) view.findViewById(R.id.img_msg);
        img_msg.setVisibility(View.GONE);
        txt_input = (EditText) view.findViewById(R.id.txt_input);
        txt_input.setVisibility(View.GONE);
        btn_neg = (TextView) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (TextView) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(
                (int) (display.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        return this;
    }

    public CustomerAlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public CustomerAlertDialog setMsg(CharSequence msg) {
        showMsg = true;
        txt_msg.setText(msg);
        return this;
    }

    public CustomerAlertDialog setMsg(String msg) {
        showMsg = true;
        txt_msg.setText(msg);
        return this;
    }

    public CustomerAlertDialog setImg(int resId) {
        showMsgImg = true;
        img_msg.setImageResource(resId);
        return this;
    }

    public CustomerAlertDialog setInputShow(boolean isShow) {
        showInput = isShow;
        return this;
    }

    public CustomerAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CustomerAlertDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public CustomerAlertDialog setPositiveButton(String text, final OnClickListener listener) {
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

    public CustomerAlertDialog setNegativeButton(String text, final OnClickListener listener) {
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

        if (showMsgImg) {
            img_msg.setVisibility(View.VISIBLE);
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
            btn_pos.setBackgroundResource(R.drawable.bt_green);
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
            btn_pos.setBackgroundResource(R.drawable.bt_green);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.bt_white);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.bt_green);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.bt_green);
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

    public CustomerAlertDialog setInput(String input, int type) {
        txt_input.setText(input);
        txt_input.setInputType(type);
        return this;
    }

    public CustomerAlertDialog setInputHint(String hint) {
        txt_input.setHint(hint);
        return this;
    }

    public CustomerAlertDialog setAutoDismiss(boolean autoDismiss) {
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

    public CustomerAlertDialog setMsgLeft() {
        txt_msg.setGravity(Gravity.START);
        return this;
    }
}
