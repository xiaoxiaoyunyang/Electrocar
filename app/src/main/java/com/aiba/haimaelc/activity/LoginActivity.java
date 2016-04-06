package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.fragment.WelcomeFragment;
import com.aiba.haimaelc.tools.Dictionary;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LoginActivity extends BaseActivity implements AMapLocationListener {

    public static final String WELCOME = "welcome";
    public static final String LOGIN = "login";
    private AMapLocationClient mLocationClient;
    private FragmentManager fragmentManager;
    public boolean showLogin;
    public boolean forRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        if (intent != null) {
            showLogin = intent.getBooleanExtra(AppConst.LOGOUT, false);
        }
        initView();
        initAGPS();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null)
            mLocationClient.onDestroy();
        mLocationClient = null;
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        addFragment(new WelcomeFragment(), WELCOME);
        // TODO: 2016/3/25 获取城市列表
        Dictionary.getSortCityList();
    }

    private void initAGPS() {
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationOption.setInterval(2 * 60 * 1000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    public void addFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
                R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_out);
        fragmentTransaction.replace(R.id.welcome_layout, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void popBackStack() {
        Fragment frag;
        if ((frag = fragmentManager.findFragmentByTag(LOGIN)) != null && frag.isVisible()) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void setTitleBarBack() {
        super.setTitleBarBack();
    }

    @Override
    public void setTitleText(String str) {
        super.setTitleText(str);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            Dictionary.saveLocation(aMapLocation);
        }
    }
}
