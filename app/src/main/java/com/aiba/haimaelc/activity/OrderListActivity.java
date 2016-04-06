package com.aiba.haimaelc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.OrderAdapter;
import com.aiba.haimaelc.http.ApiList;
import com.aiba.haimaelc.http.ApiRequest;
import com.aiba.haimaelc.http.ReturnCallBack;
import com.aiba.haimaelc.model.ChargeOrder;
import com.aiba.haimaelc.model.PileOrder;
import com.aiba.haimaelc.model.RescueOrder;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.RefreshLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private OrderAdapter orderAdapter;
    //    private List currentList;
    private List<PileOrder> pileOrderList;
    private List<ChargeOrder> chargeOrderList;
    private List<RescueOrder> rescueOrderList;
    private RefreshLayout mRefreshLayout;
    private TextView tv_no_data;
    private boolean isHasLoadedAll = true;
    private int page = 0;
    private int orderType;//1.预约;2.充电;3.救援

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        orderType = intent.getIntExtra(AppConst.ORDER_TYPE, 0);
        if (orderType == 0) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        setTitleBarBack();
        switch (orderType) {
            case 1:
                setTitleText("我的预约");
                break;
            case 2:
                setTitleText("我的充电");
                break;
            case 3:
                setTitleText("我的救援");
                break;
        }
        initListView();
    }

    private void initListView() {
        switch (orderType) {
            case 1:
                pileOrderList = new ArrayList<>();
                orderAdapter = new OrderAdapter(this, pileOrderList, R.layout.item_order);
                break;
            case 2:
                chargeOrderList = new ArrayList<>();
                orderAdapter = new OrderAdapter(this, chargeOrderList, R.layout.item_order);
                break;
            case 3:
                rescueOrderList = new ArrayList<>();
                orderAdapter = new OrderAdapter(this, rescueOrderList, R.layout.item_order);
                break;
        }
        listView = (ListView) findViewById(R.id.refresh_list_view);
        listView.setOnItemClickListener(this);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rl_service_refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isHasLoadedAll = true;
                getOrderList(false);
            }
        });
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                getOrderList(false);
            }
        });
        mRefreshLayout.setListView(listView, orderAdapter);
        getOrderList(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (orderType) {
            case 1:
                intent = new Intent(OrderListActivity.this, PileOrderDetailActivity.class);
                intent.putExtra(AppConst.PILE_ORDER, pileOrderList.get(position));
                startActivityForResult(intent, ORDER_DETAIL);
                break;
            case 2:
                intent = new Intent(OrderListActivity.this, ChargeDetailActivity.class);
                intent.putExtra(AppConst.CHARGE_ORDER, chargeOrderList.get(position));
                startActivityForResult(intent, ORDER_DETAIL);
                break;
            case 3:
                intent = new Intent(OrderListActivity.this, RescueDetailActivity.class);
                intent.putExtra(AppConst.RESCUE_ORDER, rescueOrderList.get(position));
                startActivityForResult(intent, ORDER_DETAIL);
                break;
        }
    }

    private void getOrderList(boolean showProgress) {
        switch (orderType) {
            case 1:
                getReservationList(showProgress);
                break;
            case 2:
                getChargeList(showProgress);
                break;
            case 3:
                getRescueList(showProgress);
                break;
        }
    }

    private void getReservationList(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("aid", SysModel.user.aid);
        values.put("page", String.valueOf(page));
        values.put("pageCount", String.valueOf(AppConfig.LIST_NUM));
        ApiRequest request = new ApiRequest(this, ApiList.GetReservationList.getUrl(), ApiList.GetReservationList.getMethod(), values);
        mRefreshLayout.setIsHasLoadedAll(true);
        request.showProgress(showProgress).callApi(new ReturnCallBack<List<PileOrder>>() {
            @Override
            public void onSuccess(int from, List<PileOrder> object) {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
                isHasLoadedAll = object != null && object.size() < AppConfig.LIST_NUM;
                if (page == 0) {
                    pileOrderList.clear();
                }
                page++;
                if (object != null && object.size() > 0) {
                    pileOrderList.addAll(object);
                }
                if (pileOrderList.size() > 0) {
                    tv_no_data.setVisibility(View.GONE);
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                }
                orderAdapter.notifyDataSetChanged();
                mRefreshLayout.setIsHasLoadedAll(isHasLoadedAll);
            }

            @Override
            public void onError(int code, int from, String error) {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
                tv_no_data.setVisibility(View.GONE);
                ToastUtil.makeText(error);
            }
        });
    }

    private void getChargeList(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("aid", SysModel.user.aid);
        values.put("page", String.valueOf(page));
        values.put("pageCount", String.valueOf(AppConfig.LIST_NUM));
        ApiRequest request = new ApiRequest(this, ApiList.GetChargeList.getUrl(), ApiList.GetChargeList.getMethod(), values);
        mRefreshLayout.setIsHasLoadedAll(true);
        request.showProgress(showProgress).callApi(new ReturnCallBack<List<ChargeOrder>>() {
            @Override
            public void onSuccess(int from, List<ChargeOrder> object) {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
                isHasLoadedAll = object != null && object.size() < AppConfig.LIST_NUM;
                if (page == 0) {
                    chargeOrderList.clear();
                }
                page++;
                if (object != null && object.size() > 0) {
                    chargeOrderList.addAll(object);
                }
                if (chargeOrderList.size() > 0) {
                    tv_no_data.setVisibility(View.GONE);
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                }
                orderAdapter.notifyDataSetChanged();
                mRefreshLayout.setIsHasLoadedAll(isHasLoadedAll);
            }

            @Override
            public void onError(int code, int from, String error) {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
                tv_no_data.setVisibility(View.GONE);
                ToastUtil.makeText(error);
            }
        });
//        chargeOrderList.clear();
//        ChargeOrder chargeOrder = new ChargeOrder();
//        chargeOrder.order_time = "2016-3-22 13:20:36";
//        chargeOrder.station_name = "测试电站3";
//        chargeOrder.pile_name = "1号桩";
//        chargeOrder.order_state = "0";
//        chargeOrderList.add(chargeOrder);
//        chargeOrder = new ChargeOrder();
//        chargeOrder.order_time = "2016-3-21 23:10:21";
//        chargeOrder.station_name = "测试电站1";
//        chargeOrder.pile_name = "2号桩";
//        chargeOrder.finish_time = "2016-3-22 06:54:52";
//        chargeOrder.order_state = "1";
//        chargeOrderList.add(chargeOrder);
//        orderAdapter.notifyDataSetChanged();
    }

    private void getRescueList(boolean showProgress) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        values.put("aid", SysModel.user.aid);
        values.put("page", String.valueOf(page));
        values.put("pageCount", String.valueOf(AppConfig.LIST_NUM));
        ApiRequest request = new ApiRequest(this, ApiList.GetRescueList.getUrl(), ApiList.GetRescueList.getMethod(), values);
        mRefreshLayout.setIsHasLoadedAll(true);
        request.showProgress(showProgress).callApi(new ReturnCallBack<List<RescueOrder>>() {
            @Override
            public void onSuccess(int from, List<RescueOrder> object) {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
                isHasLoadedAll = object != null && object.size() < AppConfig.LIST_NUM;
                if (page == 0) {
                    rescueOrderList.clear();
                }
                page++;
                if (object != null && object.size() > 0) {
                    rescueOrderList.addAll(object);
                }
                if (rescueOrderList.size() > 0) {
                    tv_no_data.setVisibility(View.GONE);
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                }
                orderAdapter.notifyDataSetChanged();
                mRefreshLayout.setIsHasLoadedAll(isHasLoadedAll);
            }

            @Override
            public void onError(int code, int from, String error) {
                mRefreshLayout.setLoading(false);
                mRefreshLayout.setRefreshing(false);
                tv_no_data.setVisibility(View.GONE);
                ToastUtil.makeText(error);
            }
        });
//        rescueOrderList.clear();
//        RescueOrder rescueOrder = new RescueOrder();
//        rescueOrder.order_time = "2016-3-21 23:10:21";
//        rescueOrder.order_address = "杨浦区五角场";
//        rescueOrder.order_state = "3";
//        rescueOrder.order_id = "1";
//        rescueOrderList.add(rescueOrder);
//        orderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ORDER_DETAIL:
                    page = 0;
                    getOrderList(false);
                    break;
            }
        }
    }
}
