package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.PayAccountActivity;

public class PayAccountValidateFragment extends BaseFragment implements View.OnClickListener {

    private PayAccountActivity activity;
    private EditText et_validate_code;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (PayAccountActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_account_validate, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setText("验证");
        bt_submit.setOnClickListener(this);
        et_validate_code = (EditText) view.findViewById(R.id.et_validate_code);
        et_validate_code.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                // TODO: 2016/3/25 验证
                break;
        }
    }
}
