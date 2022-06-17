package com.example.myapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.datalist.Data;
import com.example.myapp.datalist.DataApdater;
import com.example.myapp.login.User;
import com.example.myapp.my_interface.IClickItemOutstationListener;
import com.example.myapp.outstationlist.Outstation;
import com.example.myapp.outstationlist.OutstationApdater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    private Toolbar tb_outstation;
    private SearchView searchView;
    private PopupMenu popup;
    private RecyclerView rcv_outstation;
    private OutstationApdater outstationApdater;
    private User user;
    private Socket mSocket; {
        try {
            mSocket = IO.socket("http://20.205.122.62:3000");
        } catch (URISyntaxException e) {}
    }
    Emitter.Listener readListOutstation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        JSONArray inforList = data.getJSONArray("infor");
                        int soTram = inforList.length();

                        if(outstationApdater.getItemCount() == soTram) return;

                        for(int i=0; i<soTram; i++) {
                            if(outstationApdater.getItemCount() != soTram) {
                                outstationApdater.getmListOutstation().add(new Outstation());
                            }

                            Outstation jvData;
                            jvData = outstationApdater.getOutstationByIndex(i);
                            JSONObject os = inforList.getJSONObject(i);
                            jvData.setId(os.getInt("id"));
                            jvData.setOS_Type(os.getInt("OS_Type"));

                            if (os.getInt("OS_Type") == 0) {
                                jvData.setResourceId(R.drawable.ic_khithai);
                            } else if (os.getInt("OS_Type") == 1) {
                                jvData.setResourceId(R.drawable.ic_nuocthai);
                            } else if (os.getInt("OS_Type") == 2) {
                                jvData.setResourceId(R.drawable.ic_khongkhi);
                            } else if (os.getInt("OS_Type") == 3) {
                                jvData.setResourceId(R.drawable.ic_nuocmat);
                            }

                            jvData.setRcv_outstationNumber(os.getString("OS_Number"));
                            jvData.setRcv_location(os.getString("OS_Location"));
                        }
                        outstationApdater.notifyDataSetChanged();

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb_outstation = findViewById(R.id.tb_outstation);
        rcv_outstation = findViewById(R.id.rcv_OutstationList);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;
        user = (User) bundle.get("LoginActi_To_MainActi_User");

        setSupportActionBar(tb_outstation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_outstation.setLayoutManager(linearLayoutManager);

        outstationApdater = new OutstationApdater(new ArrayList<>(), new IClickItemOutstationListener() {
            @Override
            public void onClickItemOutstation(Outstation outstation) {
                onClickGoToDetail(outstation);
            }
        });
        rcv_outstation.setAdapter(outstationApdater);

        mSocket.on("vps-send-inforlist", readListOutstation);
        mSocket.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_outstation, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                outstationApdater.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                outstationApdater.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finishAffinity();
            return true;
        }
        if(id == R.id.menu_account) {
            View menuItemView = findViewById(R.id.menu_account);
            if(popup == null) {
                popup = new PopupMenu(this, menuItemView);
                popup.inflate(R.menu.menu_account);
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.menu_account_profile) {
                        onClickGoToProfile();
                        return true;
                    }
                    if(item.getItemId() == R.id.menu_account_logout) {
                        finish();
                        return true;
                    }
                    return false;
                }
            });
            popup.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void onClickGoToDetail(Outstation outstation) {
        Intent intent = new Intent(this, MonitorActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MainActi_To_MonitorActi_Outstation", outstation);
        bundle.putSerializable("MainActi_To_MonitorActi_User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void onClickGoToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MainActi_To_ProfileActi_User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}