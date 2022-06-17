package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.api.ApiService;
import com.example.myapp.login.LoginResult;
import com.example.myapp.login.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText et_inputEmail;
    private EditText et_inputPassword;
    private Button btn_login;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btnlogin);
        et_inputEmail = findViewById(R.id.inputEmail);
        et_inputPassword = findViewById(R.id.inputPassword);

        et_inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        username = et_inputEmail.getText().toString();
        password = et_inputPassword.getText().toString();

        sendPosts();
    }

    public void sendPosts() {
        LoginResult loginResult = new LoginResult("", username, password, false, false, "");

        ApiService.apiService.sendPosts(loginResult).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult loginResult = response.body();

                if(loginResult != null && loginResult.getStatus()) {
                    System.out.println(loginResult.getUsername());
                    User user = new User(loginResult.getFullName(), loginResult.getUsername(), loginResult.isRole());
                    onGoToMain(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Username/Password incorrect!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onGoToMain(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("LoginActi_To_MainActi_User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}