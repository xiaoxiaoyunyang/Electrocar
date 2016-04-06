package com.aiba.haimaelc.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aiba.haimaelc.tools.LogUtils;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.view.ChartView;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhu on 16/3/22.
 */
public class LinearGradientAreaLineChart extends ChartView {

    private String TAG = "LinearGradientLineChart";
    private AreaChart chart = new AreaChart();//填充区域
    //x轴数据集合
    private LinkedList<String> mXLables = new LinkedList<>();
    //x轴标签集合
    private LinkedList<String> mBgLables = new LinkedList<>();
    //数据集合
    private LinkedList<AreaData> mDataSet = new LinkedList<>();

    private AreaChart chartBg = new AreaChart();

    private Paint mPaintTooltips = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<CustomLineData> mCustomLineDataset = new LinkedList<>();
    private double mAverageValue = 266.2d;

    private float[] positions = {0.2f,0.8f,1.0f};
    private double dataMax = 500;//数据轴最大值
    private double dataStep = 100;//数据轴刻度间隔

    private IRenderTip renderTip;

    public LinearGradientAreaLineChart(Context context) {
        super(context);
//        initView();
    }

    public LinearGradientAreaLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initView();
    }

    public LinearGradientAreaLineChart(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
//        initView();
    }

    public void initView() {
//        addXLables();
//        charDataSet(testData);
        chartBgRender();
        chartRender();
        refresh();
    }

    public void refresh(){
        invalidate();
        requestLayout();
    }
    public void addXLables(LinkedList<String> mXLables) {
        mBgLables.clear();
        mBgLables.addAll(mXLables);
        chartBg.setCategories(mBgLables);
    }

    public void charDataSet(int length,AreaData lineData) {
        mXLables.clear();
        mDataSet.clear();
        for (int i = 0; i < length; i++) {
            mXLables.add(Integer.toString(i));
        }
        //设置折线显示的属性
        mDataSet.add(lineData);
        chart.setDataSource(mDataSet);
    }

    private void chartBgRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getCharBgDefaultSpadding();
            chartBg.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            //提高绘制性能
            chartBg.disableHighPrecision();
            //限制平滑模式
            chartBg.disablePanMode();
            //标签轴
            chartBg.setCategories(mBgLables);
            chartBg.getCategoryAxis().setTickLabelMargin(DensityUtil.dip2px(getContext(), 20));
            chartBg.setXTickMarksOffsetMargin(DensityUtil.dip2px(getContext(), 5));
            chartBg.getCategoryAxis().getTickLabelPaint().setColor(Color.rgb(155, 155, 155));
            chartBg.getCategoryAxis().getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 12));
            //设置折线类型
            chartBg.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);
            //设置数据轴最大值和刻度间隔
//            chartBg.getDataAxis().setAxisMax(dataMax);
//            chartBg.getDataAxis().setAxisSteps(dataStep);
            chartBg.getDataAxis().setTickLabelMargin(DensityUtil.dip2px(getContext(), 5));
            chartBg.getDataAxis().setHorizontalTickAlign(Paint.Align.LEFT);
//            chartBg.getDataAxis().getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 16));

            //网格
//            chartBg.getPlotGrid().showHorizontalLines();
//            chartBg.getPlotGrid().showVerticalLines();
//            chartBg.getPlotGrid().setHorizontalLineStyle(XEnum.LineStyle.DOT);
//            chartBg.getPlotGrid().setVerticalLineStyle(XEnum.LineStyle.DOT);

            //隐藏轴线和刻度线隐藏
            chartBg.getDataAxis().hideAxisLine();//隐藏轴线
            chartBg.getDataAxis().hideTickMarks();//隐藏轴刻度线
            chartBg.getCategoryAxis().hideAxisLine();
            chartBg.getCategoryAxis().hideTickMarks();
            //设置轴标签画笔颜色
            chartBg.getDataAxis().getTickLabelPaint().setColor(Color.rgb(155, 155, 155));
            chartBg.getDataAxis().getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 12));

            //定义数据轴标签显示格式
            chartBg.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);


                }

            });

            chartBg.getPlotLegend().hide();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.logE(e.getMessage());
        }
    }

    public void setDataMax(double dataMax) {
        this.dataMax = dataMax;
        chartBg.getDataAxis().setAxisMax(dataMax);
        chart.getDataAxis().setAxisMax(dataMax);
    }

    public double getDataMax() {
        return dataMax;
    }

    public void setDataStep(double dataStep) {
        this.dataStep = dataStep;
        chartBg.getDataAxis().setAxisSteps(dataStep);
        chart.getDataAxis().setAxisSteps(dataStep);
    }

    public double getDataStep() {
        return dataStep;
    }

    public float[] getPositions() {
        return positions;
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getChartDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            chart.disableHighPrecision();
            chart.disablePanMode();

            //标签轴
            chart.setCategories(mXLables);
            //数据轴
            chart.setDataSource(mDataSet);
//            chart.getDataAxis().getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 16));
            chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);

//            chart.getDataAxis().setAxisSteps(dataStep);
//            chart.getDataAxis().setAxisMax(dataMax);

            //网格 默认隐藏
            chart.getPlotGrid().hideHorizontalLines();
            chart.getPlotGrid().hideVerticalLines();

            //把轴线和刻度线给隐藏
            chart.getDataAxis().hideTickMarks();
            chart.getDataAxis().hideAxisLine();
            chart.getCategoryAxis().hideAxisLine();
            chart.getCategoryAxis().hideTickMarks();
            chart.getDataAxis().hide();
            chart.getCategoryAxis().hide();

            chart.getAreaFillPaint().setAlpha(254);
            chart.setAreaAlpha(254);

            //激活点击监听
            chart.ActiveListenItemClick();
            //为了让触发更灵敏，可以扩大15px的点击监听范围
            chart.extPointClickRange(15);
            chart.showClikedFocus();

//            CustomLineData mAverageLine = new CustomLineData("", mAverageValue, Color.rgb(229, 229, 229), 8);
//            mAverageLine.setLabelHorizontalPostion(Paint.Align.LEFT);
//            mAverageLine.setLineStyle(XEnum.LineStyle.DASH);
//            mAverageLine.setLineStroke(5);
//            mAverageLine.getCustomLinePaint().setPathEffect(new DashPathEffect(new float[]{10,10,15,20},1));

//            mCustomLineDataset.add(mAverageLine);

            chart.setCustomLines(mCustomLineDataset);
            chart.getPlotLegend().hide();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.logE(e.getMessage());
        }

    }

    public void addCustomLineData(CustomLineData lineData){
        mCustomLineDataset.add(lineData);
    }

    public void showGridHorizontalLines(XEnum.LineStyle style,PathEffect pathEffect){
        chartBg.getPlotGrid().showHorizontalLines();
        chartBg.getPlotGrid().setHorizontalLineStyle(style);
        chartBg.getPlotGrid().getHorizontalLinePaint().setPathEffect(pathEffect);
    }

    public void showGridVerticalLines(XEnum.LineStyle style,PathEffect pathEffect){
        chartBg.getPlotGrid().showVerticalLines();
        chartBg.getPlotGrid().setVerticalLineStyle(style);
        chartBg.getPlotGrid().getVerticalLinePaint().setPathEffect(pathEffect);
    }

    //显示轴线
    public void showAxisLines(){
        chartBg.getDataAxis().showAxisLine();
        chartBg.getCategoryAxis().showAxisLine();
    }
    //显示刻度线
    public void showAxisMarks(){
        chartBg.getDataAxis().showTickMarks();
        chartBg.getCategoryAxis().showTickMarks();
    }

    public AreaChart getChart() {
        return chart;
    }

    public AreaChart getChartBg() {
        return chartBg;
    }

    public void setChart(AreaChart chart) {
        this.chart = chart;
        refresh();
    }

    public void setChartBg(AreaChart chartBg) {
        this.chartBg = chartBg;
        refresh();
    }

    private int[] getCharBgDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 25); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 20); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 12); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 50); //bottom
        return ltrb;
    }

    private int[] getChartDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 25); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 20); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 15); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 50); //bottom
        return ltrb;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange(w, h);
        chartBg.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        try {
            chartBg.render(canvas);
            chart.render(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            triggerClick(event.getX(),event.getY());
        }
        return true;
    }


    //触发监听
    private void triggerClick(float x,float y)
    {
        PointPosition record = chart.getPositionRecord(x,y);
        if( null == record) {
            if(renderTip != null){
                renderTip.drawTip(0,0,0d);
            }
            return;
        }
        AreaData lData = mDataSet.get(record.getDataID());
        Double lValue = lData.getLinePoint().get(record.getDataChildID());
        LogUtils.logE("lValue =" + lValue);
        if(renderTip != null){
            renderTip.drawTip(record.getPosition().x,record.getPosition().y,lValue);
        }
        //在点击处显示tooltip
//        mPaintTooltips.setColor(Color.WHITE);
//        mPaintTooltips.setTextSize(DensityUtil.dip2px(getContext(), 12));
//        chart.getToolTip().setMargin(DensityUtil.dip2px(getContext(), 8));
//        chart.getToolTip().getBackgroundPaint().setColor(Color.rgb(165, 223, 105));
//        //chart.getToolTip().setCurrentXY(x,y);
//        chart.getToolTip().setCurrentXY(record.getPosition().x, record.getPosition().y);
//        chart.getToolTip().setStyle(XEnum.DyInfoStyle.CAPRECT);
//
//        chart.getToolTip().addToolTip("功耗",mPaintTooltips);
//        chart.getToolTip().addToolTip(Double.toString(lValue) + "wh/km",mPaintTooltips);
//        chart.getToolTip().setAlign(Paint.Align.CENTER);
//
//        this.invalidate();

    }

    public void setRenderTip(IRenderTip renderTip) {
        this.renderTip = renderTip;
    }

    public IRenderTip getRenderTip() {
        return renderTip;
    }

    public interface IRenderTip{
        void drawTip(float x, float y, double value);
    }
}
