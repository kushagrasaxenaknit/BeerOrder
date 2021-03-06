package com.kushagra.beerorder.Utils;

import android.os.Environment;
import android.util.Log;

import com.kushagra.beerorder.dataModel.Beer;

import java.util.List;


/**
 * Created by Kushagra Saxena on 25/03/2018.
 */

public class Constants {

    //Api
    public static final String BASE_URL = "http://starlord.hackerearth.com/";
   // public static final String BASE_URL = "https://api.myjson.com/bins/";

    public static final String EXTENDED_URL = "beercraft";
 //   public static final String EXTENDED_URL = "16tumn";

    //Fragment Names
    public static final String ALL_BEER = "Beer List";
    public static final String DOWNLOADED  = "Downloaded";
    public static final String FAVOURITES = "Favourites";
    public static final String RECENT = "Most Played";

    //TABLE NAME
    public static final String TABLENAME = "Item";

    public static final int ITEM_PER_PAGE = 15;
//File Storage Path
    public static final String storagePath=Environment.getExternalStorageDirectory().toString()+"/Paging Music/";
    public static void printOlaData(Beer olaData)
    {
        Log.i("oladata value =", olaData.getName()+" "+ olaData.getId());

    }
    public static void printOlaDataList( List<Beer> olaDataList)
    {
        Log.i("LOg list value =", "size is "+olaDataList.size());

        for(Beer beer :olaDataList)
        {
            Log.i("oladata value =", beer.getName()+" "+ beer.getId());

        }

    }
}
