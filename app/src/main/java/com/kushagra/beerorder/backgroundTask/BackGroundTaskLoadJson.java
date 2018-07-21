package com.kushagra.beerorder.backgroundTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.kushagra.beerorder.Api.MovieApi;
import com.kushagra.beerorder.Api.MovieService;
import com.kushagra.beerorder.R;
import com.kushagra.beerorder.Utils.CheckInternetConnection;
import com.kushagra.beerorder.dataModel.Beer;
import com.kushagra.beerorder.interfaceToImplement.OnTaskCompleted;
import com.kushagra.beerorder.interfaceToImplement.PaginationAdapterCallback;
import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kushagra.beerorder.Utils.Constants.TABLENAME;

/**
 * Created by Kushagra Saxena on 26/03/2018.
 */

public class BackGroundTaskLoadJson  {

    public OnTaskCompleted listener = null;//Call back interface
    private MovieService movieService;
    private PaginationAdapterCallback mCallback;
    DatabaseControllerAllQuery databaseControllerAllQuery;

    Context context;


    List<Beer> resultList  = new ArrayList<>();
    public BackGroundTaskLoadJson(Context context1, OnTaskCompleted listener1,PaginationAdapterCallback paginationAdapterCallback) {
        context = context1;
        mCallback = paginationAdapterCallback;
        listener = listener1;   //Assigning call back interface  through constructor

        //init service and load data
        movieService = MovieApi.getClient().create(MovieService.class);

        databaseControllerAllQuery = new DatabaseControllerAllQuery(context);
    }



    //load the json data from url
    public void loadDataIntoList()
    {


        callTopRatedMoviesApi().enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                // Got data. Send it to adapter

                mCallback.hideErrorView();
                Log.i("Loading Page  number", "loadFirstPage: "+response);

                resultList = response.body();
                Log.i("result list", resultList.toString());
                //First insert list then do anything
               // insertToSql(resultList); ??????


                listener.onTaskCompleted(resultList);
                //Collections.sort(results,OlaData.OlaDataSongArtist);

            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                t.printStackTrace();
                mCallback.showErrorView(fetchErrorMessage(t));
            }
        });

    }

    private Call<List<Beer>> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies();
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = context.getResources().getString(R.string.error_msg_unknown);
        CheckInternetConnection checkInternetConnection=new CheckInternetConnection();
        if (!(checkInternetConnection.isNetworkConnected(context))    ) {
            errorMsg = context.getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = context.getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }
    public void insertToSql(List<Beer>  response) {
        for(Beer olaData: response)
        {
            databaseControllerAllQuery.insertBeer(olaData,TABLENAME);

        }


    }
}