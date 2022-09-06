package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

public class WishListActivity extends AppCompatActivity implements SendNameInterface{

    private TextView txtParentWishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        txtParentWishList=findViewById(R.id.txtParentWishList);

        //2nd way of using fragment:- This creates a transaction to replace the Frame with our fragment
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        FirstWishList fragment= new FirstWishList();
        //sending data to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("message", "This message is coming from parent activity to fragment");
        fragment.setArguments(bundle);
        transaction.replace(R.id.container,fragment);
        transaction.commit();
    }

    @Override
    public void sendNameResult(String name) {
        txtParentWishList.setText(name);
    }
}