package com.mylibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private static Utils utils;
    private List<Book> allBooks;
    private List<Book> currentlyReadingBooks;

    private Utils() {
        allBooks=new ArrayList<>();
        currentlyReadingBooks=new ArrayList<>();
        initData();
    }
    private void initData(){
        allBooks.add(new Book(1,"Book 1","TestAutohr", 2358,"https://pngimg.com/uploads/book/book_PNG2111.png",
                "its a book","its a very good book"));
        allBooks.add(new Book(2,"Book 2","TestAutohr2", 500,"https://cdn.pixabay.com/photo/2015/11/19/21/10/glasses-1052010__340.jpg",
                "its a book 2","its a very good book 2"));
    }

    public static synchronized Utils getInstance(){
        if(utils==null)utils=new Utils();
        return utils;
    }

    public List<Book> getAllBooks() {
        return allBooks;
    }

    public List<Book> getCurrentlyReadingBooks() {
        return currentlyReadingBooks;
    }

    public Book getBook(int id){
        for (Book book:allBooks){
            if(id==book.getId())return  book;
        }
        return null;
    }

    public boolean addToCurrentlyReading(Book book){
        return currentlyReadingBooks.add(book);
    }

    public boolean removeCurrentlyReadingBook(Book book){
        Log.d("as","nikhil inside removeCurrentlyReadingBook");
        return currentlyReadingBooks.remove(book);
    }
}
