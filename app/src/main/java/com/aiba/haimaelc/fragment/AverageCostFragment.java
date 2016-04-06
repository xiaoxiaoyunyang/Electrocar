package com.aiba.haimaelc.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.MainActivity;
import com.aiba.haimaelc.adapter.AverageCostAdapter;
import com.aiba.haimaelc.model.AverageCost;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.PhoneSaveUtils;
import com.aiba.haimaelc.widget.CircleGradientProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 16/3/28.
 */
public class AverageCostFragment extends BaseFragment {

    private LinearLayout average;
    private CircleGradientProgressView rankProgress;
    private ObjectAnimator animator;
    private int percent = 100;
    private TextView rank;
    private TextView rankPercent;
    private AverageCostAdapter mAdapter;
    private ListView listView;
    private List<AverageCost> datas  = new ArrayList<>();
    private int screenWidth, screenHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_average_cost, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        screenHeight = CommonTools.getScreenHeight(getActivity());
        screenWidth = CommonTools.getScreenWidth(getActivity());
        average = (LinearLayout) view.findViewById(R.id.average);
        average.requestFocus();
        average.setFocusable(true);
        average.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, screenHeight / 3);
                params.gravity = Gravity.CENTER_VERTICAL;
                average.setLayoutParams(params);
                average.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(screenWidth * 7 / 16, screenWidth * 7 / 16);
        rankProgress = (CircleGradientProgressView) view.findViewById(R.id.ranking_progress);
        fParams.gravity = Gravity.CENTER;
        rankProgress.setLayoutParams(fParams);

        rankPercent = (TextView) view.findViewById(R.id.rank_percent);
        percent = PhoneSaveUtils.getInt(AppConst.DUMP_ElECTRIC, 100);
        rankProgress.setPercent(percent);
        rankPercent.setText(percent + "");

//
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, screenHeight / 3);
        rank = (TextView) view.findViewById(R.id.rank);
        params.width = screenWidth * 3 / 8;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        rank.setLayoutParams(params);
        getDatas();
        initView(view);
        CommonTools.setListViewHeightBasedOnChildren(listView);
    }

    private void initView(View view){
        listView = (ListView) view.findViewById(R.id.average_cost);
        listView.setFocusable(false);
        mAdapter = new AverageCostAdapter(getActivity(),datas,R.layout.item_average_cost);
        listView.setAdapter(mAdapter);
    }

    private void getDatas(){
        datas.clear();
        datas.add(new AverageCost("你瞅啥?","2.1度","2.8元"));
        datas.add(new AverageCost("瞅你咋了","4.6度","5.7元"));
        datas.add(new AverageCost("不行","9.8度","18.6元"));
        datas.add(new AverageCost("就瞅你了","2.1度","2.8元"));
        datas.add(new AverageCost("咋滴吧","13.6度","22.8元"));
        datas.add(new AverageCost("然后就被","78.6度","102.8元"));
        datas.add(new AverageCost("被销了","10.2度","20.3元"));
        datas.add(new AverageCost("肉丝,你别","2.1度","2.8元"));
        datas.add(new AverageCost("跳呗，海水","4.3度","5.2元"));
        datas.add(new AverageCost("可凉可凉了","18.9度","30.5元"));
        datas.add(new AverageCost("滚","20.1度","35.2元"));
        datas.add(new AverageCost("别跳呗","29.1度","48.3元"));
    }
}
