package com.kirikiri.firebaseplayground1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class usrStorage extends AppCompatActivity {

    //TODO upload user's picture into storage

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private Button picChooseBtn, picUploadBtn;
    private TextView usrName, usrEmail;
    private ArrayList<Image> images = new ArrayList<>();
    private static final String TAG = "usrStorage";

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_storage);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(usrStorage.this, "Please login before use this activity",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        picChooseBtn = (Button) findViewById(R.id.picChooseBtn);
        picUploadBtn = (Button) findViewById(R.id.picUploadBtn);
        usrName = (TextView) findViewById(R.id.picShowUser);
        usrEmail = (TextView) findViewById(R.id.picShowEmail);

        picChooseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        picUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void captureImage() {
        ImagePicker.create(this).limit(6).start();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            printImages(images);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void printImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath()).append("\n");
            Log.d(TAG, Uri.parse(images.get(i).getPath()).toString());
        }
//        textView.setText(stringBuffer.toString());
    }

    private void uploadImage() {

        if(images != null)
        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageReference = storage.getReference();
//            StorageReference ref = storageReference.child("images/");

            for(Image i:images) {
//                ref.putFile(Uri.parse(i.getPath()))
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                progressDialog.dismiss();
//                                Toast.makeText(usrStorage.this, "Uploaded", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                progressDialog.dismiss();
//                                Toast.makeText(usrStorage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
//                                        .getTotalByteCount());
//                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                            }
//                        });
//                Log.d(TAG, Uri.parse(i.getPath()).toString());
            }


        }
    }


}
