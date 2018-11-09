package com.kirikiri.firebaseplayground1;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;


public class usrAuth extends AppCompatActivity implements View.OnClickListener{

    //Initial firebase's Authentication
    private FirebaseAuth mAuth;
    private static final String TAG = "usrAuth";
    //Variables below will get value from textbox on screen
    private TextView usr,pass;
    //Variables below will get user's information from Firebase's Authentication
    private TextView userUID, userPhone, userEmail, userPro, userName, userURL;
    //Variables below will get user's information from Firebase's Firestore
    private TextView userFName, userLName, userSSO, userAddress, userAddressExt;
    private Button go, out;


    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_auth);
        mAuth = FirebaseAuth.getInstance();

        usr = (EditText) findViewById(R.id.insEmailValue);
        pass = (EditText) findViewById(R.id.insPassValue);
        userUID = (TextView) findViewById(R.id.authUserUID);
        userPhone = (TextView) findViewById(R.id.authUserPhone);
        userEmail = (TextView) findViewById(R.id.authUserEmail);
        userPro = (TextView) findViewById(R.id.authUserPro);
        userName = (TextView) findViewById(R.id.authUserName);
        userURL = (TextView) findViewById(R.id.authUserURL);
        userFName = (TextView) findViewById(R.id.authUserFName);
        userLName = (TextView) findViewById(R.id.authUserLName);
        userSSO = (TextView) findViewById(R.id.authUserSSO);
        userAddress = (TextView) findViewById(R.id.authUserAddress);
        userAddressExt = (TextView) findViewById(R.id.authUserAddressExt);
        go = (Button) findViewById(R.id.authGo);
        go.setOnClickListener(this);
        out = (Button) findViewById(R.id.authOut);
        out.setOnClickListener(this);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(@Nullable FirebaseUser currentUser) {
        //If login
        if (currentUser != null) {
            //Start fetch content from firestore
            //Initial database instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //locate user's document
            DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //if document of current user is valid get data
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            userFName.setText("Fname: " + document.getString("Fname"));
                            userLName.setText("Lname: " + document.getString("Lname"));
                            userPhone.setText("Lname: " + document.getString("phone"));
                            userSSO.setText("SSO: " + document.getString("SSO"));
                            userAddress.setText("address: " + document.getString("address"));
                            userAddressExt.setText("address_ext: " + document.getString("address_ext"));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            //End fetch content from Firestore
            //Start fetch content from Authentication system
            userUID.setText("UID: " + currentUser.getUid());
            userEmail.setText("Email: " + currentUser.getEmail());
            userPro.setText("Pro: " + currentUser.getProviderId());
            userName.setText("DisplayName: " + currentUser.getDisplayName());
//            userURL.setText("ProfileURL: " + currentUser.getPhotoUrl().toString());
            for (UserInfo profile : currentUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                Log.d(TAG, "ProviderID: " + providerId);
                Log.d(TAG, "ProviderUID: " + uid);
                Log.d(TAG, "ProviderDisplayName: " + name);
                Log.d(TAG, "ProviderEmail: " + email);
//                Log.d(TAG, "ProviderPhotoURL: " + photoUrl.toString());
            }
        }
        else {
            userUID.setText("UID: Signed Out");
            userPhone.setText("Phone: Signed Out");
            userEmail.setText("Email: Signed Out");
            userPro.setText("Pro: Signed Out");
            userName.setText("DisplayName: Signed Out");
            userURL.setText("PictureURL: Signed Out");
            userFName.setText("Fname: Signed Out");
            userLName.setText("Lname: Signed Out");
            userSSO.setText("SSO: Signed Out");
            userAddress.setText("address: Signed Out");
            userAddressExt.setText("address_ext: Signed Out");
        }

    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(usrAuth.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(usrAuth.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            Toast.makeText(usrAuth.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
        Toast.makeText(usrAuth.this, "Signed out",
                Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.authGo:
                //Check if username of password is invalid
                String user = usr.getText().toString();
                String pass2 = pass.getText().toString();
                if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass2))
                    signIn(user,pass2);
                else {
                    Toast.makeText(usrAuth.this, "Please enter login information",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.authOut:
                signOut();
        }
    }

}
