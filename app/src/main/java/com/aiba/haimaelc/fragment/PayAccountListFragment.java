package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.CommentStationActivity;
import com.aiba.haimaelc.activity.PayAccountActivity;
import com.aiba.haimaelc.adapter.PayAccountAdapter;
import com.aiba.haimaelc.model.PayAccount;
import com.aiba.haimaelc.tools.CommonTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PayAccountListFragment extends BaseFragment implements View.OnClickListener {

    private PayAccountActivity activity;
    private ListView listView;
    private PayAccountAdapter adapter;
    private List<PayAccount> accountList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (PayAccountActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_account_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        listView = (ListView) view.findViewById(R.id.pay_account_list_view);
        view.findViewById(R.id.tv_add_account).setOnClickListener(this);
        accountList = new ArrayList<>();
        // TODO: 2016/3/25 获取账号列表
        PayAccount account = new PayAccount();
        account.account = "123@456.com";
        account.account_type = "0";
        accountList.add(account);
        adapter = new PayAccountAdapter(activity, accountList, R.layout.item_pay_account);
        listView.setAdapter(adapter);
        CommonTools.setListViewHeightBasedOnChildren(listView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_account:
                activity.addFragment(new PayAccountAddFragment(), PayAccountActivity.ADD_ACCOUNT);
                break;
        }
    }
}
