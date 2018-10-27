package com.example.zhangl.lovelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoveLayout loveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loveLayout =findViewById(R.id.loveLayout);

    }

    public void add(View view) {
        loveLayout.addLove();

    }
}
