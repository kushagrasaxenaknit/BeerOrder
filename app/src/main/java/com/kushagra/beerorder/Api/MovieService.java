package com.kushagra.beerorder.Api;

import com.kushagra.beerorder.dataModel.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.kushagra.beerorder.Utils.Constants.EXTENDED_URL;

/**
 * Created by Kushagra Saxena on 18/03/2018.
 */

public interface MovieService {

    @GET(EXTENDED_URL)
    Call<List<Beer>> getTopRatedMovies();

}
