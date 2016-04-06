package com.aiba.haimaelc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.adapter.MyViewPagerAdapter;
import com.aiba.haimaelc.fragment.AverageCostFragment;
import com.aiba.haimaelc.fragment.WeekAverageExpenseFragment;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 16/3/24.
 */
public class TodayStatisticsActivity extends BaseActivity{

    private FragmentManager manager;
    private PagerSlidingTabStrip slidingTabStrip;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitleText("今日统计");
        initTabStrip();
    }

    private void initView(){

    }

    private void initTabStrip(){
        manager = getSupportFragmentManager();
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        fragmentList = new ArrayList<>();
        fragmentList.add(new WeekAverageExpenseFragment());
        fragmentList.add(new AverageCostFragment());
        List<String> titles  = new ArrayList<>();
        titles.add("周内平均能耗费用");
        titles.add("平均能耗费用排名");
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(manager,titles,fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        slidingTabStrip.setShouldExpand(true);//设置标签自动扩展，匹配屏幕宽度
        slidingTabStrip.setViewPager(mViewPager);
        slidingTabStrip.setTabBackground(R.color.transparent_background);
        slidingTabStrip.setDividerColorResource(R.color.line_gray);
        slidingTabStrip.setDividerPadding(CommonTools.dpToPx(this, 13));
        slidingTabStrip.setUnderlineHeight(CommonTools.dpToPx(this, 1));//设置标签栏下边间隔线高度，单位像素
        slidingTabStrip.setIndicatorHeight(CommonTools.dpToPx(this, 1));//设置Indicator高度，单位像素
        slidingTabStrip.setIndicatorColorResource(R.color.main_txt_green);
        slidingTabStrip.setScrollOffset(CommonTools.dpToPx(this, 5));
        slidingTabStrip.setTextColorResource(R.color.main_txt_black);
        slidingTabStrip.setTextSize((int)getResources().getDimension(R.dimen.text_size_normal));
    }
}
