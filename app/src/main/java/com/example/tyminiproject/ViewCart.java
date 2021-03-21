package com.example.tyminiproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tyminiproject.Common.Common;
import com.example.tyminiproject.Interface.ItemClickListner;
import com.example.tyminiproject.Model.Cart;
import com.example.tyminiproject.Model.Request;
import com.example.tyminiproject.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class ViewCart extends AppCompatActivity {


    private static final String TAG = "Cart";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference cart;
    StorageReference storageReference;
    TextView tvTotal;
    Button btnPlaceOrder;
    String custMob = "";
    int total = 0;
    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    String foodName, foodTotalPrice, cartId, MessName, foodQuantity;
    FloatingActionButton fabDelete;

    String foodId = "", custName = "", phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_view_cart);
        recyclerView = findViewById(R.id.listCart);

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        fabDelete = findViewById(R.id.fabDelete);

        database = FirebaseDatabase.getInstance();
        cart = database.getReference("Cart");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        custName = Common.currentUser.getName();
        phone = Common.currentUser.getPhone();
        Log.e(TAG, "inside onCreate : custName---" + custName);
        Log.e(TAG, "inside onCreate : phone---" + phone);
        if (getIntent() != null)
            custMob = getIntent().getStringExtra("customerMob");
        Log.e(TAG, "inside onCreate : custMob---" + custMob);
        if (!custMob.isEmpty() && custMob != null) {
            loadCartList(custMob);
        }

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(Cart.this, Cart.class);
                //  startActivity(i);

                cleanMyCart();
                Toast.makeText(ViewCart.this, "Cart is Cleaned", Toast.LENGTH_LONG).show();
               // loadCartList(custMob);
            }
        });


    }

    private void cleanMyCart() {
        DatabaseReference cart_table = FirebaseDatabase.getInstance().getReference("Cart");

        cart_table.orderByChild("phone").equalTo(custMob)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.i("Tag", "test1");

                        for (DataSnapshot postsnapshot :dataSnapshot.getChildren())  {
                            Log.i("Tag", "test2");
                            String key = postsnapshot.getKey();
                            postsnapshot.getRef().removeValue();
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", databaseError.getMessage());
                    }

                });

        /*FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference cart_table = firebaseDatabase.getReference("Cart");
        cart_table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(custMob).exists()) {
                    cart_table.child(cartId).removeValue();
                  //  Toast.makeText(V.this, "Food Deleted!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ViewCart.this, "Food Not Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    private void loadCartList(String custMob) {
        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(Cart.class, R.layout.cart_item,
                CartViewHolder.class, cart.orderByChild("phone").equalTo(custMob)) {
            @Override
            protected void populateViewHolder(CartViewHolder cartViewHolder, Cart model, int i) {
                cartViewHolder.cartItemName.setText(model.getMenu());

                final Cart local = model;
                cartId = adapter.getRef(i).getKey();
                foodName = local.getMenu();
                foodTotalPrice = local.getTotalPrice();
                MessName = local.getMessName();
                foodQuantity = local.getQuantity();
                Log.d(TAG, "populateViewHolder: foodName: " + foodName);
                Log.d(TAG, "populateViewHolder: foodPrice: " + foodTotalPrice);
                Log.d(TAG, "populateViewHolder: MessName: " + MessName);
                Log.d(TAG, "populateViewHolder: foodQuantity: " + foodQuantity);
                cartViewHolder.cartItemName.setText(foodName);
                cartViewHolder.cartItemPrice.setText(foodTotalPrice);
                cartViewHolder.cartItemCount.setText(foodQuantity);
                cartViewHolder.tvCartMessName.setText(MessName);
                cartViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        placeNewOrder();
                    }
                });
                //orderViewHolder.OrderMessName.setText(strOrderMessName);
            }
        };


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void placeNewOrder() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference requests_table = firebaseDatabase.getReference("Requests");
        Request newOrder = new Request(MessName, foodName, foodTotalPrice, foodQuantity, custName, phone);

        requests_table.child(String.valueOf(System.currentTimeMillis())).setValue(newOrder);

        Toast.makeText(ViewCart.this, "Thank you!!", Toast.LENGTH_LONG).show();
        deleteFromCart();

    }

    private void deleteFromCart() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference cart_table = firebaseDatabase.getReference("Cart");
        cart_table.child(cartId).removeValue();

        Toast.makeText(ViewCart.this, "deleted from cart!!", Toast.LENGTH_LONG).show();
        loadCartList(custMob);
    }
       /* for (Order order : carts) {

            int intPrice = (Integer.parseInt(order.getPrice()));
            Log.e(TAG, "inside loadFoodList : intPrice : " + intPrice);
            int intQuantity = (Integer.parseInt(order.getQuantity()));
            Log.e(TAG, "inside loadFoodList : intQuantity : " + intQuantity);
            total += intPrice * intQuantity;
            Log.e(TAG, "inside loadFoodList : total : " + total);

        }
        // Locale locale = new Locale("en", "US");

        String strTtotal = String.valueOf(total);
        // NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        //tvTotal.setText(fmt.format(total));
        tvTotal.setText(strTtotal);*/


    /*private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewCart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("enter your Address : ");
        final EditText editText = new EditText(ViewCart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        String phone = Common.currentUser.getPhone();
        Log.e(TAG, "inside showAlertDialog : " + phone);
        String name = Common.currentUser.getName();
        Log.e(TAG, "inside showAlertDialog : " + name);
//        String MessName = Common.currentFood.getMessName();
        //      Log.e(TAG, "inside showAlertDialog : " + MessName);
        String price = String.valueOf(total);
        Log.e(TAG, "inside showAlertDialog : " + price);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        phone,
                        name,
                        editText.getText().toString(),
                        price,
                       carts
                );
         //       requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(ViewCart.this, "Thank you!!", Toast.LENGTH_LONG).show();
                finish();
            }


        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
*/
}