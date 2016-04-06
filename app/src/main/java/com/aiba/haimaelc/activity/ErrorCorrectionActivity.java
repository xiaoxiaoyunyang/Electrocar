package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;

public class ErrorCorrectionActivity extends BaseActivity implements View.OnClickListener {

    private String stationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_correction);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        stationId = intent.getStringExtra(AppConst.STATION_ID);
        if (TextUtils.isEmpty(stationId)) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("我要纠错");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("提交纠错");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                // TODO: 2016/3/15 纠错
                finish();
                break;
        }
    }
}
