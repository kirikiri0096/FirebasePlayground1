package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

//This page will only create new data to DB

public class usrDB1 extends AppCompatActivity implements View.OnClickListener{


    private Map<String, Object> users = new HashMap<>();
    //Variables below will get user's information from Firebase's Firestore
    private TextView userFName, userLName, userSSO, userAddress, userAddressExt, phonevalue;
    private Button sentBtn;
    private static final String TAG = "usrDB";

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_db);

        //Getting elements on activity
        userFName = (EditText) findViewById(R.id.fnamevalue);
        userLName = (EditText) findViewById(R.id.lnamevalue);
        userSSO = (EditText) findViewById(R.id.ssovalue);
        userAddress = (EditText) findViewById(R.id.addressvalue);
        userAddressExt = (EditText) findViewById(R.id.addressextvalue);
        phonevalue = (EditText) findViewById(R.id.phonevalue);
        sentBtn = (Button) findViewById(R.id.usrDBSendBtn);
        sentBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //Initial database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Getting data into Map
        users.put("Fname", userFName.getText().toString());
        users.put("Lname", userLName.getText().toString());
        users.put("SSO", userSSO.getText().toString());
        users.put("address", userAddress.getText().toString());
        users.put("address_ext", userAddressExt.getText().toString());
        users.put("phone", phonevalue.getText().toString());


        //Put data into Firestore with random document name
        db.collection("users")
                .add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
