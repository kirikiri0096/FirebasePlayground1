package com.kirikiri.firebaseplayground1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//TODO create order request by user
//Instruction: get order count from db to create document ID
//Note: document id format: [xxxxxx]-> x is positive integer

public class usrMakeOrder extends AppCompatActivity {

    private Map<String, Object> userOrders = new HashMap<>();
    private static final String TAG = "UsrMakeOrder";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText prodNameValue, address0Value, desc0Value;
    private Button subm0;

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

        prodNameValue = (EditText) findViewById(R.id.prodNameValue);
        address0Value = (EditText) findViewById(R.id.address0Value);
        desc0Value = (EditText) findViewById(R.id.desc0Value);
        subm0 = (Button) findViewById(R.id.subm0);
        subm0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodName = prodNameValue.getText().toString();
                String address = address0Value.getText().toString();
                String descrip = desc0Value.getText().toString();
                if(TextUtils.isEmpty(prodName)||TextUtils.isEmpty(address))
                    Toast.makeText(usrMakeOrder.this, "Please product information",
                            Toast.LENGTH_SHORT).show();
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    userOrders.put("usrEmail", currentUser.getEmail());
                    userOrders.put("prodName", prodName);
                    userOrders.put("address", address);
                    if(!TextUtils.isEmpty(descrip))
                        userOrders.put("description", descrip);

                    DocumentReference docRef = db.collection("etc").document("order");
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    int docID = document.getLong("count").intValue();
                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                    db2.collection("etc").document("order")
                                            .update("count", docID+1);
                                    db2.collection("orders").document(convToString(docID+1))
                                            .set(userOrders)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    private static String convToString (int number) {
        String str="";
        if(number < 10) {
            str = "0000" + Integer.toString(number);
        }
        else if(number < 100) {
            str = "000" + Integer.toString(number);
        }
        else if(number < 1000) {
            str = "00" + Integer.toString(number);
        }
        else if(number < 10000) {
            str = "0" + Integer.toString(number);
        }
        else {
            str = Integer.toString(number);
        }
        return str;
    }

    public static int convToInt (String str) {
        return Integer.parseInt(str);
    }
}
