package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//TODO Show all order lists from firestore

public class orderListG extends AppCompatActivity {
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sup_global_list);
    }
}
