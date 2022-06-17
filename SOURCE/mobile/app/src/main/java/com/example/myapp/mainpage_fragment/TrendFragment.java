package com.example.myapp.mainpage_fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapp.R;
import com.example.myapp.datalist.AlarmApdater;
import com.example.myapp.datalist.Data;
import com.example.myapp.datalist.DataApdater;
import com.example.myapp.datalist.TrendApdater;
import com.example.myapp.outstationlist.Outstation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class TrendFragment extends Fragment  {

    private View mview;
    private ListView lv_Trendlist;
    private Thread thread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_trend, container, false);
        lv_Trendlist = mview.findViewById(R.id.lv_trend_list);

        ViewPager2 viewPager2 = getActivity().findViewById(R.id.vp2_monitor);
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager2.getAdapter();
        MonitorFragment monitorFragment = viewPagerAdapter.getMonitorFragment();
        RecyclerView recyclerView = monitorFragment.getMonitorRCV();
        DataApdater dataApdater = (DataApdater) recyclerView.getAdapter();
        List<Data> dataList = dataApdater.getListData();

        TrendApdater apdater = new TrendApdater(dataList, getContext());
        lv_Trendlist.setAdapter(apdater);

        feedMultiple();

        return mview;
    }

    private boolean getStateConnected() {
        if(getActivity() == null) return false;
        ViewPager2 viewPager2 = getActivity().findViewById(R.id.vp2_monitor);
        if(viewPager2 == null) return false;
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager2.getAdapter();
        if(viewPagerAdapter == null) return false;
        MonitorFragment monitorFragment = viewPagerAdapter.getMonitorFragment();
        return monitorFragment.getIsConnected();
    }

    private void updateTrendData() {
        if(getActivity() == null) return;
        ViewPager2 viewPager2 = getActivity().findViewById(R.id.vp2_monitor);
        if(viewPager2 == null) return;
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager2.getAdapter();
        if(viewPagerAdapter == null) return;
        MonitorFragment monitorFragment = viewPagerAdapter.getMonitorFragment();
        if(monitorFragment == null) return;
        RecyclerView recyclerView = monitorFragment.getMonitorRCV();
        if(recyclerView == null) return;
        DataApdater dataApdater = (DataApdater) recyclerView.getAdapter();
        if(dataApdater == null) return;
        List<Data> dataList = dataApdater.getListData();
        TrendApdater trendApdater = (TrendApdater) lv_Trendlist.getAdapter();
        if(trendApdater == null) return;
        List<Data> trendList = trendApdater.getListData();
        trendList = dataList;
        updateColorTrend(getStateConnected());
        trendApdater.notifyDataSetChanged();
    }

    private void updateColorTrend(boolean isConnect) {

        if(getContext() == null) return;
        int lineColor = ContextCompat.getColor(getContext(), R.color.blue_500);
        Drawable fillColor = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
        if(getContext() !=null) {
            if(getStateConnected()) {
                lineColor = ContextCompat.getColor(getContext(), R.color.blue_500);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    fillColor = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                }
            } else {
                lineColor = ContextCompat.getColor(getContext(), R.color.redfade_500);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    fillColor = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                }
            }
        }

        if(lv_Trendlist.getAdapter() == null) return;
        for(int i=0; i < lv_Trendlist.getAdapter().getCount(); i++) {
            TrendApdater trendApdater = (TrendApdater) (lv_Trendlist.getAdapter());
            if(trendApdater == null) return;
            View childView = trendApdater.getViewInViewHolder(i);
            if(childView == null) return;
            LineChart lineChart = childView.findViewById(R.id.lc_trend_list);
            if(lineChart == null) return;
            LineData data =  lineChart.getData();
            if(data == null) return;
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            if(set == null) return;
            set.setColor(lineColor);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                set.setFillDrawable(fillColor);
            }
            trendApdater.notifyDataSetChanged();
            lv_Trendlist.invalidateViews();
        }
    }

    private void updateListView() {
        updateTrendData();
        for(int i=0; i < lv_Trendlist.getAdapter().getCount(); i++) {
            TrendApdater trendApdater = (TrendApdater) (lv_Trendlist.getAdapter());
            View childView = trendApdater.getViewInViewHolder(i);
            LineChart lineChart = childView.findViewById(R.id.lc_trend_list);
            Data data = (Data) lv_Trendlist.getAdapter().getItem(i);
            addEntry(lineChart, data);
            trendApdater.notifyDataSetChanged();
            lv_Trendlist.invalidateViews();
        }
    }

    int VISIBLE_NUM = 750;
    long START_TIME = 0;
    long DELAY_TIME = 200;
    private void addEntry(LineChart chart, Data object) {

        LineData data = chart.getData();

        if (data != null) {
            data.setHighlightEnabled(false);

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet(object);
                data.addDataSet(set);
            }

            if(set.getEntryCount() == 0) {
                START_TIME = System.currentTimeMillis();
                XAxis xl = chart.getXAxis();
                xl.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                            return (new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
                                    .format(new Date(START_TIME + (long) value * DELAY_TIME)));
                    }
                });
            }

            if(set.getEntryCount() > VISIBLE_NUM) {
                set.removeFirst();
                for(int i = 0; i < set.getEntryCount(); i++) {
                    Entry entry = set.getEntryForIndex(i);
                    entry.setX(entry.getX() -1);
                }
            }

            float y_value = object.getValue();
            data.addEntry(new Entry(set.getEntryCount(), y_value), 0);
            set.setLabel(object.getIndicator() + ": " + Float.toString(object.getValue()) + " (" + object.getUnit() + ")");

            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(VISIBLE_NUM);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet(Data object) {

        // Pass Data.indicator to View
        LineDataSet set = new LineDataSet(null, object.getIndicator() + ": " + 0f + " (" + object.getUnit() + ")");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        int blue_500 = ContextCompat.getColor(getContext(), R.color.redfade_500);
        set.setColor(blue_500);
        set.setLineWidth(1f);
        set.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
            set.setFillDrawable(drawable);
        }
        else {
            set.setFillColor(Color.BLACK);
        }
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                updateListView();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                        if(getActivity() == null)
                            return;
                        getActivity().runOnUiThread(runnable);
                        try {
                            Thread.sleep(DELAY_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (thread != null) {
            thread.interrupt();
        }
    }


}

