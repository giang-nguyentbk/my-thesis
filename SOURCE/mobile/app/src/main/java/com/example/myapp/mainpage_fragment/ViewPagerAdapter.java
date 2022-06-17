package com.example.myapp.mainpage_fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapp.datalist.AlarmApdater;
import com.example.myapp.login.User;
import com.example.myapp.outstationlist.Outstation;

public class ViewPagerAdapter extends FragmentStateAdapter {

    Outstation outstat;
    User user;
    AlarmFragment alarmFragment;
    MonitorFragment monitorFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setData(Outstation outstation, User m_user) {
        outstat = outstation;
        user = m_user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                monitorFragment = MonitorFragment.getInstance(outstat);
                return monitorFragment;
            case 1:
                if(outstat.getOS_Type() == 4)
                    return new ControlFragment();
                return new BaseControlFragment();
            case 2:
                return new TrendFragment();
            case 3: {
                alarmFragment = AlarmFragment.getInstance(outstat, user);
                return alarmFragment;
            }
            default:
                monitorFragment = MonitorFragment.getInstance(outstat);
                return monitorFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public AlarmFragment getAlarmFragment(){
        return this.alarmFragment;
    }

    public MonitorFragment getMonitorFragment(){
        return this.monitorFragment;
    }

}
