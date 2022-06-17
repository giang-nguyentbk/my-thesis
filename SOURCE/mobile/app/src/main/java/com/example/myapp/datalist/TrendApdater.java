package com.example.myapp.datalist;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import androidx.core.content.ContextCompat;

import com.example.myapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;
import java.util.List;

public class TrendApdater extends BaseAdapter {

    private List<Data> mListData;
    private List<View> viewHolder = new ArrayList<>();
    private boolean isInitVH = false;
    private Context context;

    public TrendApdater(List<Data> mListData, Context context) {
        this.mListData = mListData;
        this.context = context;
        if(!isInitVH) {
            initVH();
            isInitVH = true;
        }
    }

    public List<Data> getListData() {
        return this.mListData;
    }

    private void initVH() {
        if(viewHolder.isEmpty()) {
            for (int i = 0; i < mListData.size(); i++) {
                View view = View.inflate(context, R.layout.layout_trend_list, null);
                LineChart lineChart = view.findViewById(R.id.lc_trend_list);
                Data data = mListData.get(i);
                initLineChart(lineChart, data, context);
                viewHolder.add(view);
            }
        }
    }

    public View getViewInViewHolder(int position) {
        return viewHolder.get(position);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int i) {

        return mListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Data data = (Data) getItem(i);

        view = viewHolder.get(i);

        return view;
    }

    private void initLineChart(LineChart chart, Data object, Context context) {
        // enable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDoubleTapToZoomEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(ContextCompat.getColor(context, R.color.teal_200));
        l.setTextSize(12f);

        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setTextSize(12f);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setLabelCount(3);
        xl.setCenterAxisLabels(true);
        xl.setGranularity(1f);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }
}
