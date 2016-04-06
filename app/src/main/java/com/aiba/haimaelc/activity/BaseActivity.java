package com.aiba.haimaelc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysApp;
import com.aiba.haimaelc.tools.StatusBarUtil;

public class BaseActivity extends AppCompatActivity {

    //handler
    public static final int HANLDER_VERY_BUTN_COUNT = 101;
    public static final int HANDLER_VERY_NOT_REGISTER = 102;
    public static final int ADD_UP = 103;
    public static final int CUT_DOWN = 104;
    public static final int SHOW_LOGIN = 105;
    //activity
    public static final int SEARCH_ADDRESS = 20001;
    public static final int CAMERA_CAPTURE = 20002;
    public static final int SELECT_FROM_ALBUM = 20003;
    public static final int SELECT_ADDRESS = 20004;
    public static final int SELECT_CITY = 20005;
    public static final int CROP_PHOTO = 20006;
    public static final int MODIFY_PHONE = 20007;
    public static final int MODIFY_NICKNAME = 20008;
    public static final int ORDER_DETAIL = 20009;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(this instanceof LoginActivity)) {
            SysApp.getInstance().addActivity(this);
        }
//        initWindow();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
//        setStatusBar();
    }

    protected  void setStatusBar(){
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitleBarBack();
    }

    protected void setTitleText(String str) {
        TextView title = (TextView) findViewById(R.id.tv_title);
        if (title != null) {
            title.setText(str);
        }
    }

    protected void setTitleBarBack() {
        View titleBarBack = findViewById(R.id.iv_title_bar_back);
        if (titleBarBack != null) {
            titleBarBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
