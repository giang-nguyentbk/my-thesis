package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.datalist.Data;
import com.example.myapp.login.User;
import com.example.myapp.outstationlist.Outstation;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;

public class DetailAlarmActivity extends AppCompatActivity {

    private Data data;
    private int OS_index;
    private User user;
    private Toolbar toolbar;
    private TextView tv_da_content_v;
    private ImageView severity_icon;
    private TextView tv_da_indicator;
    private TextView tv_da_indicator_v;
    private TextView tv_da_indicator_u;
    private TextView tv_da_lowlow_v;
    private TextView tv_da_lowlow_u;
    private TextView tv_da_low_v;
    private TextView tv_da_low_u;
    private TextView tv_da_high_v;
    private TextView tv_da_high_u;
    private TextView tv_da_highhigh_v;
    private TextView tv_da_highhigh_u;
    private TextView tv_da_deadband_v;
    private TextView tv_da_deadband_u;
    private TextView tv_da_starttime;
    private TextView tv_da_duration;
    private TextView tv_da_ackUser;
    private TextView tv_da_ackTime;
    private SwitchMaterial ackSwitch;
    private Socket mSocket; {
        try {
            mSocket = IO.socket("http://20.205.122.62:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_alarm);

        toolbar = findViewById(R.id.tb_detail_alarm);
        tv_da_content_v = findViewById(R.id.tv_da_content_v);
        severity_icon = findViewById(R.id.ic_da_severity_v);
        tv_da_indicator = findViewById(R.id.tv_da_indicator);
        tv_da_indicator_v = findViewById(R.id.tv_da_indicator_v);
        tv_da_indicator_u = findViewById(R.id.tv_da_indicator_u);
        tv_da_lowlow_v = findViewById(R.id.tv_da_LOWLOW_v);
        tv_da_lowlow_u = findViewById(R.id.tv_da_LOWLOW_u);
        tv_da_low_v = findViewById(R.id.tv_da_LOW_v);
        tv_da_low_u = findViewById(R.id.tv_da_LOW_u);
        tv_da_high_v = findViewById(R.id.tv_da_HIGH_v);
        tv_da_high_u = findViewById(R.id.tv_da_HIGH_u);
        tv_da_highhigh_v = findViewById(R.id.tv_da_HIGHHIGH_v);
        tv_da_highhigh_u = findViewById(R.id.tv_da_HIGHHIGH_u);
        tv_da_deadband_v = findViewById(R.id.tv_da_DEADBAND_v);
        tv_da_deadband_u = findViewById(R.id.tv_da_DEADBAND_u);
        tv_da_starttime = findViewById(R.id.tv_da_starttime_v);
        tv_da_duration = findViewById(R.id.tv_da_duration_v);
        tv_da_ackUser = findViewById(R.id.tv_da_ACKUser_v);
        ackSwitch = findViewById(R.id.sw_da_ack_v);
        tv_da_ackTime = findViewById(R.id.tv_da_acktime_v);

        mSocket.connect();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;
        data = (Data) bundle.get("AlarmFrg_To_DetailAlarmActi_Data");
        OS_index = bundle.getInt("AlarmFrg_To_DetailAlarmActi_OS_index");
        user = (User) bundle.get("AlarmFrg_To_DetailAlarmActi_User");

        if(data == null) return;
        tv_da_content_v.setText(data.getContent());
        switch (data.getSeverity()) {
            case 0: {
                break;
            }
            case 1: {
                severity_icon.setImageResource(R.drawable.ic_alarm);
                break;
            }
            case 2: {
                severity_icon.setImageResource(R.drawable.ic_alert);
                break;
            }
            case 3: {
                severity_icon.setImageResource(R.drawable.ic_event);
                break;
            }
        }
        tv_da_indicator.setText(data.getIndicator());
        tv_da_indicator_v.setText(Float.toString(data.getValue()));
        tv_da_indicator_u.setText(data.getUnit());
        tv_da_lowlow_u.setText(data.getUnit());
        tv_da_low_u.setText(data.getUnit());
        tv_da_high_u.setText(data.getUnit());
        tv_da_highhigh_u.setText(data.getUnit());
        tv_da_deadband_u.setText(data.getUnit());
        tv_da_lowlow_v.setText(Float.toString(data.getLOWLOW()));
        tv_da_low_v.setText(Float.toString(data.getLOW()));
        tv_da_high_v.setText(Float.toString(data.getHIGH()));
        tv_da_highhigh_v.setText(Float.toString(data.getHIGHHIGH()));
        tv_da_deadband_v.setText(Float.toString(data.getDEAD_BAND()));
        tv_da_starttime.setText(new SimpleDateFormat("hh:mm:ss aa\ndd/MM/yyyy", Locale.ENGLISH).format(new Date(data.getStartTime())));
        int hour = (int) ((data.getDuration() / 1000) / 3600);
        int min = (int) ((data.getDuration() / 1000) % 3600) / 60;
        int sec = (int) ((data.getDuration() / 1000) % 60);
        tv_da_duration.setText(String.format("%d:%02d:%02d", hour, min, sec));

        if(user.isRole()) {
            ackSwitch.setClickable(true);
        } else {
            ackSwitch.setClickable(false);
        }
        ackSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    data.setACK(true);
                    data.setAckUser(user.getFullName());
                    data.setACKTime(System.currentTimeMillis());
                    tv_da_ackUser.setText(data.getAckUser());
                    tv_da_ackTime.setText(new SimpleDateFormat("hh:mm:ss aa\ndd/MM/yyyy", Locale.ENGLISH).format(new Date(data.getACKTime())));
                    ackSwitch.setClickable(false);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("id", data.getId());
                        object.put("indicator", data.getIndicator());
                        object.put("value", data.getValue());
                        object.put("unit", data.getUnit());
                        object.put("LOWLOW", data.getLOWLOW());
                        object.put("LOW", data.getLOW());
                        object.put("HIGH", data.getHIGH());
                        object.put("HIGHHIGH", data.getHIGHHIGH());
                        object.put("DEAD_BAND", data.getDEAD_BAND());
                        object.put("severity", data.getSeverity());
                        object.put("startTime", data.getStartTime());
                        object.put("duration", data.getDuration());
                        object.put("content", data.getContent());
                        object.put("ACK", data.getACK());
                        object.put("ackUser", data.getAckUser());
                        object.put("ACKTime", data.getACKTime());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("alarmLog", OS_index, object);
                }
            }
        });

        if(!data.getACK()) {
            tv_da_ackUser.setText("");
            tv_da_ackTime.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}