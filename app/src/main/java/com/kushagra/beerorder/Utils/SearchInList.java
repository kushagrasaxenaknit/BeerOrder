package com.kushagra.beerorder.Utils;

import com.kushagra.beerorder.dataModel.Beer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kushagra Saxena on 30/06/2018.
 */

public class SearchInList {

    public List<Beer> searchResult(String query,List<Beer> list)
    {
        List<Beer>  response=new ArrayList<Beer>() ;
        for(Beer beer :list)
        {
            if(beer.getName().toLowerCase().contains(query.toLowerCase()))
            {
                response.add(beer);
            }
        }
        return response;
    }
}
