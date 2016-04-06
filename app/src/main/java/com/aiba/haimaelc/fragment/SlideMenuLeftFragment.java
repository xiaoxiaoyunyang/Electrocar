package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.CarInfoActivity;
import com.aiba.haimaelc.activity.RechargeActivity;
import com.aiba.haimaelc.activity.RescueMapActivity;
import com.aiba.haimaelc.activity.ChargeStationCollectionActivity;
import com.aiba.haimaelc.activity.ChargeStationMapActivity;
import com.aiba.haimaelc.activity.MainActivity;
import com.aiba.haimaelc.activity.MessageCenterActivity;
import com.aiba.haimaelc.activity.OrderListActivity;
import com.aiba.haimaelc.activity.PayAccountActivity;
import com.aiba.haimaelc.activity.ProfileActivity;
import com.aiba.haimaelc.activity.SettingActivity;

/**
 * Created by zhu on 16/3/21.
 */
public class SlideMenuLeftFragment extends BaseFragment implements View.OnClickListener {

    private MainActivity activity;
    //登录 头像 手机号
    private View loginView;
    private ImageView headImage;
    private TextView userName, phone;
    //预约 充电 救援
    private View myOrder, myCharge, myRescue;
    private TextView order, charge, rescue;
    //账户余额
    private View balanceView;
    private TextView balance;
    //车辆信息 查找电桩 充电救援
    private View pay_account, carInfo, searchPile, chargeRescue;
    //消息中心
    private View messageCenter;
    private ImageView newMessage;
    //我的收藏
    private View collect;
    //设置
    private View set;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_slide_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initValue();
    }

    private void initView(View view) {
        loginView = view.findViewById(R.id.login_view);
        loginView.setOnClickListener(this);
        headImage = (ImageView) view.findViewById(R.id.head_portrait);//个人头像
        userName = (TextView) view.findViewById(R.id.username);
        phone = (TextView) view.findViewById(R.id.phone);

        myOrder = view.findViewById(R.id.myorder);
        myOrder.setOnClickListener(this);
        myCharge = view.findViewById(R.id.mycharge);
        myCharge.setOnClickListener(this);
        myRescue = view.findViewById(R.id.myrescue);
        myRescue.setOnClickListener(this);
        order = (TextView) view.findViewById(R.id.order);
        order.setText(10 + "");
        charge = (TextView) view.findViewById(R.id.charge);
        rescue = (TextView) view.findViewById(R.id.rescue);

        balanceView = view.findViewById(R.id.account_balance);
        balanceView.setOnClickListener(this);
        balance = (TextView) view.findViewById(R.id.balance);

        pay_account = view.findViewById(R.id.pay_account);
        pay_account.setOnClickListener(this);
        carInfo = view.findViewById(R.id.car_info);
        carInfo.setOnClickListener(this);
        searchPile = view.findViewById(R.id.search_pile);
        searchPile.setOnClickListener(this);
        chargeRescue = view.findViewById(R.id.charge_rescue);
        chargeRescue.setOnClickListener(this);

        messageCenter = view.findViewById(R.id.message_center);
        messageCenter.setOnClickListener(this);
        newMessage = (ImageView) view.findViewById(R.id.new_message);

        collect = view.findViewById(R.id.collect);
        collect.setOnClickListener(this);
        set = view.findViewById(R.id.set);
        set.setOnClickListener(this);
    }

    private void initValue() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_view:
                intent = new Intent(activity, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.myorder:
                intent = new Intent(activity, OrderListActivity.class);
                intent.putExtra(AppConst.ORDER_TYPE, 1);
                startActivity(intent);
                break;
            case R.id.mycharge:
                intent = new Intent(activity, OrderListActivity.class);
                intent.putExtra(AppConst.ORDER_TYPE, 2);
                startActivity(intent);
                break;
            case R.id.myrescue:
                intent = new Intent(activity, OrderListActivity.class);
                intent.putExtra(AppConst.ORDER_TYPE, 3);
                startActivity(intent);
                break;
            case R.id.account_balance:
                intent = new Intent(activity, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.pay_account:
                intent = new Intent(activity, PayAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.car_info:
                intent = new Intent(activity, CarInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.search_pile:
                intent = new Intent(activity, ChargeStationMapActivity.class);
                startActivity(intent);
                break;
            case R.id.charge_rescue:
                intent = new Intent(activity, RescueMapActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.message_center:
                intent = new Intent(activity, MessageCenterActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.collect:
                intent = new Intent(activity, ChargeStationCollectionActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.set:
                intent = new Intent(activity, SettingActivity.class);
                activity.startActivity(intent);
                break;
        }
//        activity.getSlidingMenu().showContent();
    }

}
