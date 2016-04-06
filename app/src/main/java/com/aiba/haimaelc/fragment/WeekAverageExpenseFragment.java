package com.aiba.haimaelc.fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.adapter.WeekAverageCostAdapter;
import com.aiba.haimaelc.model.WeekAverageCost;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.PhoneSaveUtils;
import com.aiba.haimaelc.widget.LinearGradientAreaLineChart;

import org.xclcharts.chart.AreaData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by zhu on 16/3/25.
 * 周内平均能耗费用
 */
public class WeekAverageExpenseFragment extends BaseFragment {

    private LinearGradientAreaLineChart areaChart;
    private ListView mListView;
    private WeekAverageCostAdapter mAdapter;
    private List<WeekAverageCost> mData;
    private LinkedList<String> xLabels = new LinkedList<>();
    private LinkedList<Double> dataSeries = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_average, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        areaChart = (LinearGradientAreaLineChart) view.findViewById(R.id.average_area_chart);
        mListView = (ListView) view.findViewById(R.id.week_average);
        mListView.requestFocus();
        mListView.setFocusable(false);
        mData = new ArrayList<>();
        getLastSevenDate();
        initAreaChart();
        mAdapter = new WeekAverageCostAdapter(getActivity(), mData, R.layout.item_week_average_cost);
        mListView.setAdapter(mAdapter);
        CommonTools.setListViewHeightBasedOnChildren(mListView);
    }

    private void initAreaChart() {
        areaChart.initView();
        areaChart.showGridHorizontalLines(XEnum.LineStyle.DASH, new DashPathEffect(new float[]{15, 10, 15, 10}, 1));
        areaChart.showAxisLines();
        areaChart.setDataMax(36);
        areaChart.setDataStep(6);
        areaChart.addXLables(xLabels);
        //146,237,163
        AreaData lineData = new AreaData("周内平均能耗费用", dataSeries, Color.rgb(18,162,59),Color.rgb(192,238,165),
                new int[] {Color.argb(255, 18,162,59),Color.argb(105, 187,235,127), Color.argb(0, 187,235,127)},null);
        lineData.setDotStyle(XEnum.DotStyle.DOT);//显示点
        lineData.getDotPaint().setColor(Color.rgb(18, 162, 59));
        //设置点的半径
        lineData.getPlotLine().getPlotDot().setDotRadius((int) getActivity().getResources().getDimension(R.dimen.dot_radius));
        lineData.getLinePaint().setStrokeWidth((int) getActivity().getResources().getDimension(R.dimen.customLine_stroke));
        areaChart.charDataSet(7, lineData);
        //设置x轴颜色
        areaChart.getChartBg().getCategoryAxis().getTickLabelPaint().setColor(Color.rgb(74, 74, 74));
        //设置数据轴颜色
        areaChart.getChartBg().getDataAxis().getTickLabelPaint().setColor(Color.rgb(74,74,74));
        areaChart.getChartBg().getCategoryAxis().getAxisPaint().setColor(Color.rgb(26,7,7));
        areaChart.getChartBg().getCategoryAxis().getAxisPaint().setStrokeWidth(2);
        areaChart.getChartBg().getDataAxis().getAxisPaint().setColor(Color.rgb(27,7,7));
        areaChart.getChartBg().getDataAxis().getAxisPaint().setStrokeWidth(2);

        areaChart.refresh();
    }

    private void getLastSevenDate() {
        xLabels.clear();
        String preMonday = "";
        int distance;
        SimpleDateFormat format = new SimpleDateFormat("M-d");
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            distance = randomNumber();
            calendar.add(Calendar.DATE, -i);
            Date date = calendar.getTime();
            calendar.add(Calendar.DATE, i);
            preMonday = format.format(date);
            xLabels.add(preMonday);
            mData.add(new WeekAverageCost(preMonday, distance + "km",
                                        decimalFormat.format(distance / 10 * 2.1f) + "度",
                                        decimalFormat.format(distance / 10 * 2.8f) + "元"));
            dataSeries.add(distance / 10 * 2.8d);
            LogUtils.logE(preMonday);
        }

        //测试使用
        PhoneSaveUtils.putListData(AppConst.LAST_SEVEN_DATE, xLabels);
        PhoneSaveUtils.putListData(AppConst.LAST_WEEK_AVERAGE_COST, mData);
    }

    /**
     * 模拟数据
     *
     * @return
     */
    private int randomNumber() {
        return (int) (Math.random() * 50) + 30;
    }
}
