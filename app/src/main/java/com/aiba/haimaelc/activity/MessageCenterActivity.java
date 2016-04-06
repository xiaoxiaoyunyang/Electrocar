package com.aiba.haimaelc.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.adapter.MessageAdapter;
import com.aiba.haimaelc.model.Message;
import com.aiba.haimaelc.widget.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private RefreshLayout mRefreshLayout;
    private TextView tv_no_data;
    private boolean isHasLoadedAll = true;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("消息中心");
        initListView();
        new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    private void initListView() {
        messageList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.refresh_list_view);
        listView.setDividerHeight(0);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        messageAdapter = new MessageAdapter(this, messageList, R.layout.item_message);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rl_service_refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isHasLoadedAll = true;
                new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
            }
        });
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
            }
        });
        mRefreshLayout.setListView(listView, messageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private class MyTask extends AsyncTask<Integer, Void, Boolean> {

        private int type = 0;
        List<Message> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRefreshLayout.setIsHasLoadedAll(true);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            switch (type = params[0]) {
                case 0:
                    try {
                        // TODO: 2016/3/11 测试数据
                        Message msg = new Message();
                        msg.title = "交易成功";
                        msg.content = "交易内容交易内容交易内容交易内容交易内容交易内容交易内容交易内容";
                        list.add(msg);
                        msg = new Message();
                        msg.title = "预约电桩成功";
                        msg.content = "预约内容预约内容预约内容预约内容预约内容";
                        list.add(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mRefreshLayout.setLoading(false);
            mRefreshLayout.setRefreshing(false);
            isHasLoadedAll = list != null && list.size() < AppConfig.LIST_NUM;
            if (result) {
                switch (type) {
                    case 0:
                        if (page == 0) {
                            messageList.clear();
                        }
                        page++;
                        if (list != null && list.size() > 0) {
                            messageList.addAll(list);
                            messageAdapter.notifyDataSetChanged();
                        } else {
                            tv_no_data.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
            mRefreshLayout.setIsHasLoadedAll(isHasLoadedAll);
        }
    }
}
