package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.tools.LogUtils;

public class ModifyInformationActivity extends BaseActivity implements View.OnClickListener {

    private String modify_content;
    private EditText et_modify_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        modify_content = intent.getStringExtra(AppConst.MODIFY_TYPE);
        if (TextUtils.isEmpty(modify_content)) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        et_modify_content = (EditText) findViewById(R.id.et_modify_content);
        TextView tv_title_bar_right = (TextView) findViewById(R.id.tv_title_bar_right);
        tv_title_bar_right.setVisibility(View.VISIBLE);
        tv_title_bar_right.setText("保存");
        tv_title_bar_right.setOnClickListener(this);
        findViewById(R.id.delete_modify_content).setOnClickListener(this);
        switch (modify_content) {
            case AppConst.MODIFY_NICK_NAME:
                setTitleText("修改昵称");
                break;
            case AppConst.MODIFY_PHONE:
                setTitleText("修改手机号码");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_bar_right:
                LogUtils.logE("保存");
                break;
            case R.id.delete_modify_content:
                et_modify_content.setText("");
                break;
        }
    }
}
