package com.kirikiri.firebaseplayground1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class usrUpdate extends AppCompatActivity implements View.OnClickListener {

    private Map<String, Object> users = new HashMap<>();
    private static final String TAG = "UsrDataUpdate";
    private FirebaseAuth mAuth;
    EditText upfnamevalue, uplnamevalue, upSSOValue, upAddressvalue, upAddressExtvalue, upDisplayValue, upPhoneValue, upEmailValue;
    Button usrUpdateBtn;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_update);

        upfnamevalue = (EditText) findViewById(R.id.upfnamevalue);
        uplnamevalue = (EditText) findViewById(R.id.uplnamevalue);
        upSSOValue = (EditText) findViewById(R.id.upSSOValue);
        upAddressvalue = (EditText) findViewById(R.id.upAddressvalue);
        upAddressExtvalue = (EditText) findViewById(R.id.upAddressExtvalue);
        upDisplayValue = (EditText) findViewById(R.id.upDisplayValue);
        upPhoneValue = (EditText) findViewById(R.id.upfnamevalue);
        upEmailValue = (EditText) findViewById(R.id.upEmailValue);
        usrUpdateBtn = (Button) findViewById(R.id.usrUpdateBtn);
        usrUpdateBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Toast.makeText(usrUpdate.this, "Please login before use this activity",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //if document of current user is valid get data
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        upfnamevalue.setText(document.getString("Fname"));
                        uplnamevalue.setText(document.getString("Lname"));
                        upSSOValue.setText(document.getString("SSO"));
                        upAddressvalue.setText(document.getString("address"));
                        upAddressExtvalue.setText(document.getString("address_ext"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        upDisplayValue.setText(currentUser.getDisplayName());
        upPhoneValue.setText(currentUser.getPhoneNumber());
        upEmailValue.setText(currentUser.getEmail());
    }

    @Override
    public void onClick(View v) {

    }
}
