package com.example.myapp.mainpage_fragment;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.slider.Slider;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ControlFragment extends Fragment {

    private View view;
    private Slider slider;
    private TextView textView;
    private Button btn_onLED1;
    private Button btn_offLED1;
    private ShapeableImageView shapeLED1;
    private boolean btn_on;
    private boolean btn_off;
    private boolean run;
    private int duty = 50;
    private Socket mSocket;{
        try {
            mSocket = IO.socket("http://20.205.122.62:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSocket.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control, container, false);
        slider = view.findViewById(R.id.sl_brightness_led1);
        textView = view.findViewById(R.id.tv_duty);
        btn_onLED1 = view.findViewById(R.id.btn_onLED1);
        btn_offLED1 = view.findViewById(R.id.btn_offLED1);
        shapeLED1 = view.findViewById(R.id.shape_led1);

        btn_onLED1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_on = true;
                btn_off = false;
                run = true;
                mSocket.emit("client-send-runLED1", run);

                if(getContext() == null) return;
                btn_onLED1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
                btn_offLED1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_200));

                if(duty/100f > 0.1f)
                    shapeLED1.setAlpha(duty / 100f);
                else
                    shapeLED1.setAlpha(0.1f);

            }
        });

        btn_offLED1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_off = true;
                btn_on = false;
                run = false;
                mSocket.emit("client-send-runLED1", run);

                if(getContext() == null) return;
                btn_onLED1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_200));
                btn_offLED1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
                shapeLED1.setAlpha(0.1f);
            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                duty = Math.round(slider.getValue());
                textView.setText(Integer.toString(duty));
                mSocket.emit("client-send-duty", duty);

                if(run) {
                    if(duty/100f > 0.1f)
                        shapeLED1.setAlpha(duty / 100f);
                    else
                        shapeLED1.setAlpha(0.1f);
                }
            }
        });

        return view;
    }
}