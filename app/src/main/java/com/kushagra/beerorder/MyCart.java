package com.kushagra.beerorder;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kushagra.beerorder.adapters.ProductAdapter;
import com.kushagra.beerorder.dataModel.CartItem;
import com.kushagra.beerorder.interfaceToImplement.LinearLayoutManagerWrapper;
import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;

import java.util.ArrayList;
import java.util.List;

public class MyCart extends AppCompatActivity {
    //a list to store all the products
    List<CartItem> productList;

    //the recyclerview
    RecyclerView recyclerView;
    ImageView emptyCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface kushagra = Typeface.createFromAsset(getAssets(), "fonts/angelina.TTF");


        mTitle.setText("CART DETAILS");

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.all_cart_recycler);
        emptyCart = (ImageView) findViewById(R.id.empty_cart);

        recyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        productList = new ArrayList<>();
        DatabaseControllerAllQuery databaseControllerAllQuery =new DatabaseControllerAllQuery(getApplicationContext());
        productList=databaseControllerAllQuery.getCartItems();

        if(productList.size()<=0)
        {
            emptyCart.setVisibility(View.VISIBLE);
        }

        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(this, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
