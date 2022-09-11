package com.mylibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        //Action Bar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.drawer_open,R.string.drawer_close);
        toggle.syncState();

        //click listeners for side drawer menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.cart:
                        Toast.makeText(MainActivity.this,"cart selected",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.abtUs:
                        Toast.makeText(MainActivity.this,"about us selected",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //Framelayout replaced with Main Fragment. Fragment is needed because we used DrawerLayout in main activity.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragmentContainer, new MainFragment());
        fragmentTransaction.commit();
    }

    private void initViews(){
        drawer=findViewById(R.id.drawer);
        navigationView = findViewById(R.id.sideNavigationView);
        toolbar = findViewById(R.id.toolbar);
    }
}