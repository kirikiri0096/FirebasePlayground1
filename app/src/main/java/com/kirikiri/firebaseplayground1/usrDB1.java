package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

//This class will show hot io import user's data into the database

public class usrDB1 extends AppCompatActivity implements View.OnClickListener{

    Map<String, Object> users = new HashMap<>();
    EditText fNameV, lNameV, ssoV, emailsV, phonesV, addressV, address_extV;
    Button btn;
    private static final String TAG = "DocSnippets";

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
        fNameV = (EditText) findViewById(R.id.fnamevalue);
        lNameV = (EditText) findViewById(R.id.lnamevalue);
        ssoV = (EditText) findViewById(R.id.ssovalue);
        phonesV = (EditText) findViewById(R.id.phonevalue);
        addressV = (EditText) findViewById(R.id.addressvalue);
        address_extV = (EditText) findViewById(R.id.addressextvalue);
        btn = (Button) findViewById(R.id.usrDBSendBtn);
//        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Getting data into Map
        users.put("Fname", fNameV.getText().toString());
        users.put("Lname", lNameV.getText().toString());
        users.put("SSO", ssoV.getText().toString());
        users.put("phone", phonesV.getText().toString());
        users.put("address", addressV.getText().toString());
        users.put("address_ext", address_extV.getText().toString());


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
