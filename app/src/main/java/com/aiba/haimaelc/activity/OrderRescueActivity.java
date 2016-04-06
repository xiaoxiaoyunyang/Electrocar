package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.Address;
import com.aiba.haimaelc.model.RescueOrder;
import com.aiba.haimaelc.tools.ToastUtil;

import java.util.LinkedHashMap;

public class OrderRescueActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private EditText et_address_detail, et_name, et_phone, et_plate;
    private TextView tv_address_title;
    private Address address;
    private RadioGroup rg_gender;
    private String gender = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_rescue);
        Intent intent = getIntent();
        if (intent != null) {
            address = (Address) intent.getSerializableExtra(AppConst.SELECT_ADDRESS);
        }
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("确认信息");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("提交信息");
        findViewById(R.id.choose_position).setOnClickListener(this);
        et_address_detail = (EditText) findViewById(R.id.et_address_detail);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_plate = (EditText) findViewById(R.id.et_plate);
        et_name.setText(SysModel.user.name);
        et_phone.setText(SysModel.user.phone);
        RadioButton rb_male = (RadioButton) findViewById(R.id.rb_male);
        RadioButton rb_female = (RadioButton) findViewById(R.id.rb_female);
        gender = SysModel.user.gender;
        if ("0".equals(SysModel.user.gender)) {
            rb_male.setChecked(true);
        } else {
            rb_female.setChecked(true);
        }
        et_plate.setText(SysModel.car.plate);
        tv_address_title = (TextView) findViewById(R.id.tv_address_title);
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(this);
        if (address != null) {
            tv_address_title.setText(address.name);
            et_address_detail.setText(address.address);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_submit:
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    ToastUtil.makeText("请输入联系人");
                    return;
                }
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    ToastUtil.makeText("请输入联系电话");
                    return;
                }
                if (TextUtils.isEmpty(et_plate.getText().toString().trim())) {
                    ToastUtil.makeText("请输入车牌号");
                    return;
                }
                if (TextUtils.isEmpty(tv_address_title.getText().toString().trim())) {
                    ToastUtil.makeText("请选择救援地址");
                    return;
                }
                orderRescue(true);
                break;
            case R.id.choose_position:
                intent = new Intent(OrderRescueActivity.this, SearchMapActivity.class);
                intent.putExtra(AppConst.FROM_ORDER_RESCUE, true);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_ADDRESS:
                    address = (Address) data.getSerializableExtra(AppConst.SELECT_ADDRESS);
                    tv_address_title.setText(address.name);
                    et_address_detail.setText(address.address);
                    break;
            }
        }
    }

    private void orderRescue(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("aid", SysModel.user.aid);
        values.put("name", et_name.getText().toString().trim());
        values.put("gender", gender);
        values.put("phone", et_phone.getText().toString().trim());
        values.put("plate", et_plate.getText().toString().trim());
        values.put("latitude", String.valueOf(address.latitude));
        values.put("longitude", String.valueOf(address.longitude));
        values.put("position", tv_address_title.getText().toString().trim() + et_address_detail.getText().toString().trim());
        ApiRequest request = new ApiRequest(this, ApiList.Rescue.getUrl(), ApiList.Rescue.getMethod(), values);
        request.showProgress(showProgress).callApi(new ReturnCallBack<RescueOrder>() {
            @Override
            public void onSuccess(int from, RescueOrder object) {
                Intent intent = new Intent(OrderRescueActivity.this, RescueDetailActivity.class);
                intent.putExtra(AppConst.RESCUE_ORDER, object);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int code, int from, String error) {
                ToastUtil.makeText(error);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_male:
                gender = "0";
                break;
            case R.id.rb_female:
                gender = "1";
                break;
        }
    }
}
