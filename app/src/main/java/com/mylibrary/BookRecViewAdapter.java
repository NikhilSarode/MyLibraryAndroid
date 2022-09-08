package com.mylibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BookRecViewAdapter extends RecyclerView.Adapter<BookRecViewAdapter.ViewHolder> {

    private List<Book> books=new ArrayList<>();

    private Context mContext;
    private String parentActivity;

    public BookRecViewAdapter(Context mContext, String parentActivity) {
        this.mContext = mContext;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookTitle.setText(books.get(position).getName());
        Glide.with(mContext)
                .asBitmap()
                .load(books.get(position).getImageUrl())
                .into(holder.bookImage);

        holder.txtAuthorName.setText(books.get(position).getAuthor());
        holder.txtShortDesc.setText(books.get(position).getShortDesc());

        holder.bookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,BookActivity.class);
                //1st way of passing data
                intent.putExtra("bookId",books.get(position).getId());
                //2nd way of passing data:- such data can be passed in the bundle object
                //Thus bundle can be used to pass multiple data in same object
                Bundle bundle=new Bundle();
                bundle.putInt("bookId",books.get(position).getId());
                intent.putExtra("bundle",bundle);
                /*3rd way of passing data. Directly the object. For this to work your java object should
                    have implemented Parcelable interface
                */
                intent.putExtra("book",books.get(position));

                mContext.startActivity(intent);
            }
        });

        if(books.get(position).isExpanded()){
            TransitionManager.beginDelayedTransition(holder.collapsedContainer);
            holder.collapsedContainer.setVisibility(View.VISIBLE);

            holder.downArrow.setVisibility(View.GONE);
        }else{
            TransitionManager.beginDelayedTransition(holder.collapsedContainer);
            holder.collapsedContainer.setVisibility(View.GONE);

            holder.downArrow.setVisibility(View.VISIBLE);
        }

        if("allBooks".equalsIgnoreCase(parentActivity)){
            holder.btnDelete.setVisibility(View.GONE);
        }else if("currentlyReading".equalsIgnoreCase(parentActivity)){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Book book=books.get(position);

                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setMessage("Are you sure you want to delete");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.getInstance().removeCurrentlyReadingBook(book);
                            Toast.makeText(mContext,"Book removed",Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView bookCard;
        private ImageView bookImage;
        private TextView bookTitle;

        private RelativeLayout expandedContainer,collapsedContainer;
        private ImageView downArrow;
        private ImageView upArrow;
        private TextView txtAuthorName,txtShortDesc,txtAuthor;
        private TextView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCard=itemView.findViewById(R.id.bookCard);
            bookImage=itemView.findViewById(R.id.bookImage);
            bookTitle=itemView.findViewById(R.id.bookTitle);
            expandedContainer=itemView.findViewById(R.id.expandedContainer);
            collapsedContainer=itemView.findViewById(R.id.collapsedContainer);
            downArrow=itemView.findViewById(R.id.downArrow);
            upArrow=itemView.findViewById(R.id.upArrow);
            txtAuthorName=itemView.findViewById(R.id.txtAuthorName);
            txtShortDesc=itemView.findViewById(R.id.txtShortDesc);
            txtAuthor=itemView.findViewById(R.id.txtAuthor);
            btnDelete=itemView.findViewById(R.id.btnDelete);

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Book book=books.get(getAdapterPosition());
                    book.setExpanded(true);
                    notifyItemChanged(getAdapterPosition());
                }
            });

            upArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Book book=books.get(getAdapterPosition());
                    book.setExpanded(false);
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
