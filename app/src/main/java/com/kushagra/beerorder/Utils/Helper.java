package com.kushagra.beerorder.Utils;

import android.content.Context;

import com.kushagra.beerorder.dataModel.Beer;
import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Kushagra Saxena on 30/06/2018.
 */

public class Helper {

    public List<String> getFilterOptions( List<Beer> list)
    {
        List<String>  response=new ArrayList<>() ;
        HashSet <String> hashSet =new HashSet<>();
        for(Beer beer :list)
        {
            hashSet.add(beer.getStyle());

        }
        response.addAll(hashSet);
        return response;
    }
    public List<Beer> applySort( List<Beer> list,int position)
    {

        if(position==1)
        {
            Collections.sort(list);
        }
        else if(position==2)
        {
            Collections.sort(list);
            Collections.reverse(list);
        }
        return list;
    }
    public List<Beer> applyFilter( List<Beer> list,String style)
    {
        List<Beer>  response=new ArrayList<>() ;
        for(Beer beer :list)
        {
            if(beer.getStyle().compareToIgnoreCase(style)==0)
            {
                response.add(beer);
            }

        }
        return response;
    }
    public List<Beer> showFav(Context context)
    {
        DatabaseControllerAllQuery databaseControllerAllQuery = new DatabaseControllerAllQuery(context);

        List<Beer>  response=databaseControllerAllQuery.getFavTuple();

        return response;
    }
}
