package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.fragment.ChargeInfoFragment;
import com.aiba.haimaelc.fragment.ChargingFragment;
import com.aiba.haimaelc.model.ChargeOrder;
import com.aiba.haimaelc.model.ChargePile;

public class ChargeDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String CHARGING = "charging";
    public static final String CHARGE_INFO = "charge_info";
    private FragmentManager fragmentManager;
    private TextView tv_title_bar_right;
    public ChargePile chargePile;
    public ChargeOrder chargeOrder;
    public boolean isChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_detail);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        chargePile = (ChargePile) intent.getSerializableExtra(AppConst.PILE_DETAIL);
        chargeOrder = (ChargeOrder) intent.getSerializableExtra(AppConst.CHARGE_ORDER);
        if (chargePile == null && chargeOrder == null) {
            finish();
            return;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.iv_title_bar_back).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        tv_title_bar_right = (TextView) findViewById(R.id.tv_title_bar_right);
        if (chargeOrder == null || "0".equals(chargeOrder.order_state)) {
            setTitleText("充电");
            tv_title_bar_right.setVisibility(View.VISIBLE);
            tv_title_bar_right.setOnClickListener(this);
            tv_title_bar_right.setText("帮助");
            addFragment(new ChargingFragment(), CHARGING);
        } else {
            setTitleText("充电信息");
            addFragment(new ChargeInfoFragment(), CHARGE_INFO);
        }
    }

    public void addFragment(Fragment fragment, String tag) {
        if (fragment instanceof ChargeInfoFragment) {
            tv_title_bar_right.setVisibility(View.GONE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_out);
        fragmentTransaction.replace(R.id.charge_detail_layout, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_bar_right:
                // TODO: 2016/3/16 充电使用帮助
                break;
            case R.id.iv_title_bar_back:
                if (isChanged) {
                    setResult(RESULT_OK);
                }
                finish();
                break;
        }
    }
}
