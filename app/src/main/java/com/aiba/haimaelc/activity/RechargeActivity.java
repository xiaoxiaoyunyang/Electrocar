package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.SortCity;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private CheckBox cb_union_pay, cb_wechat_pay, cb_ali_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("账户余额");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("立即付款");
        findViewById(R.id.view_ali_pay).setOnClickListener(this);
        findViewById(R.id.view_union_pay).setOnClickListener(this);
        findViewById(R.id.view_wechat_pay).setOnClickListener(this);
        cb_ali_pay = (CheckBox) findViewById(R.id.cb_ali_pay);
        cb_wechat_pay = (CheckBox) findViewById(R.id.cb_wechat_pay);
        cb_union_pay = (CheckBox) findViewById(R.id.cb_union_pay);
        initCheckBox();
    }

    private void initCheckBox() {
        cb_ali_pay.setChecked(false);
        cb_wechat_pay.setChecked(false);
        cb_union_pay.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                break;
            case R.id.view_ali_pay:
                cb_ali_pay.setChecked(true);
                cb_wechat_pay.setChecked(false);
                cb_union_pay.setChecked(false);
                break;
            case R.id.view_union_pay:
                cb_ali_pay.setChecked(false);
                cb_wechat_pay.setChecked(false);
                cb_union_pay.setChecked(true);
                break;
            case R.id.view_wechat_pay:
                cb_ali_pay.setChecked(false);
                cb_wechat_pay.setChecked(true);
                cb_union_pay.setChecked(false);
                break;
        }
    }
}
