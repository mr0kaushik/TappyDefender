package com.app_services.mr_kaushik.tappydefender;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Level extends Activity {
    TextView level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        level = (TextView) findViewById(R.id.tvSelect);

    }
}
