package com.aiba.haimaelc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.SortCityAdapter;
import com.aiba.haimaelc.model.SortCity;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.Dictionary;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.widget.MySideBar;
import com.aiba.haimaelc.widget.PinnedHeaderListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectCityActivity extends BaseActivity implements OnClickListener {

    private PinnedHeaderListView sortListView;
    private SortCityAdapter myAdapter;
    private EditText city_search_edit;
    private Map<String, List<SortCity>> map;
    private List<String> sections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initView();
        if (Dictionary.sortCityList.size() == 0) {
            // TODO: 2016/3/23 获取城市列表
            Dictionary.getSortCityList();
            createMap(Dictionary.sortCityList, true);
            myAdapter.notifyDataSetChanged();
        } else {
            createMap(Dictionary.sortCityList, true);
            myAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("选择地区");
        findViewById(R.id.city_search_delete).setOnClickListener(this);
        sortListView = (PinnedHeaderListView) findViewById(R.id.city_list);
        sortListView.setHeaderDividersEnabled(false);
        // 设置右侧触摸监听
        ((MySideBar) findViewById(R.id.sidebar)).setOnTouchingLetterChangedListener(new MySideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int index = sections.indexOf(s);
                if (index == -1) {
                    return;
                } else if (index == 1) {
                    sortListView.setSelection(0);
                    return;
                }
                int position = sortListView.getHeaderViewsCount();
                for (int i = 0; i < index; i++) {
                    position += map.get(sections.get(i)).size() + 1;
                }
                sortListView.setSelection(position);
            }
        });
        map = new LinkedHashMap<>();
        sections = new ArrayList<>();
        myAdapter = new SortCityAdapter(this, sections, map);
        sortListView.setAdapter(myAdapter);
        sortListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(AppConst.SELECT_CITY, map.get(sections.get(section)).get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        city_search_edit = (EditText) findViewById(R.id.city_search_edit);
        city_search_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    createMap(Dictionary.sortCityList, true);
                    myAdapter.notifyDataSetChanged();
                } else {
                    filterData(s.toString());
                }
            }
        });
        city_search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && SelectCityActivity.this.getCurrentFocus() != null) {
                    ((InputMethodManager) city_search_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SelectCityActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        LogUtils.logE("点击");
        switch (v.getId()) {
            case R.id.city_search_delete:
                LogUtils.logE("删除");
                city_search_edit.setText("");
                InputMethodManager imm = (InputMethodManager) (getSystemService(Context.INPUT_METHOD_SERVICE));
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    private void createMap(List<SortCity> list, boolean hasHot) {
        map.clear();
        sections.clear();
        if (hasHot) {
            List<SortCity> sortCityList = new ArrayList<>();
            sortCityList.add(SysModel.currentCity);
            map.put(getResources().getString(R.string.location_city), sortCityList);
            sections.add(getResources().getString(R.string.location_city));
            map.put(getResources().getString(R.string.hot_city), Dictionary.getHotCityList());
            sections.add(getResources().getString(R.string.hot_city));
        }
        if (list.size() == 0) {
            return;
        }
        List<SortCity> mList = new ArrayList<>();
        String section = list.get(0).getCityKey().substring(0, 1);
        int cityCounts = list.size();
        for (int i = 0; i < cityCounts; i++) {
            if (list.get(i).getCityKey().substring(0, 1).equals(section)) {
                mList.add(list.get(i));
            } else {
                map.put(section, mList);
                sections.add(section);
                section = list.get(i).getCityKey().substring(0, 1);
                mList = new ArrayList<>();
                mList.add(list.get(i));
            }
        }
        map.put(section, mList);
        sections.add(section);
    }

//    private class MyTask extends AsyncTask<Integer, Void, Boolean> {
//        ArrayList<SortCity> list;
//        private String ERROR = "请求失败";
//        private int type = 0;
//
//        @Override
//        protected Boolean doInBackground(Integer... params) {
//            switch (type = params[0]) {
//                case 0:
//                    try {
//                        list = AibaHttpApi.getCtiylist();
//                    } catch (ToastException e) {
//                        e.printStackTrace();
//                        ERROR = e.getMessage();
//                        return false;
//                    }
//                    break;
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            ProgressManager.closeProgress();
//            if (!result) {
//                if (!showReLogin()) {
//                    AiBaToast.makeText(ERROR);
//                }
//            } else {
//                switch (type) {
//                    case 0:
//                        if (list != null && list.size() > 0) {
//                            ChinsesCharComp sort = new ChinsesCharComp();
//                            Collections.sort(list, sort);
//                            AiBaDictionary.sortCityList.clear();
//                            AiBaDictionary.sortCityList.addAll(list);
//                        }
//                        createMap(AiBaDictionary.sortCityList);
//                        myAdapter.notifyDataSetChanged();
//                        break;
//                }
//            }
//        }
//    }

    private class ChinsesCharComp implements Comparator<SortCity> {

        public int compare(SortCity c1, SortCity c2) {
            Collator myCollator = Collator
                    .getInstance(java.util.Locale.CHINESE);
            if (myCollator.compare(c1.getCityKey(), c2.getCityKey()) < 0)
                return -1;
            else if (myCollator.compare(c1.getCityKey(), c2.getCityKey()) > 0)
                return 1;
            else
                return 0;
        }
    }

    private void filterData(String filterStr) {
        List<SortCity> list = new ArrayList<>();
        for (int i = 0; i < Dictionary.sortCityList.size(); i++) {
            if (Dictionary.sortCityList.get(i).getName().contains(filterStr)
                    || Dictionary.sortCityList.get(i).getCityKey().contains(CommonTools.Upper(filterStr))) {
                list.add(Dictionary.sortCityList.get(i));
            }
        }
        createMap(list, false);
        myAdapter.notifyDataSetChanged();
    }
}
