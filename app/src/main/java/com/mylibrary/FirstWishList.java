package com.mylibrary;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private TextView txtWishList;
    private EditText editTextWishList;
    private Button btnWishList;
    private SendNameInterface sendNameInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_first_wish_list, container, false);
        txtWishList=view.findViewById(R.id.txtWishList);
        editTextWishList=view.findViewById(R.id.editTxtWishList);
        btnWishList = view.findViewById(R.id.btnWishList);

        //Receiving data from parent activity
        Bundle bundle = getArguments();
        if(bundle != null) {
            String message = bundle.getString("message");
            if(message!=null)txtWishList.setText(message);
        }

        try {
            /*get the instance of implementation class (which is parent activity) so that we can execute our callback
            on that*/
            sendNameInterface = (SendNameInterface) getActivity();
        }catch(ClassCastException ce){
            /*try catch used because say this fragment is used in more than one activity and some of the activity
            does not implement the SendNameInterface then above thing will throw exception*/
        }

        btnWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name=editTextWishList.getText().toString();
               if(sendNameInterface != null) {
                   //Sending data from fragment to activity using callback interface
                   sendNameInterface.sendNameResult(name);
               }
            }
        });

        return view;
    }
}