package com.app_services.mr_kaushik.tappydefender;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HighScore extends Activity {
    TextView tvHighScore;
    TextView tvDisplayHS;
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        tvHighScore = (TextView)findViewById(R.id.tvHigh);
        tvDisplayHS = (TextView) findViewById(R.id.tvDisplayHS);
        String distance = getIntent().getStringExtra("distance");
        String time = getIntent().getStringExtra("time");
        tvDisplayHS.setText("Distance : " + distance + " Time : " + time);
    }
}
