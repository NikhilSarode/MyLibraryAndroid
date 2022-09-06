package com.mylibrary;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*
Activities can be broken down in fragments.
Why to use fragments:-
    1. Maintain well seperation
    2. Reusability of the fragments possbile
    3. Fragments can be replaced with other fragments. Thus in same container we can show different views
*/
public class FirstWishList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_first_wish_list, container, false);
        return view;
    }
}