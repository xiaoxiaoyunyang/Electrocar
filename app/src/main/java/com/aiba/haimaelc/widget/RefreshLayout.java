package com.aiba.haimaelc.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.aiba.haimaelc.R;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 *
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout implements OnScrollListener {

    private int mTouchSlop; //滑动到最下面时的上拉操作
    private ListView mListView; //listview实例
    private OnLoadListener mOnLoadListener; //上拉监听器, 到了最底部的上拉加载操作
    private View mListViewFooter; //ListView的加载中footer
    private int mYDown; //按下时的y坐标
    private int mLastY; //抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
    private boolean isLoading = false; //是否在加载中 ( 上拉加载更多 )
    private boolean isHasLoadedAll = false;
    private int positionToTop = 10; //显示totop按钮所需位置
    private View footerContentView;

    public void setPositionToTop(int positionToTop) {
        this.positionToTop = positionToTop;
    }

    public void setIsHasLoadedAll(boolean isHasLoadedAll) {
        this.isHasLoadedAll = isHasLoadedAll;
    }

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mListViewFooter = LayoutInflater.from(context).inflate(
                R.layout.layout_list_footer, null, false);
        footerContentView = mListViewFooter.findViewById(R.id.pull_to_refresh_load_progress);
        this.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    //获取ListView对象
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
                Log.d(VIEW_LOG_TAG, "### 找到listview");
            }
        }
    }

    //getListView()为空时需要主动设置listView
    public void setListView(ListView listView, BaseAdapter adapter) {
        mListView = listView;
        mListView.setOnScrollListener(this);
        mListView.addFooterView(mListViewFooter);
        mListView.setFooterDividersEnabled(true);
        mListView.setAdapter(adapter);
        mListView.removeFooterView(mListViewFooter);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    //是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp() && !isHasLoadedAll;
    }

    //判断是否到了最底部
    private boolean isBottom() {
        return mListView != null && mListView.getAdapter() != null
                && mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
    }

    //是否是上拉操作
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    //如果到了最底部,而且是上拉操作.那么执行onLoad方法
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (buttonView != null) {
            if (firstVisibleItem > positionToTop) {
                // TODO: 2015/12/4 隐藏回到顶部箭头
                buttonView.setVisibility(View.GONE);
            } else {
                buttonView.setVisibility(View.GONE);
            }
        }
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public interface OnLoadListener {
        void onLoad();
    }

    private View buttonView;

    public void setButton(View view) {
        buttonView = view;
    }
}