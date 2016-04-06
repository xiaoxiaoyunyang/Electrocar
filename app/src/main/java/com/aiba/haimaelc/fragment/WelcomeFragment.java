package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.activity.BaseActivity;
import com.aiba.haimaelc.activity.LoginActivity;
import com.aiba.haimaelc.activity.MainActivity;
import com.aiba.haimaelc.model.CarGpsInfo;
import com.aiba.haimaelc.model.CarInfo;
import com.aiba.haimaelc.model.UserInfo;

public class WelcomeFragment extends BaseFragment implements View.OnClickListener {

    private LoginActivity activity;
    private View show_button;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseActivity.SHOW_LOGIN:
                    show_button.setVisibility(View.VISIBLE);
                    activity.showLogin = true;
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LoginActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        show_button = view.findViewById(R.id.show_button);
        view.findViewById(R.id.tv_login).setOnClickListener(this);
        view.findViewById(R.id.tv_register).setOnClickListener(this);
        view.findViewById(R.id.tv_experience).setOnClickListener(this);
        if (activity.showLogin) {
            show_button.setVisibility(View.VISIBLE);
        } else {
            show_button.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(BaseActivity.SHOW_LOGIN, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_login:
                activity.addFragment(new LoginFragment(), LoginActivity.LOGIN);
                activity.forRegister = false;
                break;
            case R.id.tv_register:
                activity.addFragment(new LoginFragment(), LoginActivity.LOGIN);
                activity.forRegister = true;
                break;
            case R.id.tv_experience:
                // TODO: 2016/4/5 测试数据
                SysModel.user = new UserInfo();
                SysModel.user.aid = "5";
                SysModel.user.nickname = "测试用户";
                SysModel.user.gender = "0";
                SysModel.user.phone = "12345678901";
                SysModel.car = new CarInfo();
                SysModel.car.plate = "沪B20385";
                SysModel.carGps = new CarGpsInfo();
                SysModel.carGps.latitude = "39.9616042740";
                SysModel.carGps.longitude = "116.4425558698";
                intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                activity.finish();
                break;
        }
    }
}
