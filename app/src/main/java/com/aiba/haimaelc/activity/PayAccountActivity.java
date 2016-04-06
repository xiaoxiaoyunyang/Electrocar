package com.aiba.haimaelc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.fragment.PayAccountAddFragment;
import com.aiba.haimaelc.fragment.PayAccountListFragment;
import com.aiba.haimaelc.model.PayAccount;

import java.util.Random;

public class PayAccountActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACCOUNT_LIST = "account_list";
    public static final String ADD_ACCOUNT = "add_account";
    public static final String VALIDATE_ACCOUNT = "validate_account";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_account);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.iv_title_bar_back).setOnClickListener(this);
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        if (new Random().nextInt(2) == 1) {
            addFragment(new PayAccountListFragment(), ACCOUNT_LIST);
        } else {
            addFragment(new PayAccountAddFragment(), ADD_ACCOUNT);
        }
    }

    public void addFragment(Fragment fragment, String tag) {
        switch (tag) {
            case ACCOUNT_LIST:
                setTitleText("我的付款");
                break;
            case ADD_ACCOUNT:
                setTitleText("支付宝");
                break;
            case VALIDATE_ACCOUNT:
                setTitleText("验证您的支付宝账号");
                break;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_out);
        fragmentTransaction.replace(R.id.pay_account_layout, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_bar_back:
                popBackStack();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    private void popBackStack() {
        Fragment frag;
        if ((frag = fragmentManager.findFragmentByTag(VALIDATE_ACCOUNT)) != null && frag.isVisible()) {
            fragmentManager.popBackStack();
            setTitleText("支付宝");
        } else if (fragmentManager.findFragmentByTag(ACCOUNT_LIST) != null &&
                (frag = fragmentManager.findFragmentByTag(ADD_ACCOUNT)) != null && frag.isVisible()) {
            fragmentManager.popBackStack();
            setTitleText("我的付款");
        } else {
            finish();
        }
    }
}
