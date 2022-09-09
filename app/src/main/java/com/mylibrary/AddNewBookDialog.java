package com.mylibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddNewBookDialog extends DialogFragment {

    private EditText editTxtNewBkName,editTxtNewkAuthor,editTxtNewBkPages;
    private Button btnSave,btnCancel;

    public interface OnSaveNewBookDialogInterface{
        void onSaveNewBook(Book book);
    }

    private OnSaveNewBookDialogInterface onSaveNewBookDialogInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_new_book_dialog,null);
        initViews(view);

        //Bundle bundle=getArguments(); // If you want to receive data from activity

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                .setView(view) //sets the view to the plain dialog box
                .setTitle("Create a new book");

        try {
            onSaveNewBookDialogInterface=(OnSaveNewBookDialogInterface) getActivity();
        }catch (ClassCastException ex){

        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName=editTxtNewBkName.getText().toString();
                String bookAuthor=editTxtNewkAuthor.getText().toString();
                int bookPages=Integer.parseInt(editTxtNewBkPages.getText().toString());
                Book book = new Book();
                book.setName(bookName);book.setAuthor(bookAuthor);book.setPages(bookPages);

                if(onSaveNewBookDialogInterface!=null) {
                    onSaveNewBookDialogInterface.onSaveNewBook(book);
                    dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return builder.create();
    }

    private void initViews(View view){
        editTxtNewBkName=view.findViewById(R.id.editTxtNewBkName);
        editTxtNewkAuthor=view.findViewById(R.id.editTxtNewkAuthor);
        editTxtNewBkPages=view.findViewById(R.id.editTxtNewBkPages);
        btnSave=view.findViewById(R.id.btnSave);
        btnCancel=view.findViewById(R.id.btnCancel);
    }
}
