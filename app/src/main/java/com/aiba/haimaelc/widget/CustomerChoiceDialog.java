package com.aiba.haimaelc.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.aiba.haimaelc.R;

public class CustomerChoiceDialog {

    private Dialog dialog;
    private Button choice1;
    private Button choice2;
    private Button cancel;
    private Context context;
    private View line;

    public CustomerChoiceDialog(Context context) {
        this.context = context;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.view_choice_dialog, null);
        cancel = (Button) dialogView.findViewById(R.id.cancel);
        choice1 = (Button) dialogView.findViewById(R.id.choice1);
        choice2 = (Button) dialogView.findViewById(R.id.choice2);
        line = dialogView.findViewById(R.id.line);
        dialog = new Dialog(context, R.style.ChoiceDialogStyle);
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.ChoiceDialogAnimation);
        WindowManager.LayoutParams wl = window.getAttributes();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wl.x = 0;
        wl.y = windowManager.getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void setButton1OnClickListener(OnClickListener listener) {
        choice1.setOnClickListener(listener);
    }

    public void setButton2OnClickListener(OnClickListener listener) {
        choice2.setOnClickListener(listener);
    }

    public void setButton1Text(String text, int colorId) {
        choice1.setText(text);
        choice1.setTextColor(context.getResources().getColor(colorId));
    }

    public void setButton2Text(String text, int colorId) {
        choice2.setText(text);
        choice2.setTextColor(context.getResources().getColor(colorId));
    }

    public void setButtonCancelText(String text) {
        cancel.setText(text);
    }

    public void setButtonVisible(boolean first) {
        choice1.setVisibility(first ? View.VISIBLE : View.GONE);
        line.setVisibility(first ? View.VISIBLE : View.GONE);
        choice2.setBackgroundResource(first ? R.drawable.bt_choice_dialog_corner_bottom_selector :
                R.drawable.bt_choice_dialog_corner_both_selector);
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }
}
