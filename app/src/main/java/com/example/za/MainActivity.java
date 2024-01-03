package com.example.za;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToPvPActivity(View view) {
        Intent intent = new Intent(this, PvPActivity.class);
        startActivity(intent);
    }

    public void goToPvCActivity(View view) {
        Intent intent = new Intent(this, PvCActivity.class);
        startActivity(intent);
    }
}
