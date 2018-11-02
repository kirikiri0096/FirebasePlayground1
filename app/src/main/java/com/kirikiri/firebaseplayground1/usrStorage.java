package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class usrStorage extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_storage);


    }

    @Override
    public void onClick(View v) {

    }
}
