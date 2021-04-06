package com.example.tyminiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.MessUser;
import com.example.tyminiproject.SignUp.MessOwnerSignUp;
import com.example.tyminiproject.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Home";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView, userName, tvMob;
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<MessUser, MenuViewHolder> adapter;
    String strMob, isMessOwner;
    Button btn_ViewMess;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_shopping_cart_solid);
        */toolbar.setTitle("Menu");
        //  setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        navigationView.setCheckedItem(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.userName);
        String custName = Common.currentUser.getName();
        userName.setText(custName);

        tvMob = headerView.findViewById(R.id.tv_mob);
        tvMob.setText(Common.currentUser.getPhone());

        isMessOwner = Common.currentUser.getMessOwner();
        Log.d(TAG, "onCreate: isMessOwner : " + isMessOwner);

        Menu menu = navigationView.getMenu();
        for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            MenuItem menuItem = menu.getItem(menuItemIndex);
            if (menuItem.getItemId() == R.id.nav_messReg) {
                if (isMessOwner.equals("YES")) {
                    Log.d(TAG, "onCreate IF: isMessOwner : " + isMessOwner);
                    menuItem.setVisible(false);
                } else {
                    Log.d(TAG, "onCreate ELSE: isMessOwner : " + isMessOwner);
                    menuItem.setVisible(true);
                }
            }
        }

        strMob = tvMob.getText().toString();
        Log.d(TAG, "onCreate: nav nar MOB NO : " + strMob);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ViewCart.class);
                i.putExtra("customerMob", strMob);
                startActivity(i);
                //  Toast.makeText(Home.this, "FAB   Click", Toast.LENGTH_LONG).show();
            }
        });
        database = FirebaseDatabase.getInstance();
        category = database.getReference("MessUser");


        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();

    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<MessUser, MenuViewHolder>(MessUser.class, R.layout.mess_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, MessUser model, int i) {
                menuViewHolder.MessName.setText(model.getName());
                menuViewHolder.tvOwmner.setText(model.getOwner());
                menuViewHolder.tvTime.setText(model.getTime());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(menuViewHolder.MenuImage);

                MessUser clickItem = model;
                menuViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this, "" + clickItem.getName(), Toast.LENGTH_LONG).show();
                        //Get categoryId & sent it to new activity
                        Intent i = new Intent(Home.this, FoodList.class);
                        i.putExtra("MessId", adapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_menu:
                Toast.makeText(Home.this, "home   Click", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Home.this, Home.class);
                startActivity(intent);
                break;

            case R.id.nav_cart:
                // Toast.makeText(Home.this, "bus Favorite Click", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Home.this, ViewCart.class);
                i.putExtra("customerMob", strMob);
                startActivity(i);
                break;
            case R.id.nav_Myorders:
                // Toast.makeText(Home.this, "bus Favorite Click", Toast.LENGTH_LONG).show();
                Intent i4 = new Intent(Home.this, OrderStatus.class);
                i4.putExtra("customerMobNo", strMob);
                startActivity(i4);
                break;

            case R.id.nav_logOut:
                Intent i2 = new Intent(Home.this, SignIn.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
                break;

            case R.id.nav_messReg:
                String custName = userName.getText().toString();
                Intent i3 = new Intent(Home.this, MessOwnerSignUp.class);
                i3.putExtra("mobileNo", strMob);
                i3.putExtra("custName", custName);
                startActivity(i3);

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
