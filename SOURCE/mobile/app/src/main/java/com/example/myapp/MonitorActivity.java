package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.login.User;
import com.example.myapp.mainpage_fragment.MonitorFragment;
import com.example.myapp.mainpage_fragment.ViewPagerAdapter;
import com.example.myapp.outstationlist.Outstation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MonitorActivity extends AppCompatActivity {

    private Toolbar tb_monitor;
    private ViewPager2 mviewPager2;
    private BottomNavigationView mbottomNavigationView;
    private Outstation outstation;
    private User user;
    private TextView tvOSNum;
    private TextView tvOSLocation;
    private ImageView imgOSType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        mviewPager2 = findViewById(R.id.vp2_monitor);
        mbottomNavigationView = findViewById(R.id.btnv_monitor);
        tb_monitor = findViewById(R.id.tb_monitor);
        tvOSNum = findViewById(R.id.tv_tb_outstationNum);
        tvOSLocation = findViewById(R.id.tv_tb_location);
        imgOSType = findViewById(R.id.img_tb_icon);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;
        outstation = (Outstation) bundle.get("MainActi_To_MonitorActi_Outstation");
        user = (User) bundle.get("MainActi_To_MonitorActi_User");

        setSupportActionBar(tb_monitor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvOSNum.setText(outstation.getRcv_outstationNumber());
        tvOSLocation.setText(outstation.getRcv_location());
        imgOSType.setImageResource(outstation.getResourceId());

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.setData(outstation, user);
        mviewPager2.setAdapter(adapter);

        mbottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_monitor) {
                    mviewPager2.setCurrentItem(0);
                } else if (id == R.id.menu_control) {
                    mviewPager2.setCurrentItem(1);
                } else if (id == R.id.menu_trend) {
                    mviewPager2.setCurrentItem(2);
                } else if (id == R.id.menu_alarm) {
                    mviewPager2.setCurrentItem(3);
                }
                return true;
            }
        });

        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mbottomNavigationView.getMenu().findItem(R.id.menu_monitor).setChecked(true);
                        break;
                    case 1:
                        mbottomNavigationView.getMenu().findItem(R.id.menu_control).setChecked(true);
                        break;
                    case 2:
                        mbottomNavigationView.getMenu().findItem(R.id.menu_trend).setChecked(true);
                        break;
                    case 3:
                        mbottomNavigationView.getMenu().findItem(R.id.menu_alarm).setChecked(true);
                        break;
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        if (mviewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mviewPager2.setCurrentItem(mviewPager2.getCurrentItem() - 1);
        }
    }

//    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if(outstation.getResourceId() == R.drawable.ic_khithai) {
//        fragmentTransaction.replace(R.id.fl_monitorActivity, KhiThaiFragment.getInstance(outstation));
//    } else if(outstation.getResourceId() == R.drawable.ic_nuocthai) {
//        fragmentTransaction.replace(R.id.fl_monitorActivity, NuocThaiFragment.getInstance(outstation));
//    }
//        fragmentTransaction.commit();
}