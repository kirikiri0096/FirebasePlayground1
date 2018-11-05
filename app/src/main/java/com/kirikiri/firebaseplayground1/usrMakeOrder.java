package com.kirikiri.firebaseplayground1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

//TODO create order request by user
//Instruction: get order count from db to create document ID
//Note: document id format: [xxxxxx]-> x is positive integer

public class usrMakeOrder extends AppCompatActivity {

    private Map<String, Object> users = new HashMap<>();
    private static final String TAG = "UsrMakeOrder";
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_make_order);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(usrMakeOrder.this, "Please login before use this activity",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
