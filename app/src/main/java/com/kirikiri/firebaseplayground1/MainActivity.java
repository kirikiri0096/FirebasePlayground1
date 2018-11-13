package com.kirikiri.firebaseplayground1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    
    private RadioButton usrDB, usrAuth, usrRegEmail, usrRegGoo, usrRegFace, sysEtc, usrPictureUpload, usrUpdateProf, usrGetProf, supGloLst, usrMakeOrder, supLst, usrPictureDownload;
    private Button goBtn;
    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting elements on activity
        usrDB = (RadioButton) findViewById(R.id.usrDBBtn);
        usrAuth = (RadioButton) findViewById(R.id.usrAuthBtn);
        usrRegEmail = (RadioButton) findViewById(R.id.usrRegEmail);
        usrRegGoo = (RadioButton) findViewById(R.id.usrRegGoogle);
        usrRegFace = (RadioButton) findViewById(R.id.usrRegFacebook);
        sysEtc = (RadioButton) findViewById(R.id.etcBut);
        usrPictureUpload = (RadioButton) findViewById(R.id.usrPictureUpload);
        usrUpdateProf = (RadioButton) findViewById(R.id.usrUpdateProf);
        usrGetProf = (RadioButton) findViewById(R.id.usrGetProf);
        supGloLst = (RadioButton) findViewById(R.id.supGloLst);
        usrMakeOrder = (RadioButton) findViewById(R.id.usrMakeOrder);
        supLst = (RadioButton) findViewById(R.id.supLst);
        usrPictureDownload = (RadioButton) findViewById(R.id.usrPictureDownload);
        
        goBtn = (Button) findViewById(R.id.startDebugButt);
        goBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //creating Intend
        Intent intent = null;
        if(usrDB.isChecked()) {
            intent = new Intent(this, usrDB1.class);
        }
        if(usrAuth.isChecked()) {
            intent = new Intent(this, usrAuth.class);
        }
        if(usrRegEmail.isChecked()) {
            intent = new Intent(this, usrRegEmail.class);
        }
        if(usrRegGoo.isChecked()) {
            intent = new Intent(this, usrRegGoogle.class);
        }
        if(usrRegFace.isChecked()) {
            intent = new Intent(this, usrRegFace.class);
        }
        if(sysEtc.isChecked()) {
            intent = new Intent(this, testActivity.class);
        }
        if(usrPictureUpload.isChecked()) {
            intent = new Intent(this, usrStorage.class);
        }
        if(usrPictureDownload.isChecked()) {
            intent = new Intent(this, usrStorageUp.class);
        }
        if(usrGetProf.isChecked()) {
            intent = new Intent(this, usrGetP.class);
        }
        if(usrUpdateProf.isChecked()) {
            intent = new Intent(this, usrUpdate.class);
        }
        if(supGloLst.isChecked()) {
            intent = new Intent(this, orderListG.class);
        }
        if(usrMakeOrder.isChecked()) {
            intent = new Intent(this, usrMakeOrder.class);
        }
        if(supLst.isChecked()) {
            intent = new Intent(this, supOrderList.class);
        }
        switch (v.getId()) {
            case R.id.startDebugButt:
                if (intent != null) {
                    startActivity(intent);
                }
                break;
        }
    }
}
