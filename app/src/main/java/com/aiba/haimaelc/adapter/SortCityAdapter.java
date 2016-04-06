package com.aiba.haimaelc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.SortCity;
import com.aiba.haimaelc.tools.CommonTools;

import java.util.List;
import java.util.Map;

public class SortCityAdapter extends SectionedBaseAdapter {

    private Map<String, List<SortCity>> map;
    private Context context;
    private List<String> sections;
    private LayoutInflater inflater;
    private static final int VIEW_TYPE = 3;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;

    public SortCityAdapter(Context context, List<String> sections, Map<String, List<SortCity>> map) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.sections = sections;
        this.map = map;
        this.context = context;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return map.size();
    }

    @Override
    public int getCountForSection(int section) {
        String key = sections.get(section);
        return map.get(key).size();
    }

    @Override
    public int getItemViewType(int section, int position) {
        if (context.getResources().getString(R.string.location_city).equals(sections.get(section))) {
            return TYPE_1;
        } else if (context.getResources().getString(R.string.hot_city).equals(sections.get(section))) {
            return TYPE_2;
        } else {
            return TYPE_3;
        }
    }

    @Override
    public int getItemViewTypeCount() {
        return VIEW_TYPE;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
//        ItemCurrentViewHolder holder1 = null;
//        ItemHotViewHolder holder2 = null;
        View view = null;
        int type = getItemViewType(section, position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
//                    holder1 = new ItemCurrentViewHolder();
//                    view = inflater.inflate(R.layout.item_select_city_current, null);
//                    holder1.textView = (TextView) view.findViewById(R.id.aiba_current_city);
//                    view.setTag(holder1);
//                    break;
                case TYPE_2:
//                    holder2 = new ItemHotViewHolder();
//                    view = inflater.inflate(R.layout.item_select_city_hot, null);
//                    holder2.gridView = (GridLayout) view.findViewById(R.id.aiba_hot_city);
//                    view.setTag(holder2);
//                    break;
                case TYPE_3:
                    holder = new ItemViewHolder();
                    view = inflater.inflate(R.layout.item_select_city, null);
                    holder.tvLetter = (TextView) view.findViewById(R.id.child_txt);
                    holder.city_list_divider = view.findViewById(R.id.city_list_divider);
                    view.setTag(holder);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
//                    view = convertView;
//                    holder1 = (ItemCurrentViewHolder) view.getTag();
//                    break;
                case TYPE_2:
//                    view = convertView;
//                    holder2 = (ItemHotViewHolder) view.getTag();
//                    break;
                case TYPE_3:
                    view = convertView;
                    holder = (ItemViewHolder) view.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_1:
//                holder1.textView.setText(TextUtils.isEmpty(SysModel.currentCity) ? "定位中..." : SysModel.currentCity);
//                holder1.textView.setOnClickListener(listener);
                String city = map.get(sections.get(section)).get(position).getName();
                holder.tvLetter.setText(TextUtils.isEmpty(city) ? "定位中..." : city);
                holder.tvLetter.setTextColor(context.getResources().getColor(R.color.green_dark));
                holder.city_list_divider.setVisibility(View.GONE);
                CommonTools.setDrawableLeft(context, holder.tvLetter, R.mipmap.icon_station_distance);
                break;
            case TYPE_2:
//                holder2.gridView.removeAllViewsInLayout();
//                for (int i = 0; i < Dictionary.hotCityList.size(); i++) {
//                    View add_city = inflater.inflate(R.layout.view_hot_city, null);
//                    // add_city.setLayoutParams(layoutParams);
//                    TextView hotCity = (TextView) add_city.findViewById(R.id.hot_city);
//                    hotCity.setText(Dictionary.hotCityList.get(i).getName());
//                    hotCity.setTag(R.string.tag_01, Dictionary.hotCityList.get(i).getCityId());
//                    hotCity.setTag(R.string.tag_02, Dictionary.hotCityList.get(i).getProvinceId());
//                    hotCity.setTag(R.string.tag_03, Dictionary.hotCityList.get(i).getName());
//                    hotCity.setTag(R.string.tag_04, Dictionary.hotCityList.get(i).getWeatherCode());
//                    hotCity.setOnClickListener(listener);
//                    holder2.gridView.addView(add_city);
//                }
//                break;
            case TYPE_3:
                holder.tvLetter.setText(map.get(sections.get(section)).get(position).getName());
                if (position == map.get(sections.get(section)).size() - 1) {
                    holder.city_list_divider.setVisibility(View.GONE);
                } else {
                    holder.city_list_divider.setVisibility(View.VISIBLE);
                }
                break;
        }
        return view;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        SectionHeaderViewHolder holder;
        View view;
        if (convertView == null) {
            holder = new SectionHeaderViewHolder();
            view = inflater.inflate(R.layout.item_select_city_title, null);
            holder.tvTitle = (TextView) view.findViewById(R.id.alpha);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (SectionHeaderViewHolder) view.getTag();
        }
        if (context.getResources().getString(R.string.location_city).equals(sections.get(section))) {
            holder.tvTitle.setText("当前城市");
        } else if (context.getResources().getString(R.string.hot_city).equals(sections.get(section))) {
            holder.tvTitle.setText("热门城市");
        } else {
            holder.tvTitle.setText(sections.get(section));
        }
        return view;
    }

    class ItemViewHolder {
        TextView tvLetter;
        View city_list_divider;
    }

    class SectionHeaderViewHolder {
        TextView tvTitle;
    }

//    class ItemCurrentViewHolder {
//        TextView textView;
//    }
//
//    class ItemHotViewHolder {
//        GridLayout gridView;
//    }
}