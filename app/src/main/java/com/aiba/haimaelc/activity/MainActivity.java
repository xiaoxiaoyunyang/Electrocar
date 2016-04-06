package com.aiba.haimaelc.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.SysModel;
import com.aiba.haimaelc.adapter.MyViewPagerAdapter;
import com.aiba.haimaelc.fragment.CarDoorFragment;
import com.aiba.haimaelc.fragment.CarTireFrament;
import com.aiba.haimaelc.fragment.SlideMenuLeftFragment;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.PhoneSaveUtils;
import com.aiba.haimaelc.tools.StatusBarUtil;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.CircleGradientProgressView;
import com.aiba.haimaelc.widget.HoloCircleRoundProgressView;
import com.aiba.haimaelc.widget.LinearGradientAreaLineChart;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends SlidingFragmentActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener, LinearGradientAreaLineChart.IRenderTip {

    public static final String TAG = "MainActivity";
    public static final int EXAMATION_ANIMATION_DURATION = 4 * 5 * 1000;
    public static final int ELC_ANIMATION_DURATION = 2 * 1000;
    private int oldOilNum = 100;
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    private int progressWidth = 0;

    //车辆状态、油量、续航里程
    private CircleGradientProgressView dumpProgress;
    private TextView dumpEnergy, dumpPercent;
    private TextView endurance;//续航里程
    private TextView battery; //剩余电量
    private ProgressBar batteryProgress;//剩余电量进度条
    private LinearLayout batteryView;
    private TextView charge;//充电
    private ObjectAnimator dumpAnimator;

    //车辆体检
    private TextView startExam;
    private HoloCircleRoundProgressView elcProgress, powerProgress, stableProgress, brakeProgress;
    private TextView elcStatus, powerStatus, stableStatus, brakeStatus;
    private TextView elcDesc, powerDesc, stableDesc, brakeDesc;
    private ImageView elcImage, powerImage, stableImage, brakeImage;

    private ObjectAnimator elcAnimator, powerAnimator, stableAnimator, brakeAnimator;
    private AnimatorSet animatorSet;

    //车门和车轮胎
    private List<Fragment> fragmentList;
    private ViewPager myViewPager;
    private FragmentManager fragmentManager;
    private MyViewPagerAdapter mAdapter;
    private ImageView point1, point2;
    private ImageView carStatusImg;
    private TextView carStatus;
    private ImageView headImage;
    private int currentIndex = 0;

    //侧滑菜单
    private SlidingMenu leftMenu;

    //功耗清单
    private TextView mAverage;
    private TextView pointNum;
    private LinearGradientAreaLineChart areaChart;
    private LinkedList<String> xLables; //x轴坐标
    private LinkedList<Double> dataSeries;
    private double mAverageValue = 324;
    private LinearLayout pointTip; //功耗清单折线图峰值

    //今日统计
    private TextView statistics;
    private boolean flag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        progressWidth = screenWidth / 5;
        initView();
        initMenu();
        if (leftMenu.isMenuShowing()) {
            leftMenu.showContent();
        }
        oldOilNum = PhoneSaveUtils.getInt(AppConst.DUMP_ElECTRIC, 100);//获取随即剩余电量
        initValue();
        new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    @Override
    protected void setStatusBar() {
//        StatusBarUtil.setTransparent(this);
    }

    private void initView() {
        findViewById(R.id.message).setOnClickListener(this);//消息中心
        startExam = (TextView) findViewById(R.id.start_exam);
        startExam.setOnClickListener(this);
        //电力系统
        elcProgress = (HoloCircleRoundProgressView) findViewById(R.id.elc_progress);
        elcStatus = (TextView) findViewById(R.id.elc_status);
        elcDesc = (TextView) findViewById(R.id.elc_desc);
        elcImage = (ImageView) findViewById(R.id.elc_image);
        //动力系统
        powerProgress = (HoloCircleRoundProgressView) findViewById(R.id.power_progress);
        powerStatus = (TextView) findViewById(R.id.power_status);
        powerDesc = (TextView) findViewById(R.id.power_desc);
        powerImage = (ImageView) findViewById(R.id.power_image);
        //稳定系统
        stableProgress = (HoloCircleRoundProgressView) findViewById(R.id.stable_progress);
        stableStatus = (TextView) findViewById(R.id.stable_status);
        stableDesc = (TextView) findViewById(R.id.stable_desc);
        stableImage = (ImageView) findViewById(R.id.stable_image);
        //刹车系统
        brakeProgress = (HoloCircleRoundProgressView) findViewById(R.id.brake_progress);
        brakeStatus = (TextView) findViewById(R.id.brake_status);
        brakeDesc = (TextView) findViewById(R.id.brake_desc);
        brakeImage = (ImageView) findViewById(R.id.brake_image);

        //剩余油量
        dumpProgress = (CircleGradientProgressView) findViewById(R.id.dump_progress);
        dumpEnergy = (TextView) findViewById(R.id.dump_energy);
        dumpPercent = (TextView) findViewById(R.id.dump_percent);
        endurance = (TextView) findViewById(R.id.endurance);
        battery = (TextView) findViewById(R.id.battery_txt);
        batteryProgress = (ProgressBar) findViewById(R.id.battery_progress);
        batteryView = (LinearLayout) findViewById(R.id.battery_view);
        charge = (TextView) findViewById(R.id.go_charge);
        charge.setOnClickListener(this);

        //车门和车胎
        myViewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new CarTireFrament());
        fragmentList.add(new CarDoorFragment());
        mAdapter = new MyViewPagerAdapter(fragmentManager, fragmentList);
        myViewPager.setAdapter(mAdapter);
        myViewPager.addOnPageChangeListener(this);
        myViewPager.setCurrentItem(0);
        currentIndex = 0;

        point1 = (ImageView) findViewById(R.id.ponit1);
        point2 = (ImageView) findViewById(R.id.ponit2);
        changeCurrentIndexStatus(currentIndex);
        carStatusImg = (ImageView) findViewById(R.id.car_status_img);
        carStatusImg.setEnabled(false);
        carStatus = (TextView) findViewById(R.id.car_status_txt);

        headImage = (ImageView) findViewById(R.id.head);

        mAverage = (TextView) findViewById(R.id.average);
        pointNum = (TextView) findViewById(R.id.power_num);
        areaChart = (LinearGradientAreaLineChart) findViewById(R.id.power_dissipation);
        areaChart.setRenderTip(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth - DensityUtil.dip2px(this, 32), screenHeight / 3);
        params.gravity = Gravity.CENTER;
        areaChart.setLayoutParams(params);
        pointTip = (LinearLayout) findViewById(R.id.point_tip);

        statistics = (TextView) findViewById(R.id.statistics);
        statistics.setOnClickListener(this);
    }

    private void initValue() {
        if (oldOilNum <= 20) {
            flag = false;
        } else {
            flag = true;
        }
        doChangeElectricColor(flag);
        batteryProgress.setProgress(oldOilNum);
        dumpProgress.setPercent(oldOilNum);
        dumpEnergy.setText(oldOilNum + "");
        battery.setText(oldOilNum + "");
        endurance.setText(oldOilNum + "km");

        xLables = new LinkedList<>();
        xLables.add("25km");
        xLables.add("20");
        xLables.add("15");
        xLables.add("10");
        xLables.add("5");
        xLables.add("0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateAreaChart();
    }

    private void initMenu() {
        Fragment leftMenuFragment = new SlideMenuLeftFragment();
        setBehindContentView(R.layout.left_menu_frame);
        fragmentManager.beginTransaction().replace(R.id.id_left_menu_frame, leftMenuFragment).commit();
        leftMenu = getSlidingMenu();
        //设置侧滑菜单属性
        leftMenu.setMode(SlidingMenu.LEFT);
        //设置触摸屏幕的模式
        leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置侧滑菜单的偏移量
        leftMenu.setBehindOffset(screenWidth / 7);
        //设置渐入渐出效果的值
        leftMenu.setFadeDegree(0.35f);
    }

    private void startExam() {
        if (checkAnimatorIsRuning()) {
            ToastUtil.makeLongText("正在体检中...");
            return;
        }
        startExam.setText("正在体检");
        doPrepareForAnimation();
        final int random = getRandomNum();
        elcAnimator = ObjectAnimator.ofFloat(elcProgress, "percent", 0, 500).setDuration(EXAMATION_ANIMATION_DURATION);
        elcAnimator.setInterpolator(new LinearInterpolator());
        elcAnimator.setRepeatCount(2
        );
        elcAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                elcProgress.setStatus(HoloCircleRoundProgressView.Status.END);
                elcProgress.setIsNormal(random != 1);
                elcStatus.setText(random != 1 ? "健康" : "不健康");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                elcStatus.setText("正在扫描");
                powerStatus.setText("等待扫描");
                stableStatus.setText("等待扫描");
                brakeStatus.setText("等待扫描");
                elcProgress.setStatus(HoloCircleRoundProgressView.Status.RUNING);
            }
        });
        powerAnimator = ObjectAnimator.ofFloat(powerProgress, "percent", 0, 500).setDuration(EXAMATION_ANIMATION_DURATION);
        powerAnimator.setInterpolator(new LinearInterpolator());
        powerAnimator.setRepeatCount(2);
        powerAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                powerProgress.setStatus(HoloCircleRoundProgressView.Status.END);
                powerProgress.setIsNormal(random != 2);
                powerStatus.setText(random != 2 ? "健康" : "不健康");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                powerStatus.setText("正在扫描");
                powerProgress.setStatus(HoloCircleRoundProgressView.Status.RUNING);
            }
        });
        stableAnimator = ObjectAnimator.ofFloat(stableProgress, "percent", 0, 500).setDuration(EXAMATION_ANIMATION_DURATION);
        stableAnimator.setInterpolator(new LinearInterpolator());
        stableAnimator.setRepeatCount(2);
        stableAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                stableProgress.setStatus(HoloCircleRoundProgressView.Status.END);
                stableProgress.setIsNormal(random != 3);
                stableStatus.setText(random != 3 ? "健康" : "不健康");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                stableStatus.setText("正在扫描");
                stableProgress.setStatus(HoloCircleRoundProgressView.Status.RUNING);
            }
        });
        brakeAnimator = ObjectAnimator.ofFloat(brakeProgress, "percent", 0, 500).setDuration(EXAMATION_ANIMATION_DURATION);
        brakeAnimator.setInterpolator(new LinearInterpolator());
        brakeAnimator.setRepeatCount(2);
        brakeAnimator.addListener(new AnimatorListenerAdapter() {
            //            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                brakeProgress.setStatus(HoloCircleRoundProgressView.Status.END);
                brakeProgress.setIsNormal(random != 4);
                brakeStatus.setText(random != 4 ? "健康" : "不健康");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                brakeStatus.setText("正在扫描");
                brakeProgress.setStatus(HoloCircleRoundProgressView.Status.RUNING);
            }
        });
        animatorSet = new AnimatorSet();
        animatorSet.play(elcAnimator).before(powerAnimator);
        animatorSet.play(powerAnimator).before(stableAnimator);
        animatorSet.play(stableAnimator).before(brakeAnimator);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startExam.setText("开始体检");
            }
        });
    }

    public void doChargeClick(View view) {
        Intent intent = new Intent(MainActivity.this, ChargeStationMapActivity.class);
        startActivity(intent);
    }

    private void doPrepareForAnimation() {
        elcProgress.setPercent(0);
        powerProgress.setPercent(0);
        stableProgress.setPercent(0);
        brakeProgress.setPercent(0);
    }

    private boolean checkAnimatorIsRuning() {
        if (elcAnimator == null || powerAnimator == null ||
                stableAnimator == null || brakeAnimator == null) {
            return false;
        }
        return elcAnimator.isRunning() || powerAnimator.isRunning() ||
                stableAnimator.isRunning() || brakeAnimator.isRunning();
    }

    private void doChangeElectricColor(boolean flag) {
        int color = getResources().getColor(flag ? R.color.main_txt_green : R.color.main_txt_red);
        Drawable chargeDrawable = getResources().getDrawable(flag ? R.mipmap.go : R.mipmap.goto_red);
        int batterViewDrawableId = flag ? R.mipmap.battery_green : R.mipmap.battery_red;
        int batterProgressDrawbleId = flag ? R.drawable.elc_progressbar_green_bg : R.drawable.elc_progressbar_red_bg;
        charge.setCompoundDrawablesWithIntrinsicBounds(null, null, chargeDrawable, null);
        charge.setTextColor(color);
        batteryView.setBackgroundResource(batterViewDrawableId);
        batteryProgress.setProgressDrawable(getResources().getDrawable(batterProgressDrawbleId));
        dumpEnergy.setTextColor(color);
        dumpPercent.setTextColor(color);
    }

    /**
     * 产生0-4随机数
     *
     * @return
     */
    private int getRandomNum() {
        return (int) (Math.random() * 5);
    }

    /**
     * 剩余电量
     *
     * @return
     */
    private int getOilNum() {
        return (int) (Math.random() * 100 + 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position < 0 || position > 1 || currentIndex == position) {
            return;
        }
        currentIndex = position;
        if (position == 0) {
            point1.setEnabled(true);
            point2.setEnabled(false);
            carStatus.setText("轮胎气压低");
            carStatusImg.setEnabled(false);
        } else {
            point1.setEnabled(false);
            point2.setEnabled(true);
            carStatus.setText("车门正常");
            carStatusImg.setEnabled(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeCurrentIndexStatus(int currentIndex) {
        if (currentIndex == 0) {
            point1.setEnabled(true);
            point2.setEnabled(false);
        } else {
            point1.setEnabled(false);
            point2.setEnabled(true);
        }
    }

    public void showLeftMenu(View view) {
        leftMenu.showMenu();
    }

    private void invalidateAreaChart() {
        areaChart.initView();
        areaChart.addXLables(xLables);
//        double[] testData = {140, 210, 132, 253, 190, 380, 260, 382, 377, 467, 279, 185, 83, 390,0};
        dataSeries = new LinkedList<>();
        dataSeries.add(140d);
        dataSeries.add(210d);
        dataSeries.add(132d);
        dataSeries.add(253d);
        dataSeries.add(190d);
        dataSeries.add(380d);
        dataSeries.add(382d);
        dataSeries.add(377d);
        dataSeries.add(467d);
        dataSeries.add(279d);
        dataSeries.add(185d);
        dataSeries.add(83d);
        dataSeries.add(390d);
        dataSeries.add(0d);
        AreaData lineData = new AreaData("功耗", dataSeries, Color.rgb(97, 241, 255), Color.rgb(90, 248, 99), Color.rgb(238, 113, 101),
                new int[]{Color.argb(255, 241, 16, 17), Color.argb(225, 185, 236, 156), Color.argb(15, 185, 236, 156)}, null);
        //不显示点
        lineData.setDotStyle(XEnum.DotStyle.DOT);
        lineData.getDotPaint().setAlpha(0);
        lineData.getLinePaint().setStrokeWidth((int) getResources().getDimension(R.dimen.customLine_stroke));
        areaChart.charDataSet(dataSeries.size(), lineData);
        areaChart.setDataMax(500);
        areaChart.setDataStep(100);
        CustomLineData mAverageLine = new CustomLineData("", mAverageValue, Color.rgb(229, 229, 229), 8);
        mAverageLine.setLabelHorizontalPostion(Paint.Align.LEFT);
        mAverageLine.setLineStyle(XEnum.LineStyle.DASH);
        mAverageLine.setLineStroke((int) getResources().getDimension(R.dimen.customLine_stroke));
        mAverageLine.getCustomLinePaint().setPathEffect(new DashPathEffect(new float[]{15, 15, 15, 15}, 1));
        areaChart.addCustomLineData(mAverageLine);
    }

    /**
     * 捕捉返回键，如果当前显示菜单，刚隐藏
     */
    @Override
    public void onBackPressed() {
        if (leftMenu.isMenuShowing()) {
            leftMenu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.message:
                intent = new Intent(MainActivity.this, MessageCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.go_charge:
                doChargeClick(v);
                break;
            case R.id.start_exam:
                startExam();
                break;
            case R.id.statistics:
                intent = new Intent(MainActivity.this, TodayStatisticsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void drawTip(float x, float y, double value) {
        if (x == 0 && y == 0) {
            pointTip.setVisibility(View.GONE);
            return;
        }
        LogUtils.logE(getResources().getDimension(R.dimen.point_tip_offset_x) + "");
        pointTip.setX(x - getResources().getDimension(R.dimen.point_tip_offset_x));
        pointTip.setY(y - pointTip.getHeight() - getResources().getDimension(R.dimen.point_tip_offset_y) + areaChart.getTop());
        pointNum.setText(value + "wh/km");
        pointTip.setVisibility(View.VISIBLE);
    }


    class MyTask extends AsyncTask<Object, Void, Boolean> {

        private int oilNum;

        @Override
        protected Boolean doInBackground(Object... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    oilNum = getOilNum();
                    try {
                        Thread.sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }).start();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            PhoneSaveUtils.putInt(AppConst.DUMP_ElECTRIC, oilNum);
            if (oilNum <= 20) {
                flag = false;
            } else {
                flag = true;
            }
            doChangeElectricColor(flag);
            batteryProgress.setProgress(oilNum);
            battery.setText(oilNum + "%");
            endurance.setText(oilNum * 3 + "Km");
            // TODO: 2016/4/5
            SysModel.carGps.endurance = String.valueOf(oilNum * 3);
            SysModel.carGps.dumpEnergy = String.valueOf(oilNum);

            dumpAnimator = ObjectAnimator.ofFloat(dumpProgress, "percent", oldOilNum, oilNum);
            dumpAnimator.setDuration(ELC_ANIMATION_DURATION);
            dumpAnimator.start();
            dumpAnimator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    int color = 0;
                    if (percent <= 20) {
                        color = getResources().getColor(R.color.main_txt_red);
                    } else {
                        color = getResources().getColor(R.color.main_txt_green);
                    }
                    dumpEnergy.setTextColor(color);
                    dumpPercent.setTextColor(color);
                    dumpEnergy.setText((int) percent + "");
                }
            });
        }
    }
}