package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.PayAccountActivity;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.StringUtils;
import com.aiba.haimaelc.tools.ToastUtil;

public class PayAccountAddFragment extends BaseFragment implements View.OnClickListener {

    private PayAccountActivity activity;
    private View input_alipay_phone;
    private EditText et_alipay_email, et_alipay_phone;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (PayAccountActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_account_add, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setText("提交");
        bt_submit.setOnClickListener(this);
        input_alipay_phone = view.findViewById(R.id.input_alipay_phone);
        et_alipay_email = (EditText) view.findViewById(R.id.et_alipay_email);
        et_alipay_phone = (EditText) view.findViewById(R.id.et_alipay_phone);
        et_alipay_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || StringUtils.isNumeric(s.toString().trim())) {
                    input_alipay_phone.setVisibility(View.GONE);
                } else {
                    input_alipay_phone.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkInput() {
        if (input_alipay_phone.getVisibility() == View.GONE) {
            if (et_alipay_email.getText().toString().trim().length() != 11) {
                ToastUtil.makeText("请输入正确的手机号码");
                return;
            }
        } else {
            if (!StringUtils.checkEmail(et_alipay_email.getText().toString().trim())) {
                ToastUtil.makeText("请输入正确的邮箱账号");
                return;
            }
            if (!StringUtils.isNumeric(et_alipay_phone.getText().toString().trim())
                    || et_alipay_phone.getText().toString().trim().length() != 11) {
                ToastUtil.makeText("请输入正确的手机号码");
                return;
            }
        }
        // TODO: 2016/3/25 提交账号
        activity.addFragment(new PayAccountValidateFragment(), PayAccountActivity.VALIDATE_ACCOUNT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                checkInput();
                break;
        }
    }
}
