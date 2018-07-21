package com.kushagra.beerorder.interfaceToImplement;

import com.kushagra.beerorder.dataModel.Beer;

import java.util.List;


/**
 * Created by Kushagra Saxena on 26/03/2018.
 */

public interface OnTaskCompleted{

    void onTaskCompleted(List<Beer>  response);
}