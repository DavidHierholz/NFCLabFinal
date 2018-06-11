package com.example.david.nfclabfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onReadButtonClicked(View view) {
        Intent intent = new Intent(this, ReadActivity.class);
        startActivity(intent);
    }

    public void onWriteButtonClicked(View view) {
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }
}
