package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;

public class usrGetP extends AppCompatActivity implements View.OnClickListener {

    //To-Do
    //This page will fetch and upload user's profile picture into storage
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_get_prof);
    }
    @Override
    public void onClick(View v) {

    }

    private void updateUI(FirebaseUser currentUser) {

    }
}
