package com.example.tyminiproject.Common;

import android.util.Log;

import com.example.tyminiproject.Model.Food;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.Model.Ratings;
import com.example.tyminiproject.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Common {
    private static final String TAG = "Common";
    public static User currentUser;

    public static MessUser currentMessUser;
    public static Ratings currentRating;
    public static Food currentFood;

    public static final String UPDATE = "Update";

    public static final String DELETE = "Delete";
   /* private static int totalUsers;
    private static int sum;
    private static int maxRating = 5;*/



}
