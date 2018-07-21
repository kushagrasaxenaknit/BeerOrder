package com.kushagra.beerorder.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kushagra.beerorder.CartDetails;
import com.kushagra.beerorder.R;
import com.kushagra.beerorder.dataModel.Beer;
import com.kushagra.beerorder.interfaceToImplement.OnLoadMoreListener;
import com.kushagra.beerorder.interfaceToImplement.PaginationAdapterCallback;
import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;
import com.kushagra.beerorder.viewHolders.BeerViewHolder;

import java.util.List;

import static com.kushagra.beerorder.Utils.Constants.TABLENAME;


/**
 * Created by Kushagra Saxena on 26/03/2018.
 */


public class SongsPaginationDataAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static List<Beer> beerCurrentList;
    protected Handler handler;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private boolean endReached;
    private OnLoadMoreListener onLoadMoreListener;
    private PaginationAdapterCallback paginationAdapterCallback;
    private Context mContext;
    DatabaseControllerAllQuery databaseControllerAllQuery ;



    public SongsPaginationDataAdapter(List<Beer> songReceivedtList, RecyclerView recyclerView, Context context) {
        beerCurrentList = songReceivedtList;
        mContext=context;
        databaseControllerAllQuery = new DatabaseControllerAllQuery(mContext);
        handler=new Handler();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if(!endReached) {
                                if (!loading
                                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                    // End has been reached
                                    // Do something
                                    Log.i("load more IN ADPATER = ", "loading = " + loading
                                            + "(lastVisibleItem + visibleThreshold)" + (lastVisibleItem + visibleThreshold));
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (onLoadMoreListener != null) {
                                                onLoadMoreListener.onLoadMore();
                                            }
                                        }
                                    });

                                    loading = true;
                                }

                            }//end reached
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return beerCurrentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.beer_row_item, parent, false);

            vh = new BeerViewHolder(v,mContext,beerCurrentList);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Beer result = beerCurrentList.get(position); // Beer
        if (holder instanceof BeerViewHolder) {

            final BeerViewHolder songsVH = (BeerViewHolder) holder;

            songsVH.mMovieTitle.setText(result.getName());
            songsVH.style.setText(result.getStyle());
            songsVH.mMovieDesc.setText("Alcohol Content "+result.getAbv());
            songsVH.shineButton.setBtnColor(Color.GRAY);
            songsVH.shineButton.setBtnFillColor(Color.RED);
            songsVH.shineButton.setShapeResource(R.raw.heart);
            songsVH.addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Intent intent= new Intent(mContext,CartDetails.class);

                    intent.putExtra("id",result.getId());

                    intent.putExtra("name",result.getName());
                    intent.putExtra("style",result.getStyle());
                    intent.putExtra("ibu",result.getIbu());
                    intent.putExtra("abv",result.getAbv());
                    intent.putExtra("ounces",result.getOunces());


                    mContext.startActivity(intent);

                }
            });

            Beer olaData=beerCurrentList.get(songsVH.getAdapterPosition());


            if(databaseControllerAllQuery.getFavStatus(olaData,TABLENAME))
            {

                songsVH.shineButton.setChecked(true);
            }
            else
            {

                songsVH.shineButton.setChecked(false);
            }


            songsVH.shineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Beer olaData=beerCurrentList.get(songsVH.getAdapterPosition());

                    if(databaseControllerAllQuery.getFavStatus(olaData,TABLENAME))
                    {
                        databaseControllerAllQuery.removeSongsFavoriteStatus(beerCurrentList.get(songsVH.getAdapterPosition()));
                        // setCallbackListener
                        if(paginationAdapterCallback!=null)
                        {
                            paginationAdapterCallback.retryPageLoad();

                        }


                    }
                    else
                    {
                        databaseControllerAllQuery.setSongsFavoriteStatus(beerCurrentList.get(songsVH.getAdapterPosition()));



                    }
                }
            });





        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    public void setLoaded() {
        loading = false;
    }
    public void setEndReachedTrue() {
        endReached = true;
    }
    public void resetEndReached() {
        endReached = false;
    }

    @Override
    public int getItemCount() {
        return beerCurrentList.size();
    }

       public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setCallbackListener(PaginationAdapterCallback callbackListener) {
        this.paginationAdapterCallback = callbackListener;
    }




    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}