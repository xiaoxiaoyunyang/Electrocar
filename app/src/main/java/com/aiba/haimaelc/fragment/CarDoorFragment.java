package com.aiba.haimaelc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.activity.MainActivity;

/**
 * Created by zhu on 16/3/18.
 */
public class CarDoorFragment extends BaseFragment{
    private MainActivity activity;

    private TextView topLeft,topRight,bottomLeft,bottomRight;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car_door_status, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        topLeft = (TextView) activity.findViewById(R.id.top_left);
        topRight = (TextView) activity.findViewById(R.id.top_right);
        bottomLeft = (TextView) activity.findViewById(R.id.bottom_left);
        bottomRight = (TextView) activity.findViewById(R.id.bottm_right);

    }
}
