package com.kirikiri.firebaseplayground1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class usrRegEmail extends AppCompatActivity implements View.OnClickListener {

    //this page is user's registration with E-mail without any other information

    //Initial firebase and data
    private FirebaseAuth mAuth;
    private static final String TAG = "regEmail";
    private Map<String, Object> userDB = new HashMap<>();

    private TextView usr,pass, userUID, userPhone, userEmail, userPro, userName, userURL;
    private Button go;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_reg_email);

        mAuth = FirebaseAuth.getInstance();
        usr = (EditText) findViewById(R.id.insEmailValue2);
        pass = (EditText) findViewById(R.id.insPassValue2);
        userUID = (TextView) findViewById(R.id.userUID2);
        userPhone = (TextView) findViewById(R.id.userPhone2);
        userEmail = (TextView) findViewById(R.id.userEmail2);
        userPro = (TextView) findViewById(R.id.userPro2);
        userName = (TextView) findViewById(R.id.userName2);
        userURL = (TextView) findViewById(R.id.userURL2);
        go = (Button) findViewById(R.id.authGo2);
        go.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
//         Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(@Nullable FirebaseUser currentUser) {
        if (currentUser != null) {
            userUID.setText("UID: " + currentUser.getUid());
            userPhone.setText("Phone: " + currentUser.getPhoneNumber());
            userEmail.setText("Email: " + currentUser.getEmail());
            userPro.setText("Pro: " + currentUser.getProviderId());
            userName.setText("DisplayName: " + currentUser.getDisplayName());
        }
        else {
            userUID.setText("UID: Signed Out");
            userPhone.setText("Phone: Signed Out");
            userEmail.setText("Email: Signed Out");
            userPro.setText("Pro: Signed Out");
            userName.setText("DisplayName: Signed Out");
        }
    }

    @Override
    public void onClick(View v) {
        checkAccount(usr.getText().toString(),pass.getText().toString());
    }

    private void checkAccount (final String email, final String password) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(email);
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //if document of user is valid get data
                        Log.d(TAG, "createUserWithEmail:Email hae been used");
                        Toast.makeText(usrRegEmail.this, "This email has been use",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Log.d(TAG, "No such document");
                        createAccount(email, password);
                    }
                }
            }
        });


    }

    private void createAccount (String email, String password) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(usrRegEmail.this, "createUserWithEmail:success",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            db.collection("users").document(user.getEmail())
                                    .set(userDB)
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

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(usrRegEmail.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }
}
