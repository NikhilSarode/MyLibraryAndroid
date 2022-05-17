package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookActivity extends AppCompatActivity {

    private Button btnDetailsCurrentlyReading,btnDetailsWantToRead,btnAlreadyRead,btnAddToFav;
    private TextView txtBookDetailsName,txtBkDetNameVal,txtBkDtsAuthor,txtBkDtsAuthorVal,txtBkDtsPages,txtBkDtlsPagesVal,txtBkDtlsDesc,txtBkDtlsDescVal;
    private ImageView imgBkDtls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent=getIntent();
        int bookId=-1;
        if(intent!=null)bookId=intent.getIntExtra("bookId",-1);
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
    }
}