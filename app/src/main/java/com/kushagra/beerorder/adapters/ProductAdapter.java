package com.kushagra.beerorder.adapters;

/**
 * Created by Kushagra Saxena on 01/07/2018.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.kushagra.beerorder.R;
import com.kushagra.beerorder.dataModel.CartItem;
import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;

import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<CartItem> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<CartItem> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cart_item, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        //getting the product of the specified position
        CartItem product = productList.get(position);

        //binding the data with the viewholder views
        holder.name.setText(product.getName());
        holder.style.setText(product.getStyle());
        holder.content.setText("Alcohol Content : "+String.valueOf(product.getAbv()));
        holder.quantity.setText("Quantity : "+String.valueOf(product.getQuantity()));
        // Set a click listener for item remove button
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the clicked item label
                String itemLabel = productList.get(position).getName();

                // Remove the item on remove/button click
                DatabaseControllerAllQuery databaseControllerAllQuery =new DatabaseControllerAllQuery(mCtx);
                databaseControllerAllQuery.deleteCartItem(productList.get(position).getLocalId());

                productList.remove(position);


                /*
                    public final void notifyItemRemoved (int position)
                        Notify any registered observers that the item previously located at position
                        has been removed from the data set. The items previously located at and
                        after position may now be found at oldPosition - 1.

                        This is a structural change event. Representations of other existing items
                        in the data set are still considered up to date and will not be rebound,
                        though their positions may be altered.

                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(position);

                /*
                    public final void notifyItemRangeChanged (int positionStart, int itemCount)
                        Notify any registered observers that the itemCount items starting at
                        position positionStart have changed. Equivalent to calling
                        notifyItemRangeChanged(position, itemCount, null);.

                        This is an item change event, not a structural change event. It indicates
                        that any reflection of the data in the given position range is out of date
                        and should be updated. The items in the given range retain the same identity.

                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(position,productList.size());

                // Show the removed item label
                Toast.makeText(mCtx,"Removed : " + itemLabel, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, style, content, quantity;
        ImageButton delete;

        public ProductViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            style = itemView.findViewById(R.id.style);
            content = itemView.findViewById(R.id.Acontent);
            quantity = itemView.findViewById(R.id.quantity);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}