package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class testActivity extends AppCompatActivity implements View.OnClickListener {

    //This page just to test some UI or Function

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {

    }
}
