package com.example.myapp.mainpage_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapp.DetailAlarmActivity;
import com.example.myapp.MonitorActivity;
import com.example.myapp.R;
import com.example.myapp.datalist.AlarmApdater;
import com.example.myapp.datalist.Data;
import com.example.myapp.datalist.DataApdater;
import com.example.myapp.datalist.TrendApdater;
import com.example.myapp.login.User;
import com.example.myapp.my_interface.IClickItemAlarmListener;
import com.example.myapp.outstationlist.Outstation;

import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment {

    private View mview;
    private RecyclerView rcv_alarmList;
    private Outstation outstation;
    private User user;

    public static AlarmFragment getInstance(Outstation outstation, User user) {
        AlarmFragment alarmFragment = new AlarmFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MonitorActi_To_AlarmFrag_Outstation", outstation);
        bundle.putSerializable("MonitorActi_To_AlarmFrag_User", user);
        alarmFragment.setArguments(bundle);
        return alarmFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outstation = (Outstation) getArguments().get("MonitorActi_To_AlarmFrag_Outstation");
        user = (User) getArguments().get("MonitorActi_To_AlarmFrag_User");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_alarm, container, false);
        rcv_alarmList = mview.findViewById(R.id.rcv_alarm_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_alarmList.setLayoutManager(linearLayoutManager);

        AlarmApdater alarmApdater = new AlarmApdater(new ArrayList<>(), new IClickItemAlarmListener() {
            @Override
            public void onClickItemAlarm(Data data) {
                ocClickGoToAlarmDetail(data);
            }
        });
        rcv_alarmList.setAdapter(alarmApdater);

        return mview;
    }

    public RecyclerView getAlarmRCV(){
        return this.rcv_alarmList;
    }

    private void ocClickGoToAlarmDetail(Data data) {
        Intent intent = new Intent(getContext(), DetailAlarmActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("AlarmFrg_To_DetailAlarmActi_Data", data);
        bundle.putInt("AlarmFrg_To_DetailAlarmActi_OS_index", outstation.getId());
        bundle.putSerializable("AlarmFrg_To_DetailAlarmActi_User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}