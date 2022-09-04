package com.mylibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class BookActivity extends AppCompatActivity {

    private Button btnDetailsCurrentlyReading,btnDetailsWantToRead,btnAlreadyRead,btnAddToFav,btnrecommendToFriend;
    private TextView txtBookDetailsName,txtBkDetNameVal,txtBkDtsAuthor,txtBkDtsAuthorVal,txtBkDtsPages,txtBkDtlsPagesVal,txtBkDtlsDesc,txtBkDtlsDescVal;
    private ImageView imgBkDtls;
    private static final int camera_request_code=145;
    private static final int camera_permission_code=105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent=getIntent();
        int bookId=-1;
        if(intent!=null)bookId=intent.getIntExtra("bookId",-1);
        //or get it through bundle
        if(intent!=null){
            Bundle bundle=intent.getBundleExtra("bundle");
            if(bundle!=null)bookId=bundle.getInt("bookId");
        }

        //This bundle was coming from Intent. In this method as a argument we see savedInstanceState which is also
        //a bundle. This bundle is used to retain the state of activity upon any configuration changes.
        //Configuration changes can be like rotating the phone, change device language etc.
        //In such events activity onCreate hook is invoked again which causes to loose user performed actions
        //To reatin that use this savedInstanceState

        Book book=Utils.getInstance().getBook(bookId);

        initViews();
        txtBkDtsAuthorVal.setText(book.getAuthor());
        txtBkDtlsPagesVal.setText(String.valueOf(book.getPages()));
        txtBkDtlsDescVal.setText(book.getLongDesc());
        Glide.with(this)
                .asBitmap()
                .load(book.getImageUrl())
                .into(imgBkDtls);

        btnDetailsCurrentlyReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.getInstance().addToCurrentlyReading(book);
                Intent intent = new Intent(BookActivity.this,CurrentlyReadingActivity.class);
                startActivity(intent);
            }
        });

        btnAlreadyRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtBkDtlsDescVal.setText("Its already read");
            }
        });
        //based on above click listener if "Already Read" is clicked it changes the description.
        //Now rotate the screen. See that the change is lost if we dont use savedInstanceState to reset it
        if(savedInstanceState!=null) {
            String desc=savedInstanceState.getString("bookDescription");
            txtBkDtlsDescVal.setText(desc);
        }

        //Lets say i want to recommend a book by sending SMS. So this will be done as follows.
        btnrecommendToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We will create an Intent with an action. Put in message. Now for this android system will check
                //which apps can handle this intent. So it will give a list of apps to choose from.
                //This shows communication between apps is done using intent.
                //Thus intent is not only useful in navigating from activity to activity in a single app but also
                //to share data between apps.
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Hello this is a very good book : "+book.getName());
                intent.setType("text/plain");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(BookActivity.this,"Cannot handle the intent",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //To capture a photo for book image using camera
        imgBkDtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePermission();
            }
        });
    }

    //To retain the information upon configuration changes
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("bookDescription",txtBkDtlsDescVal.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void handlePermission(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},camera_permission_code);
        }
    }
    private void openCamera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,camera_request_code );
    }

    //To capture a photo for book image using camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case camera_request_code:
                if(resultCode==RESULT_OK && data!=null){
                    Bundle bundle=data.getExtras();
                    if(bundle!=null){
                        Bitmap bitmap=(Bitmap)bundle.get("data");
                        Glide.with(this)
                                .asBitmap()
                                .load(bitmap)
                                .into(imgBkDtls);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case camera_permission_code:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                break;
        }
    }

    private void initViews(){
        btnDetailsCurrentlyReading=findViewById(R.id.btnDetailsCurrentlyReading);
        btnDetailsWantToRead=findViewById(R.id.btnDetailsWantToRead);
        btnAlreadyRead=findViewById(R.id.btnAlreadyRead);
        btnAddToFav=findViewById(R.id.btnAddToFav);
        txtBookDetailsName=findViewById(R.id.txtBookDetailsName);
        txtBkDetNameVal=findViewById(R.id.txtBkDetNameVal);
        txtBkDtsAuthor=findViewById(R.id.txtBkDtsAuthor);
        txtBkDtsAuthorVal=findViewById(R.id.txtBkDtsAuthorVal);
        txtBkDtsPages=findViewById(R.id.txtBkDtsPages);
        txtBkDtlsPagesVal=findViewById(R.id.txtBkDtlsPagesVal);
        txtBkDtlsDesc=findViewById(R.id.txtBkDtlsDesc);
        txtBkDtlsDescVal=findViewById(R.id.txtBkDtlsDescVal);
        imgBkDtls=findViewById(R.id.imgBkDtls);
        btnrecommendToFriend=findViewById(R.id.recommendToFriend);
    }
}