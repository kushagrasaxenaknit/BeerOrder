package com.kushagra.beerorder;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;

import static com.kushagra.beerorder.Utils.Constants.ALL_BEER;

public class CartDetails extends AppCompatActivity {
private TextView name,style,bitter,size,content,id,quantity;

String sname,sstyle,sbitter,ssize,scontent,sid,squantity;
double alcohol=0.0;

ImageButton addCart,plus,minus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        Intent intent = getIntent();

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

      TextView  mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface kushagra = Typeface.createFromAsset(getAssets(), "fonts/angelina.TTF");


            mTitle.setText("BEER DETAILS");

        sid = intent.getIntExtra("id",0) +"";

        sname = intent.getStringExtra("name");
        sstyle = intent.getStringExtra("style");
        sbitter = intent.getStringExtra("ibu");
        scontent = intent.getStringExtra("abv");
        alcohol=intent.getDoubleExtra("ounces",0.0);
        ssize = intent.getDoubleExtra("ounces",0.0)+"";
        sname = intent.getStringExtra("name");

        name = (TextView) findViewById(R.id.name);
        style = (TextView) findViewById(R.id.style);
        bitter = (TextView) findViewById(R.id.bitter);
        size = (TextView) findViewById(R.id.size);
        content = (TextView) findViewById(R.id.content);
        id = (TextView) findViewById(R.id.productId);
        quantity = (TextView) findViewById(R.id.quantity);
        addCart = (ImageButton) findViewById(R.id.addToCart);
        plus = (ImageButton) findViewById(R.id.plus);
        minus = (ImageButton) findViewById(R.id.minus);






        name.setTypeface(kushagra);
        name.setText(sname);

        style.setText(sstyle);
        bitter.setText("Bitter : "+sbitter);
        size.setText("Size : "+ssize+ " ounces");
        content.setText("Alcohol Content : "+scontent);
        id.setText("Product Id : "+sid);
        quantity.setText("1");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(quantity.getText().toString())==99)
                {

                }
                else
                {
                    quantity.setText(   (Integer.parseInt(quantity.getText().toString())+1  )+"");

                }
                  }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(quantity.getText().toString())==1)
                {
                    Toast.makeText(getApplicationContext(), "BUY MINIMUM ONE", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    quantity.setText(   (Integer.parseInt(quantity.getText().toString())-1 )+"" );

                }

            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseControllerAllQuery databaseControllerAllQuery =new DatabaseControllerAllQuery(getApplicationContext());
                int qty=(Integer.parseInt(quantity.getText().toString()));
                int id=(Integer.parseInt(sid));
                databaseControllerAllQuery.insertBeerCart(sname,scontent,sbitter,sstyle,alcohol,qty,id,"Cart");
                Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                finish();


            }
        });

    }
}
