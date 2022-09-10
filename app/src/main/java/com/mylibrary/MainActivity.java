package com.mylibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AddNewBookDialog.OnSaveNewBookDialogInterface {

    private Button btnAllBooks,btnCurrentlyReading,btnCreateNewBook,btnYourWishList,btnSeeYourFavourites,btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        btnAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AllBooksActivity.class);
                startActivity(intent);
            }
        });

        btnCurrentlyReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CurrentlyReadingActivity.class);
                startActivity(intent);
            }
        });

        btnYourWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WishListActivity.class);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Please visit our website");
                builder.setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(MainActivity.this,WebsiteActivity.class);
                        intent.putExtra("url","www.google.com");
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        btnCreateNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewBookDialog newBkDialog=new AddNewBookDialog();
                //newBkDialog.setArguments(bundle);//if you want to pass data to the dialog
                newBkDialog.show(getSupportFragmentManager(),"createNewBkDialog");
            }
        });

        btnSeeYourFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,FavouritesActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onSaveNewBook(Book book) {
        Utils.getInstance().addBook(book);
        Intent intent = new Intent(MainActivity.this,AllBooksActivity.class);
        startActivity(intent);
    }

    private void initViews(){
        btnAllBooks=findViewById(R.id.btnAllBooks);
        btnCurrentlyReading=findViewById(R.id.btnCurrentlyReading);
        btnCreateNewBook=findViewById(R.id.btnCreateNewBook);
        btnYourWishList=findViewById(R.id.btnYourWishList);
        btnSeeYourFavourites=findViewById(R.id.btnSeeYourFavourites);
        btnAbout=findViewById(R.id.btnAbout);
    }
}