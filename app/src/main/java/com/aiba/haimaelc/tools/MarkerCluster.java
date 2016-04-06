package com.aiba.haimaelc.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MarkerCluster {

    private Activity activity;
    private MarkerOptions options;
    private ArrayList<MarkerOptions> includeMarkers;
    private LatLngBounds bounds;// 创建区域
    private Object obj;//marker绑定对象

    /**
     * @param activity
     * @param markerOptions
     * @param projection
     * @param gridSize      区域大小参数
     */
    public MarkerCluster(Activity activity, MarkerOptions markerOptions, Projection projection, int gridSize, Object obj) {
        options = new MarkerOptions();
        this.activity = activity;
        Point point = projection.toScreenLocation(markerOptions.getPosition());
        Point southwestPoint = new Point(point.x - gridSize, point.y + gridSize);
        Point northeastPoint = new Point(point.x + gridSize, point.y - gridSize);
        bounds = new LatLngBounds(
                projection.fromScreenLocation(southwestPoint),
                projection.fromScreenLocation(northeastPoint));
        options.anchor(0.5f, 1f).title(markerOptions.getTitle())
                .position(markerOptions.getPosition())
                .icon(markerOptions.getIcon())
                .snippet(markerOptions.getSnippet());
        includeMarkers = new ArrayList<>();
        includeMarkers.add(markerOptions);
        this.obj = obj;
    }

    /**
     * 添加marker
     */
    public void addMarker(MarkerOptions markerOptions) {
        includeMarkers.add(markerOptions);// 添加到列表中
    }

    public int getSize() {
        return includeMarkers.size();
    }

    public Object getObject() {
        return obj;
    }

    /**
     * 设置聚合点的中心位置以及图标
     */
    public void setPositionAndIcon() {
        int size = includeMarkers.size();
        if (size == 1) {
            return;
        }
        double lat = 0.0;
        double lng = 0.0;
        String snippet = "";
        for (MarkerOptions op : includeMarkers) {
            lat += op.getPosition().latitude;
            lng += op.getPosition().longitude;
            snippet += op.getTitle() + "\n";
        }
        options.position(new LatLng(lat / size, lng / size));// 设置中心位置为聚集点的平均位置
        options.title("聚合点");
        options.snippet(snippet);
        options.anchor(0.5f, 1f);
        options.icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(size, R.mipmap.marker_station_cluster))));
//        int iconType = size / 10;
//        switch (iconType) {
//            case 0:
//                options.icon(BitmapDescriptorFactory
//                        .fromBitmap(getViewBitmap(getView(size,
//                                R.drawable.marker_cluster_10))));
//                break;
//            case 1:
//                options.icon(BitmapDescriptorFactory
//                        .fromBitmap(getViewBitmap(getView(size,
//                                R.drawable.marker_cluster_20))));
//                break;
//            case 2:
//                options.icon(BitmapDescriptorFactory
//                        .fromBitmap(getViewBitmap(getView(size,
//                                R.drawable.marker_cluster_30))));
//                break;
//            case 3:
//                options.icon(BitmapDescriptorFactory
//                        .fromBitmap(getViewBitmap(getView(size,
//                                R.drawable.marker_cluster_30))));
//                break;
//            case 4:
//                options.icon(BitmapDescriptorFactory
//                        .fromBitmap(getViewBitmap(getView(size,
//                                R.drawable.marker_cluster_50))));
//                break;
//            default:
//                options.icon(BitmapDescriptorFactory
//                        .fromBitmap(getViewBitmap(getView(size,
//                                R.drawable.marker_cluster_100))));
//                break;
//        }
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public MarkerOptions getOptions() {
        return options;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }

    public View getView(int carNum, int resourceId) {
        View view = activity.getLayoutInflater().inflate(R.layout.view_station_cluster, null);
        TextView carNumTextView = (TextView) view.findViewById(R.id.station_num);
        RelativeLayout myCarLayout = (RelativeLayout) view.findViewById(R.id.station_bg);
        myCarLayout.setBackgroundResource(resourceId);
        carNumTextView.setText(String.valueOf(carNum));
        return view;
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
