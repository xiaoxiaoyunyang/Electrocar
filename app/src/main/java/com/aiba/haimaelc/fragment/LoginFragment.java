package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.BaseActivity;
import com.aiba.haimaelc.activity.LoginActivity;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.UserInfo;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.PhoneSaveUtils;
import com.aiba.haimaelc.tools.ToastUtil;

import java.util.LinkedHashMap;
import java.util.Locale;

public class LoginFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private LoginActivity activity;
    private TextView tv_get_code, tv_notice, login_switch;
    private String userPhone;
    private EditText et_user_phone, et_code, et_password;//用户名输入编辑框
    private Button bt_submit;
    private View tv_register_tip, show_et_password, show_et_code;
    private boolean loginByPassword = true;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseActivity.HANLDER_VERY_BUTN_COUNT:
                    Integer countNum = (Integer) tv_get_code.getTag();
                    countNum--;
                    removeMessages(BaseActivity.HANLDER_VERY_BUTN_COUNT);
                    if (countNum > 0) {
                        tv_get_code.setTag(countNum);
                        tv_get_code.setEnabled(false);
                        tv_get_code.setText(String.format(Locale.CHINA, "%ds后重新获取", countNum));
                        sendEmptyMessageDelayed(BaseActivity.HANLDER_VERY_BUTN_COUNT, 1000);
                    } else if (countNum <= -100) {
                        tv_get_code.setText("获取失败");
                        tv_get_code.setEnabled(true);
                    } else {
                        tv_get_code.setEnabled(true);
                        tv_get_code.setText("重新获取");
                    }
                    break;
                case BaseActivity.HANDLER_VERY_NOT_REGISTER:
                    removeMessages(BaseActivity.HANLDER_VERY_BUTN_COUNT);
                    tv_get_code.setText("获取验证码");
                    tv_get_code.setEnabled(true);
                    break;
                default:
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
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        view.findViewById(R.id.iv_title_bar_back).setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initView() {
        tv_register_tip = view.findViewById(R.id.tv_register_tip);
        show_et_password = view.findViewById(R.id.show_et_password);
        show_et_code = view.findViewById(R.id.show_et_code);
        login_switch = (TextView) view.findViewById(R.id.login_switch);
        login_switch.setOnClickListener(this);
        tv_get_code = (TextView) view.findViewById(R.id.tv_get_code);
        tv_notice = (TextView) view.findViewById(R.id.tv_notice);
        tv_get_code.setOnClickListener(this);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setEnabled(false);
        et_user_phone = (EditText) view.findViewById(R.id.et_user_phone);
        et_user_phone.addTextChangedListener(this);
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_code.addTextChangedListener(this);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_password.addTextChangedListener(this);
        view.findViewById(R.id.iv_phone_number_delete).setOnClickListener(this);
        if (activity.forRegister) {
            activity.setTitleText("注册");
            bt_submit.setText("注册并登录");
            tv_register_tip.setVisibility(View.VISIBLE);
            show_et_code.setVisibility(View.VISIBLE);
            show_et_password.setVisibility(View.VISIBLE);
            et_password.setHint("请输入6-20位字符密码");
            et_code.setHint("请输入手机验证码");
            tv_get_code.setVisibility(View.VISIBLE);
            tv_notice.setText("注册即表示同意");
            login_switch.setVisibility(View.GONE);
        } else {
            activity.setTitleText("登录");
            bt_submit.setText("登录");
            tv_register_tip.setVisibility(View.GONE);
            show_et_code.setVisibility(View.GONE);
            show_et_password.setVisibility(View.VISIBLE);
            et_password.setHint("请输入密码");
            tv_notice.setText("登录即表示同意");
            login_switch.setVisibility(View.VISIBLE);
        }
        long sendTime = PhoneSaveUtils.getLong(AppConst.SEND_VERIFICATION_CODE_TIME, 0);
        int interval = (int) ((System.currentTimeMillis() - sendTime) / 1000);
        if (interval < 60) {
            tv_get_code.setTag(60 - interval);
            mHandler.sendEmptyMessage(BaseActivity.HANLDER_VERY_BUTN_COUNT);
        }
    }

    private void getCodeClick(View view) {
        InputMethodManager imm = (InputMethodManager) (activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        // 获取验证码
        userPhone = et_user_phone.getText().toString().trim();
        if (TextUtils.isEmpty(userPhone)) {
            ToastUtil.makeText("请输入手机号码");
            return;
        }
        // TODO: 2016/3/22 获取验证码
        tv_get_code.setTag(AppConfig.SMS_INTERVAL);
        PhoneSaveUtils.putLong(AppConst.SEND_VERIFICATION_CODE_TIME, System.currentTimeMillis());
        mHandler.sendEmptyMessage(BaseActivity.HANLDER_VERY_BUTN_COUNT);
    }

    private void switchLoginMode() {
        loginByPassword = !loginByPassword;
        if (loginByPassword) {
            login_switch.setText("切换至验证码登录");
            et_code.setHint("请输入密码");
            show_et_code.setVisibility(View.GONE);
            show_et_password.setVisibility(View.VISIBLE);
            bt_submit.setEnabled(!TextUtils.isEmpty(et_user_phone.getText().toString().trim())
                    && !TextUtils.isEmpty(et_password.getText().toString().trim()));
        } else {
            login_switch.setText("切换至密码登录");
            et_code.setHint("请输入手机验证码");
            show_et_code.setVisibility(View.VISIBLE);
            show_et_password.setVisibility(View.GONE);
            bt_submit.setEnabled(!TextUtils.isEmpty(et_user_phone.getText().toString().trim())
                    && !TextUtils.isEmpty(et_code.getText().toString().trim()));
        }
    }

    @Override
    public void onClick(View v) {
        String uid = "3";
        switch (v.getId()) {
            case R.id.tv_get_code:
                getCodeClick(v);
                break;
            case R.id.bt_submit:
                if (activity.forRegister) {
                    ToastUtil.makeText("注册");
                    LinkedHashMap<String, String> values = new LinkedHashMap<>();
                    ApiRequest request = new ApiRequest(activity, ApiList.GetUserInfo.getUrl().replaceAll("ID", uid), ApiList.GetUserInfo.getMethod(), values);
                    request.callApi(new ReturnCallBack<UserInfo>() {
                        @Override
                        public void onSuccess(int from, UserInfo object) {
                            LogUtils.logE("onSuccess");
                            LogUtils.logE(object.nickname);
                        }

                        @Override
                        public void onError(int code, int from, String error) {
                            LogUtils.logE("onError");
                        }
                    });
                } else if (loginByPassword) {
                    ToastUtil.makeText("密码登录");
                    LinkedHashMap<String, String> values = new LinkedHashMap<>();
                    values.put("nickname", "昵称");
                    values.put("phone", "12345678901");
                    values.put("imgsrc", "1234567");
                    ApiRequest request = new ApiRequest(activity, ApiList.UpdateUserInfo.getUrl().replaceAll("ID", uid), ApiList.UpdateUserInfo.getMethod(), values);
                    request.callApi(new ReturnCallBack<String>() {
                        @Override
                        public void onSuccess(int from, String object) {
                            LogUtils.logE("onSuccess");
                        }

                        @Override
                        public void onError(int code, int from, String error) {
                            LogUtils.logE("onError");
                        }
                    });
                } else {
                    ToastUtil.makeText("验证码登录");
                }
                break;
            case R.id.iv_phone_number_delete:
                et_user_phone.setText("");
                break;
            case R.id.iv_title_bar_back:
                activity.popBackStack();
                break;
            case R.id.login_switch:
                switchLoginMode();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (activity.forRegister) {
            bt_submit.setEnabled(!TextUtils.isEmpty(et_user_phone.getText().toString().trim())
                    && !TextUtils.isEmpty(et_code.getText().toString().trim())
                    && !TextUtils.isEmpty(et_password.getText().toString().trim()));
        } else if (loginByPassword) {
            bt_submit.setEnabled(!TextUtils.isEmpty(et_user_phone.getText().toString().trim())
                    && !TextUtils.isEmpty(et_password.getText().toString().trim()));
        } else {
            bt_submit.setEnabled(!TextUtils.isEmpty(et_user_phone.getText().toString().trim())
                    && !TextUtils.isEmpty(et_code.getText().toString().trim()));
        }
    }
}
