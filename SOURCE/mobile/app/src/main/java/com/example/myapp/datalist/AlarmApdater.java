package com.example.myapp.datalist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.my_interface.IClickItemAlarmListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmApdater extends RecyclerView.Adapter<AlarmApdater.AlarmViewHolder> {

    private List<Data> mListAlarm;
    private IClickItemAlarmListener iClickItemAlarmListener;

    public AlarmApdater(List<Data> mListAlarm, IClickItemAlarmListener iClickItemAlarmListener) {
        this.mListAlarm = mListAlarm;
        this.iClickItemAlarmListener = iClickItemAlarmListener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alarm_list, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        final Data data = mListAlarm.get(position);
        if (data == null)
            return;
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa    dd/MM/yyyy", Locale.ENGLISH);

        switch (data.getSeverity()) {
            case 0: {
                return;
            }
            case 1: {
                holder.img_alarmIcon.setImageResource(R.drawable.ic_alarm);
                break;
            }
            case 2: {
                holder.img_alarmIcon.setImageResource(R.drawable.ic_alert);
                break;
            }
            case 3: {
                holder.img_alarmIcon.setImageResource(R.drawable.ic_event);
                break;
            }
        }

        holder.tv_alarmDatetime.setText(df.format(new Date(data.getStartTime())));
        holder.tv_alarmContent.setText(data.getContent());

        holder.rl_alarmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemAlarmListener.onClickItemAlarm(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListAlarm != null)
            return mListAlarm.size();
        return 0;
    }

    public List<Data> getmListAlarm() {
        return this.mListAlarm;
    }


    public class AlarmViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_alarmList;
        private ImageView img_alarmIcon;
        private TextView tv_alarmDatetime;
        private TextView tv_alarmContent;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_alarmList = itemView.findViewById(R.id.rl_alarm_list);
            img_alarmIcon = itemView.findViewById(R.id.img_alarm_icon);
            tv_alarmDatetime = itemView.findViewById(R.id.tv_alarm_datetime);
            tv_alarmContent = itemView.findViewById(R.id.tv_alarm_content);
        }
    }
}
