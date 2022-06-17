package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myapp.login.User;
import com.example.myapp.outstationlist.Outstation;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    private Toolbar toolbar;
    private TextView tv_fullName;
    private TextView tv_role;
    private TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.tb_profile);
        tv_fullName = findViewById(R.id.tv_fullName);
        tv_role = findViewById(R.id.tv_role);
        tv_username = findViewById(R.id.tv_username);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;
        user = (User) bundle.get("MainActi_To_ProfileActi_User");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_fullName.setText(user.getFullName());
        if(user.isRole())
            tv_role.setText("admin");
        else
            tv_role.setText("user");
        tv_username.setText(user.getUsername());
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