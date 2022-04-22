package com.example.tyminiproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.MessRecommendationApi.JsonPlaceHolderApi;
import com.example.tyminiproject.MessRecommendationApi.Mess;
import com.example.tyminiproject.MessRecommendationApi.RecommendMessAdaptor;
import com.example.tyminiproject.Model.Ratings;
import com.example.tyminiproject.Model.Request;
import com.example.tyminiproject.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessProfile extends AppCompatActivity  {

    private static final String TAG = "MessProfile";

    RatingBar ratingstars;
    String strRating;

    String messName, messOwner, messMob, messAddr, messOFFday, messTime, messBanner;
    ProgressBar progressBar;
    String strMob, strRole = null, foodChildName;
    TextView tvmessName, tvRmessName, tvRmessCategory, tvmessOwner, tvmessMob, tvmessAddr, tvmessOFFday, tvmessTime;
    ImageView imgMessBanner, imgBack;
    TextView textView,tvMessRating;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public int flag;
    RecyclerView recyclerView;
    RecommendMessAdaptor recommendMessAdaptor;
    List<Mess> messList;
    String customerId;
    Home home = new Home();
    EditText etRating, etComment;
    Common common;
    int sum = 0;
    int maxRating = 5;
    int totalUsers, strAvgRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_profile);

        ratingstars= findViewById(R.id.et_stars);


        tvMessRating = findViewById(R.id.tvMyRating);
        progressBar = findViewById(R.id.progMessProf);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerRMess);
        textView = findViewById(R.id.text_view_result);
        tvmessName = findViewById(R.id.tvMessName);
        tvmessOwner = findViewById(R.id.tvMessOwnerName);
        tvmessMob = findViewById(R.id.tvMessMob);
        tvmessAddr = findViewById(R.id.tvMessAddress);
        tvmessOFFday = findViewById(R.id.tvMessOFFday);
        tvmessTime = findViewById(R.id.tvMessTime);
        imgMessBanner = findViewById(R.id.imgMessBanner);
        imgBack = findViewById(R.id.imgBack);
        etRating = findViewById(R.id.et_rating);
        etComment = findViewById(R.id.et_Comment);

        tvRmessName = findViewById(R.id.RmessName);
        tvRmessCategory = findViewById(R.id.RmessCat);
        recyclerView.setVisibility(View.GONE);
        if (getIntent() != null) {
            strRole = getIntent().getStringExtra("role");
            Log.e(TAG, "inside onCreate : role---" + strRole);

            if (strRole.equals("owner")) {
                displayInfoToOwner();
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                //Log.e(TAG, "inside else onCreate : role---" + strRole);
            }
            if (strRole.equals("customer")) {
                displayInfoToCustomer();
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        ratingstars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.e(TAG, "inside onRatingChanged : v---" + v);

                int r= (int) v;
                Log.e(TAG, "inside onRatingChanged : r---" + r);
                strRating=String.valueOf(r);
                Log.e(TAG, "inside onRatingChanged : strRating---" + strRating);

            }
        });


    }

    private void commonInfo() {

        messName = Common.currentMessUser.getName();
        messOwner = Common.currentMessUser.getOwner();
        messMob = Common.currentMessUser.getPhone();
        messAddr = Common.currentMessUser.getAddress();
        messOFFday = Common.currentMessUser.getOffDay();
        messTime = Common.currentMessUser.getTime();
        messBanner = Common.currentMessUser.getImage();

        tvmessName.setText(messName);
        tvmessOwner.setText(messOwner);
        tvmessMob.setText(messMob);
        tvmessAddr.setText(messAddr);
        tvmessOFFday.setText(messOFFday);
        tvmessTime.setText(messTime);
        Picasso.with(getBaseContext()).load(messBanner)
                .into(imgMessBanner);
        Log.e(TAG, "commonInfo: tvmessMob"+tvmessMob);
        calculteRating(tvmessMob.getText().toString());
    }

    public void calculteRating(String strMessMob) {

        DatabaseReference ratings_table = FirebaseDatabase.getInstance().getReference("Ratings");
        ratings_table.orderByChild("messId").equalTo(strMessMob)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Log.i("Tag", "test1");

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            totalUsers++;
                            Log.d(TAG, "onDataChange: totalUsers" + totalUsers);
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object rating = map.get("rating");
                            int pValue = Integer.parseInt(String.valueOf(rating));
                            Log.d(TAG, "onDataChange: pValue" + pValue);
                            sum += pValue;
                            Log.d(TAG, "onDataChange: sum" + sum);
                        }
                        Log.d(TAG, "onDataChange: sum" + sum);
                        Log.d(TAG, "onDataChange: maxRating" + maxRating);
                        Log.d(TAG, "onDataChange: totalUsers" + totalUsers);
                        if (totalUsers!=0) {
                            strAvgRating = (sum * maxRating) / (totalUsers * maxRating);
                            Log.d(TAG, "onDataChange: avgRating" + strAvgRating);

                            Log.d(TAG, "onDataChange: strAvgRating" + strAvgRating);
                            setMessRating(strAvgRating);
                        }else {

                            setMessRating(0);

                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", databaseError.getMessage());
                    }


                });


    }

    private void setMessRating(int AvgRating) {
        String strAvgRating = String.valueOf(AvgRating);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference messUser_table = firebaseDatabase.getReference("MessUser");
        messUser_table.child(messMob).child("avgRating").setValue(strAvgRating);
        tvMessRating.setText(Common.currentMessUser.getAvgRating());
    }

    private void displayInfoToCustomer() {
        flag = 1;
        Log.e(TAG, "inside displayInfoToCustomer : flag---" + flag);
        commonInfo();
        callMessRecommendationApi();
    }

    private void callMessRecommendationApi() {


        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://mess-recommendation-api.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        messList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getRecommendedMess();
    }

    private void getRecommendedMess() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Mess>> listCall = jsonPlaceHolderApi.getPosts(messName);

        listCall.enqueue(new Callback<List<Mess>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<List<Mess>> call, Response<List<Mess>> response) {
                messList = response.body();
                Log.d("main activity  onResponse ", messList.toString());
                progressBar.setVisibility(View.GONE);
                recommendMessAdaptor = new RecommendMessAdaptor(getApplicationContext(), messList);
                recyclerView.setAdapter(recommendMessAdaptor);


            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<Mess>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textView.setText("MESS NOT FOUND IN DATASET ");
                Log.d("main activity   onFailure  ", t.toString());

            }
        });
    }

    private void displayInfoToOwner() {
        flag = 0;
        Log.e(TAG, "inside displayInfoToOwner : flag---" + flag);
        commonInfo();
    }

    public void onBack(View view) {

        if (flag == 1) {
            flag = 0;
            Log.e(TAG, "inside onBack IF : flag---" + flag);
            Intent i = new Intent(MessProfile.this, Home.class);
            startActivity(i);
            finish();

        } else {
            Log.e(TAG, "inside onBack ELSE : flag---" + flag);
            Intent i = new Intent(MessProfile.this, MessOwnerHome.class);
            startActivity(i);
            finish();


        }
    }

    public void saveRating(View view) {
        //String strRating = etRating.getText().toString();
        String strComment = etComment.getText().toString();
        if(strRating.isEmpty()){
            etRating.setError("enter rating");
        }else if(  strComment.isEmpty()){
            etComment.setError("enter comment");
        }
        else {
            String messId = messMob ;
            customerId = Common.currentUser.getPhone() ;
            Log.d(TAG, "onCreate: customerId :  "+customerId);
            Log.d(TAG, "onDataChange: messId    "+messId);
            Log.d(TAG, "onDataChange: strRating "+strRating);
            Log.d(TAG, "onDataChange: strComment    "+strComment);


            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference ratings_table = firebaseDatabase.getReference("Ratings");
            Ratings newRating = new Ratings(customerId,messId,strRating,strComment);

            ratings_table.child(customerId).setValue(newRating);
            Toast.makeText(MessProfile.this, "Ratings submitted", Toast.LENGTH_SHORT).show();

            calculteRating(tvmessMob.getText().toString());
        }
    }
}