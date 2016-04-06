package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aiba.haimaelc.R;

public class CarInfoActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("车辆信息");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("确定");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_submit:
                finish();
                break;
        }
    }
}
