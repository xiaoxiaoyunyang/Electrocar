package com.aiba.haimaelc.adapter;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * 只会显示三个item，其他的销毁，前一个，当前，下一个
 * @author zhu
 *
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> list;
	private FragmentManager mFragmentManager;
    private List<String> titles;
	public MyViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}
    public MyViewPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments){
        super(fm);
        this.titles = titles;
        this.list = fragments;
    }

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	 // 初始化每个页卡选项  
    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        return super.instantiateItem(arg0, arg1);  
    }  
      
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);  
    }  
    
    /*
     * 返回两种状态：（根据状态来判断是否需要调用ViewPager.dateSetChanged()）
     * 1、POSITION_UNCHANGED  什么都不做
     * 2、POSITION_NONE 调用destroyItem 来去掉对象，并设置为刷新状态，以便触发PagerAdapter.instantiateItem()
     * */

    @Override
    public int getItemPosition(Object object) {
    	return POSITION_NONE;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
    	return titles.get(position);
    }
}
