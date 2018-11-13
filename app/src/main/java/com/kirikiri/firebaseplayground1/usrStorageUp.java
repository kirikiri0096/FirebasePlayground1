package com.kirikiri.firebaseplayground1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class usrStorageUp extends AppCompatActivity {

    private final String TAG = "changeProfPic";
    private TextView stUsrName, stUsrEmail, stUsrURL;
    private ImageView stUpImage;
    private Button stUpload2;
    private FirebaseUser currentUser;
    private List<Image> images;
    private String ImaURL;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(usrStorageUp.this, "Please login before use this activity",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.usr_storage_up);
            stUsrName = (TextView) findViewById(R.id.stUsrName);
            stUsrEmail = (TextView) findViewById(R.id.stUsrEmail);
            stUsrURL = (TextView) findViewById(R.id.stUsrURL);
            stUpImage = (ImageView) findViewById(R.id.stUpImage);
            stUpload2 = (Button) findViewById(R.id.stUpload2);
            stUpload2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(TAG, "OnClick");
                    ImagePicker.create(usrStorageUp.this)
                            .single()
                            .start();
                }
            });
            stUsrName.setText("Display Name: " +currentUser.getDisplayName());
            stUsrEmail.setText("Email: " + currentUser.getEmail());
            stUsrURL.setText("ProfileURL: " + currentUser.getPhotoUrl().toString());
            ImaURL = currentUser.getPhotoUrl().toString();

            new DownLoadImageTask(stUpImage).execute(ImaURL);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            uploadImage(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(List<Image> images) {

        if (images != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageReference = storage.getReference();
            // [START upload_file]
            final StorageReference ref = storageReference.child("users/prof/" + currentUser.getEmail() +".jpg");
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ImaURL = uri.toString();
                }
            });
            ref.putFile(Uri.fromFile(new File(images.get(0).getPath())))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(usrStorageUp.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            // [START] Change PhotoURL Path
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(Uri.parse(ImaURL))
                                    .build();
                            currentUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.i(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            // [END] Change PhotoURL Path
                            stUsrURL.setText("ProfileURL: " + currentUser.getPhotoUrl().toString());
//                            ImaURL = String.valueOf(storage.getReferenceFromUrl(currentUser.getPhotoUrl().toString()));
                            ImaURL = currentUser.getPhotoUrl().toString();
                            new DownLoadImageTask(stUpImage).execute(ImaURL);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(usrStorageUp.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
            // [END upload_file]

        }
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
