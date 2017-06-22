package com.kings.yatharth.yatharthresume.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kings.yatharth.yatharthresume.R;
import com.kings.yatharth.yatharthresume.controller.AsyncTaskCompleteListener;

public class Home extends AppCompatActivity implements AsyncTaskCompleteListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView info = (TextView) findViewById(R.id.info);
    }

    @Override
    public void onTaskComplete(Object result) {
        // Do after result of http request
    }
}
