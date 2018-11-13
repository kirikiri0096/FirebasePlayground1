package com.kirikiri.firebaseplayground1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class usrGetP extends AppCompatActivity implements View.OnClickListener {

    //To-Do
    //This page will fetch user's profile picture from currentUser.getPhotoUrl().toString()

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageView userPicDisp;
    private TextView userUID, userPhone, userEmail, userPro, userName, userURL;
    private String ImaURL;
    private final String TAG = "getProfile";
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.usr_get_prof);
        userUID = (TextView) findViewById(R.id.userUID5);
        userPhone = (TextView) findViewById(R.id.userPhone5);
        userEmail = (TextView) findViewById(R.id.userEmail5);
        userPro = (TextView) findViewById(R.id.userPro5);
        userName = (TextView) findViewById(R.id.userName5);
        userURL = (TextView) findViewById(R.id.userURL5);
        userPicDisp = (ImageView) findViewById(R.id.userPicDisp);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(usrGetP.this, "Please login before use this activity",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else {
            userUID.setText("UID: " + currentUser.getUid());
            userPhone.setText("Phone: " + currentUser.getPhoneNumber());
            userEmail.setText("Email: " + currentUser.getEmail());
            userPro.setText("Pro: " + currentUser.getProviderId());
            userName.setText("DisplayName: " + currentUser.getDisplayName());
            userURL.setText("ProfileURL: " + currentUser.getPhotoUrl().toString());
            ImaURL = currentUser.getPhotoUrl().toString();

            new DownLoadImageTask(userPicDisp).execute(ImaURL);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            String filename = currentUser.getEmail()+(".jpg");
            StorageReference storageRef = storage.getReference().child("users").child("prof");
            String pathToProf = storageRef.child(filename).getPath();

        }
    }
    @Override
    public void onClick(View v) {

    }

    private void updateUI(FirebaseUser currentUser) {
        userUID.setText("UID: " + currentUser.getUid());
        userPhone.setText("Phone: " + currentUser.getPhoneNumber());
        userEmail.setText("Email: " + currentUser.getEmail());
        userPro.setText("Pro: " + currentUser.getProviderId());
        userName.setText("DisplayName: " + currentUser.getDisplayName());
        userURL.setText("ProfileURL: " + currentUser.getPhotoUrl().toString());
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
