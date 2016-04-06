package com.aiba.haimaelc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConfig;
import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.AddressAdapter;
import com.aiba.haimaelc.model.Address;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.RefreshLayout;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchMapActivity extends BaseActivity implements View.OnClickListener, TextWatcher,
        PoiSearch.OnPoiSearchListener, AdapterView.OnItemClickListener {

    private View delete;
    private AutoCompleteTextView destination;
    private PoiSearch.Query query;
    private List<Address> addressList;
    private AddressAdapter addressAdapter;
    private String cityName = "";
    private String searchKey = "";
    private Address address;
    private ListView listView;
    private RefreshLayout mRefreshLayout;
    private boolean isHasLoadedAll = true;
    private boolean isFromOrderRescue;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        Intent intent = getIntent();
        if (intent != null) {
            cityName = intent.getStringExtra(AppConst.CITY_NAME);
            isFromOrderRescue = intent.getBooleanExtra(AppConst.FROM_ORDER_RESCUE, false);
        }
        initView();
        showKeyboard(destination);
    }

    private void initView() {
        setTitleBarBack();
        if (isFromOrderRescue) {
            setTitleText("选择位置");
            doPoiSearch(searchKey, "", cityName);
        } else {
            findViewById(R.id.tv_title).setVisibility(View.GONE);
            findViewById(R.id.et_title).setVisibility(View.VISIBLE);
            delete = findViewById(R.id.title_bar_search_delete);
            delete.setOnClickListener(this);
            destination = (AutoCompleteTextView) findViewById(R.id.destination);
            destination.addTextChangedListener(this);
            destination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH && SearchMapActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) destination.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(SearchMapActivity.this.getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                        return true;
                    }
                    return false;
                }
            });
        }
        initListView();
    }

    private void initListView() {
        addressList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.refresh_list_view);
        listView.setOnItemClickListener(this);
        addressAdapter = new AddressAdapter(this, addressList, R.layout.item_address);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rl_service_refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                isHasLoadedAll = true;
                doPoiSearch(searchKey, "", cityName);
            }
        });
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                doPoiSearch(searchKey, "", cityName);
            }
        });
        mRefreshLayout.setListView(listView, addressAdapter);
    }

    private void showKeyboard(final View view) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (view != null) {
                    SearchMapActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置可获得焦点
                            view.setFocusable(true);
                            view.setFocusableInTouchMode(true);
                            //请求获得焦点
                            view.requestFocus();
                            //调用系统输入法
                            InputMethodManager inputManager = (InputMethodManager) view
                                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(view, 0);
                        }
                    });
                }
            }
        }, 200);
    }

    private void doPoiSearch(String key, String ctgr, String city) {
//        this.key = key;
//        this.city = city;
//        this.ctgr = ctgr;
        query = new PoiSearch.Query(key, ctgr, city);
        // keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
        // 共分为以下20种：汽车服务|汽车销售|
        // 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        // 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        // 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        // cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
        query.setPageSize(AppConfig.LIST_NUM);// 设置每页最多返回多少条poiitem
        query.setPageNum(page);// 设置查第一页
        PoiSearch poiSearch = new PoiSearch(this, query);
        if (TextUtils.isEmpty(key)) {
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Double.valueOf(SysModel.carGps.latitude),
                    Double.valueOf(SysModel.carGps.longitude)), 500));// 设置周边搜索的中心点以及区域
        }
        poiSearch.setOnPoiSearchListener(this);// 设置数据返回的监听器
        poiSearch.searchPOIAsyn();// 开始搜索
    }

    private void setSearchResult() {
        Intent intent = new Intent();
        intent.putExtra(AppConst.SELECT_ADDRESS, address);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        mRefreshLayout.setLoading(false);
        mRefreshLayout.setRefreshing(false);
        if (i != 1000) {
            return;
        }
        if (page == 0) {
            addressList.clear();
        }
        page++;
        if (poiResult.getQuery().equals(query)) {// 是否是同一条
            ArrayList<PoiItem> poiItems = poiResult.getPois();
            isHasLoadedAll = poiItems != null && poiItems.size() < AppConfig.LIST_NUM;
            List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
            Address address;
            if (poiItems != null && poiItems.size() > 0) {
                for (PoiItem item : poiItems) {
                    address = new Address();
                    address.name = item.getTitle();
                    address.address = item.getCityName() + item.getAdName() + item.getSnippet();
                    address.latitude = item.getLatLonPoint().getLatitude();
                    address.longitude = item.getLatLonPoint().getLongitude();
                    addressList.add(address);
                }
            } else if (suggestionCities != null && suggestionCities.size() > 0) {
                ToastUtil.makeLongText("请在搜索关键字前添加城市名\n例如\"" + suggestionCities.get(0).getCityName() + "\"");
            }
            mRefreshLayout.setIsHasLoadedAll(isHasLoadedAll);
            addressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s.toString().trim())) {
            delete.setVisibility(View.GONE);
            addressList.clear();
            addressAdapter.notifyDataSetChanged();
        } else {
            delete.setVisibility(View.VISIBLE);
            searchKey = s.toString().trim();
            page = 0;
            isHasLoadedAll = true;
            doPoiSearch(searchKey, "", cityName);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_search_delete:
                destination.setText("");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addressList.get(position).selected = true;
        addressAdapter.notifyDataSetChanged();
        address = addressList.get(position);
        setSearchResult();
    }
}
