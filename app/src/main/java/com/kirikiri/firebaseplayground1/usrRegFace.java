package com.kirikiri.firebaseplayground1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import bolts.Task;

public class usrRegFace extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;

    private TextView userUID, userPhone, userEmail, userPro, userName, userURL;
    private Button faceOut;

    private CallbackManager mCallbackManager;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("949304445270084");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.usr_reg_face);

        mAuth = FirebaseAuth.getInstance();

        userUID = (TextView) findViewById(R.id.userUID4);
        userPhone = (TextView) findViewById(R.id.userPhone4);
        userEmail = (TextView) findViewById(R.id.userEmail4);
        userPro = (TextView) findViewById(R.id.userPro4);
        userName = (TextView) findViewById(R.id.userName4);
        userURL = (TextView) findViewById(R.id.userURL4);
        faceOut = (Button)  findViewById(R.id.faceOut);
        faceOut.setOnClickListener(this);

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.faceSign);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(usrRegFace.this, "Authentication sucess.",
                                    Toast.LENGTH_SHORT).show();
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(user.getEmail());
                            com.google.android.gms.tasks.Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (!document.exists()) {
                                            Map<String, Object> userDB = new HashMap<>();
//                                            userDB.put("Fname", user.getDisplayName().substring(0, user.getDisplayName().indexOf(" ")));
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
                                        }
                                    }
                                }
                            });
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(usrRegFace.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }

//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                    }
                });
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            userUID.setText("UID: " + currentUser.getUid());
            userPhone.setText("Phone: " + currentUser.getPhoneNumber());
            userEmail.setText("Email: " + currentUser.getEmail());
            userPro.setText("Pro: " + currentUser.getProviderId());
            userName.setText("DisplayName: " + currentUser.getDisplayName());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userDB = new HashMap<>();
            db.collection("users").document(currentUser.getEmail())
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
        switch (v.getId()){
            case R.id.faceOut:
                    signOut();
        }

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(usrRegFace.this, "Signed out",
                Toast.LENGTH_SHORT).show();
        updateUI(null);
    }
}
