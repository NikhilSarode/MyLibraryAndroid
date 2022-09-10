package com.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavouritesActivity extends AppCompatActivity {

    private TextView txtInternet;
    private Button btnFetch,btnSave;
    private Retrofit retrofit;
    private PostEndpoint postEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        txtInternet = findViewById(R.id.txtInternet);
        btnFetch = findViewById(R.id.btnFetch);
        btnSave = findViewById(R.id.btnSave);

        /*for debugging. Logs of the http call can be seen in logcat. filter by okhttp*/
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
        postEndpoint = retrofit.create(PostEndpoint.class);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void onGetAllPosts(List<Post> posts){
        txtInternet.setText("Post1="+ posts.get(0).getTitle()+"\n"+"Post2="+ posts.get(1).getTitle());
    }

    private void onSavePost(Post savedPost){
        txtInternet.setText("saved post title="+savedPost.getTitle());
    }

    private void fetchData(){
        Call<List<Post>> call= postEndpoint.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> posts = response.body();
                onGetAllPosts(posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("nikhil endpoint call dailed get posts");
            }
        });
    }

    private void saveData(){
        Post post = new Post(15785,9,"test title","test description");
        Call<Post> call= postEndpoint.savePost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                onSavePost(post);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

}