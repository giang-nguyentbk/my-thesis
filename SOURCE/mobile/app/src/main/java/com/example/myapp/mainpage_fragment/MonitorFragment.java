package com.example.myapp.mainpage_fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapp.datalist.AlarmApdater;
import com.example.myapp.datalist.Data;
import com.example.myapp.datalist.DataApdater;
import com.example.myapp.outstationlist.Outstation;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MonitorFragment extends Fragment {

    private View mview;
    private RecyclerView rcv_Datalist;
    private Outstation outstation;
    private boolean isConnected = false;
    private int delay = 0;
    private String client_id = "";
    private Socket mSocket;{
        try {
            mSocket = IO.socket("http://20.205.122.62:3000");
        } catch (URISyntaxException e) {}
    }
    Emitter.Listener readListIndicator = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    try {
                        JSONArray indicatorList = data.getJSONArray("data");
                        int chisoCount = indicatorList.length();

                        DataApdater dataApdater = (DataApdater) rcv_Datalist.getAdapter();
                        if(dataApdater == null) return;

                        for(int i=0; i<chisoCount; i++) {
//                            if(dataApdater.getItemCount() != chisoCount) {
//                                dataApdater.getListData().add(new Data());
//                            }
                            Data jvData;
                            jvData = dataApdater.getDataByIndex(i);

                            JSONObject indicator = indicatorList.getJSONObject(i);
                            jvData.setId(i);
                            jvData.setIndicator(indicator.getString("indicator"));
                            if(outstation.getOS_Type() == 4 && i == 2) {
                                jvData.setValue(delay);
                            } else {
                                jvData.setValue((float) indicator.getDouble("value"));
                            }
                            jvData.setUnit(indicator.getString("unit"));
                            jvData.setLOWLOW((float) indicator.getDouble("LOWLOW"));
                            jvData.setLOW((float) indicator.getDouble("LOW"));
                            jvData.setHIGH((float) indicator.getDouble("HIGH"));
                            jvData.setHIGHHIGH((float) indicator.getDouble("HIGHHIGH"));
                            jvData.setDEAD_BAND((float) indicator.getDouble("DEAD_BAND"));
                            jvData.setSeverity(indicator.getInt("severity"));
                            jvData.setPre_severity(indicator.getInt("pre_severity"));
                            jvData.setStartTime(indicator.getLong("startTime"));
                            jvData.setDuration(indicator.getLong("duration"));
                            jvData.setPre_duration(indicator.getLong("pre_duration"));
                            jvData.setContent(indicator.getString("content"));
                            jvData.setACK(indicator.getBoolean("ACK"));
                            jvData.setAckUser(indicator.getString("ackUser"));
                            jvData.setACKTime(indicator.getLong("ACKTime"));
                        }
                        dataApdater.notifyDataSetChanged();
                        addAlarmData();
                        if(getContext() == null) return;
                        int teal_200 = ContextCompat.getColor(getContext(), R.color.teal_200);
                        updateColor(teal_200);
                        isConnected = true;

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private void updateColor(int color) {
        if(rcv_Datalist.getAdapter() == null) return;
        for(int i=0; i < rcv_Datalist.getAdapter().getItemCount(); i++) {
            if (rcv_Datalist.findViewHolderForAdapterPosition(i) == null) break;
            RecyclerView.ViewHolder viewHolder = rcv_Datalist.findViewHolderForAdapterPosition(i);
            if(viewHolder == null) break;
            TextView tv_Temper = viewHolder.itemView.findViewById(R.id.tv_value);
            if(getContext() == null) break;
            tv_Temper.setTextColor(color);
            rcv_Datalist.invalidate();
        }
    }

    public static MonitorFragment getInstance(Outstation outstation) {
        MonitorFragment monitorFragment = new MonitorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MonitorActi_To_MonitorFrag_Outstation", outstation);
        monitorFragment.setArguments(bundle);
        return monitorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outstation = (Outstation) getArguments().get("MonitorActi_To_MonitorFrag_Outstation");
        mSocket.on("vps-send-clientID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    client_id = data.getString("clientID");
                    mSocket.emit("client-send-OS_index", client_id, outstation.getOS_Type());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("vps-send-delay", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    int delay1 = data.getInt("delay");
                    delay = 2*delay1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mSocket.on("vps-send-data", readListIndicator);
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        int redfade_500 = ContextCompat.getColor(getContext(), R.color.redfade_500);
                        updateColor(redfade_500);
                        isConnected = false;
                    }
                });
            }
        });
        mSocket.connect();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_monitor, container, false);

        rcv_Datalist = mview.findViewById(R.id.rcv_dataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_Datalist.setLayoutManager(linearLayoutManager);

        DataApdater dataApdater = new DataApdater(getListData());
        dataApdater.setOutstation(outstation);
        rcv_Datalist.setAdapter(dataApdater);

        return mview;
    }

    private List<Data> getListData() {
        List<Data> list = new ArrayList<>();

        if(outstation.getOS_Type() == 0) { //Khi thai
            list.add(new Data("Temperature", 0, "°C", 60, 70, 90, 100, 3));
            list.add(new Data("Pressure", 0, "mbar", 800, 900, 1100, 1200, 20));
            list.add(new Data("NO", 0, "mg/m3", 100, 110, 130, 140, 3));
            list.add(new Data("NO2", 0, "mg/m3", 70, 80, 100, 110, 3));
            list.add(new Data("CO", 0, "mg/m3", 590, 610, 670, 690, 20));
            list.add(new Data("SO2", 0, "mg/m3", 150, 165, 195, 210, 5));
            list.add(new Data("O2", 0, "%V", 55, 60, 85, 95, 3));
            list.add(new Data("H2S", 0, "mg/m3", 70, 80, 100, 110, 3));
            list.add(new Data("NH3", 0, "mg/m3", 680, 700, 760, 790, 20));
            list.add(new Data("Hg", 0, "mg/m3", 160, 180, 220,240, 5));
            list.add(new Data("PM", 0, "mg/m3", 130, 140, 160, 170, 3));

        }else if(outstation.getOS_Type() == 1) { //Nuoc thai
            list.add(new Data("Flow In", 0, "m3/h", 60, 70, 90, 100, 3));
            list.add(new Data("Flow Out", 0, "m3/h", 800, 900, 1100, 1200, 20));
            list.add(new Data("Temperature", 0, "°C", 100, 110, 130, 140, 3));
            list.add(new Data("Color", 0, "Pt-Co", 70, 80, 100, 110, 3));
            list.add(new Data("pH", 0, "", 590, 610, 670, 690, 20));
            list.add(new Data("TSS", 0, "mg/L", 150, 165, 195, 210, 5));
            list.add(new Data("COD", 0, "mg/L", 55, 60, 85, 95, 3));
            list.add(new Data("NH4+", 0, "mg/L", 70, 80, 100, 110, 3));
            list.add(new Data("Photpho+", 0, "mg/L", 680, 700, 760, 790, 20));
            list.add(new Data("Nito", 0, "mg/L", 160, 180, 220,240, 5));
            list.add(new Data("TOC", 0, "mg/L", 130, 140, 160, 170, 3));
            list.add(new Data("Clo", 0, "mg/L", 130, 140, 160, 170, 3));

        }else if(outstation.getOS_Type() == 2) { //Khong khi
            list.add(new Data("Temperature", 0, "°C", 60, 70, 90, 100, 3));
            list.add(new Data("NO2", 0, "ppb", 800, 900, 1100, 1200, 20));
            list.add(new Data("CO", 0, "ppb", 100, 110, 130, 140, 3));
            list.add(new Data("SO2", 0, "ppb", 70, 80, 100, 110, 3));
            list.add(new Data("O3", 0, "ppb", 590, 610, 670, 690, 20));
            list.add(new Data("PM10", 0, "ug/Nm3", 150, 165, 195, 210, 5));
            list.add(new Data("PM2.5", 0, "ug/Nm3", 55, 60, 85, 95, 3));

        }else if(outstation.getOS_Type() == 3) { //Nuoc mat
            list.add(new Data("Temperature", 0, "°C", 60, 70, 90, 100, 3));
            list.add(new Data("pH", 0, "", 800, 900, 1100, 1200, 20));
            list.add(new Data("TSS", 0, "mg/L", 100, 110, 130, 140, 3));
            list.add(new Data("COD", 0, "mg/L", 70, 80, 100, 110, 3));
            list.add(new Data("DO", 0, "mg/L", 590, 610, 670, 690, 20));
            list.add(new Data("NO3-", 0, "mg/L", 150, 165, 195, 210, 5));
            list.add(new Data("PO4+", 0, "mg/L", 55, 60, 85, 95, 3));
            list.add(new Data("NH4+", 0, "mg/L", 70, 80, 100, 110, 3));
            list.add(new Data("Total P", 0, "mg/L", 680, 700, 760, 790, 20));
            list.add(new Data("Total N", 0, "mg/L", 160, 180, 220,240, 5));
            list.add(new Data("TOC", 0, "mg/L", 130, 140, 160, 170, 3));
        }else if(outstation.getOS_Type() == 4) { //Bach khoa
            list.add(new Data("Button ON", 0, "", 0, 0, 0, 0, 3));
            list.add(new Data("Button OFF", 0, "", 0, 0, 0, 0, 3));
            list.add(new Data("Delay", 0, "ms", 0, 0, 0, 0, 3));
        }

        return list;
    }

    public boolean getIsConnected(){
        return this.isConnected;
    }

    public RecyclerView getMonitorRCV(){
        return this.rcv_Datalist;
    }

    private void addAlarmData() {
        if(getActivity() == null) return;
        ViewPager2 viewPager2 = getActivity().findViewById(R.id.vp2_monitor);
        if(viewPager2 == null) return;
        ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager2.getAdapter();
        if(viewPagerAdapter == null) return;
        AlarmFragment alarmFragment = viewPagerAdapter.getAlarmFragment();
        if(alarmFragment == null) return;
        RecyclerView recyclerView = alarmFragment.getAlarmRCV();
        if(recyclerView == null) return;
        AlarmApdater alarmApdater = (AlarmApdater) recyclerView.getAdapter();
        if(alarmApdater == null) return;
        List<Data> alarmDataList = alarmApdater.getmListAlarm();
        DataApdater dataApdater = (DataApdater) rcv_Datalist.getAdapter();
        if(dataApdater == null) return;
        List<Data> dataList = dataApdater.getListData();

        List<Data> newList = new ArrayList<>();
        for(int i=0; i< dataList.size(); i++) {
            if(!dataList.get(i).getACK() && (dataList.get(i).getSeverity() == 1 || dataList.get(i).getSeverity() == 2)) {
                newList.add(dataList.get(i));
            }
        }
        if(newList.size() == alarmDataList.size()) {
            int i = 0;
            for(i=0; i<newList.size(); i++) {
                int j;
                for(j=0; j<alarmDataList.size(); j++){
                    if(newList.get(i).getId() == alarmDataList.get(j).getId()) break;
                }
                if(j == alarmDataList.size()) break;
            }
            if(i==newList.size()) {
                return;
            }
        }

        alarmDataList.clear();
        alarmDataList.addAll(newList);
        alarmApdater.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.close();
    }
}