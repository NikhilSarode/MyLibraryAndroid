package com.mylibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment implements AddNewBookDialog.OnSaveNewBookDialogInterface {

    private Button btnAllBooks,btnCurrentlyReading,btnCreateNewBook,btnYourWishList,btnSeeYourFavourites,btnAbout;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        initViews(view);
        initBottomNavMenu();

        btnAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AllBooksActivity.class);
                startActivity(intent);
            }
        });

        btnCurrentlyReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CurrentlyReadingActivity.class);
                startActivity(intent);
            }
        });

        btnYourWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WishListActivity.class);
                startActivity(intent);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Please visit our website");
                builder.setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(getActivity(),WebsiteActivity.class);
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
                AddNewBookDialog newBkDialog=new AddNewBookDialog(MainFragment.this);
                //newBkDialog.setArguments(bundle);//if you want to pass data to the dialog
                newBkDialog.show(getActivity().getSupportFragmentManager(),"createNewBkDialog");
            }
        });

        btnSeeYourFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),FavouritesActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onSaveNewBook(Book book) {
        Utils.getInstance().addBook(book);
        Intent intent = new Intent(getActivity(),AllBooksActivity.class);
        startActivity(intent);
    }
    private void initViews(View view){
        btnAllBooks=view.findViewById(R.id.btnAllBooks);
        btnCurrentlyReading=view.findViewById(R.id.btnCurrentlyReading);
        btnCreateNewBook=view.findViewById(R.id.btnCreateNewBook);
        btnYourWishList=view.findViewById(R.id.btnYourWishList);
        btnSeeYourFavourites=view.findViewById(R.id.btnSeeYourFavourites);
        btnAbout=view.findViewById(R.id.btnAbout);
        bottomNavigationView=view.findViewById(R.id.bottomNavView);
    }

    private void initBottomNavMenu(){
        bottomNavigationView.setSelectedItemId(R.id.bottomHome); //for default selection in bottom nav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottomHome:
                        Toast.makeText(getActivity(),"Home selected",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bottomSearch:
                        Toast.makeText(getActivity(),"Search selected",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bottomCart:
                        Toast.makeText(getActivity(),"Cart selected",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }
}