package com.kushagra.beerorder.localDatabase;

/**
 * Created by Kushagra Saxena on 26/03/2018.
 */


        import android.content.ContentValues;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
        import com.kushagra.beerorder.dataModel.Beer;
        import com.kushagra.beerorder.dataModel.CartItem;


        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Locale;



        import static com.kushagra.beerorder.Utils.Constants.ITEM_PER_PAGE;
        import static com.kushagra.beerorder.Utils.Constants.printOlaData;


/**
 * Created by Kushagra Saxena on 11/10/2017.
 */


public class DatabaseControllerAllQuery extends SQLiteOpenHelper {

    Context context = null;
    private Boolean firstTime = null;

    public DatabaseControllerAllQuery(Context applicationcontext) {
        super(applicationcontext, "beer.db", null, 1);

        context = applicationcontext;


    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {

        String query;

        //home
        query = "CREATE TABLE IF NOT EXISTS Item( id INTEGER PRIMARY KEY, name TEXT, abv TEXT , ibu TEXT , style TEXT , ounces REAL ,favorite INTEGER DEFAULT 0,globalId INTEGER )";
        database.execSQL(query);
        query = "CREATE TABLE IF NOT EXISTS Cart( id INTEGER PRIMARY KEY, name TEXT, abv TEXT , ibu TEXT , style TEXT , ounces REAL ,quantity INTEGER ,globalId INTEGER )";
        database.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Item";
        database.execSQL(query);
        query = "DROP TABLE IF EXISTS Cart";
        database.execSQL(query);
        onCreate(database);
    }


    /**
     * Inserts Beer into SQLite DB
     *
     *
     */
    public void insertBeer(Beer olaData, String tableName) {

        if(tableName.compareTo("Item")==0) {

            SQLiteDatabase database = this.getWritableDatabase();

            Cursor cursor = null;
            String sql ="SELECT id FROM "+tableName+" where globalId = '" + olaData.getId() + "'";
            cursor= database.rawQuery(sql,null);
            Log.i("Count of insert tuple","song name"+olaData.getName()+"Cursor Count : " + cursor.getCount());

            if(cursor.getCount()<=0)
            {

                //Song Not Found

                ContentValues values = new ContentValues();
                values.put("name", olaData.getName());
                values.put("abv", olaData.getAbv());
                values.put("ibu", olaData.getIbu());
                values.put("style", olaData.getStyle());
                values.put("ounces", olaData.getOunces());
                values.put("globalId", olaData.getId());


                database.insert(tableName, null, values);

                Log.i("inserted Song=", values.getAsString("name")+" "+ values.getAsString("globalId") );


            }
            cursor.close();

            database.close();

        }


    }

    /**
     * Inserts Beer into SQLite DB
     *
     *
     */
    public void insertBeerCart(String name,String content,String bitter,String style,Double ounces,int quantity,int id, String tableName) {

        if(tableName.compareTo("Cart")==0) {

            SQLiteDatabase database = this.getWritableDatabase();




                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("abv",content);
                values.put("ibu", bitter);
                values.put("style", style);
                values.put("ounces", ounces);
            values.put("quantity", quantity);
                values.put("globalId", id);


                database.insert(tableName, null, values);

                Log.i("inserted Song=", values.getAsString("name")+" "+ values.getAsString("globalId") );




            database.close();

        }


    }

    public boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = context.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            //int second=mPreferences.edit();
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }




    //get  favourite status
    public boolean getFavStatus(Beer olaData, String tableName) {

        String fav = "";
        String selectQuery = "SELECT  * FROM " + tableName + " where globalId = '" + olaData.getId() + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Log.i("mobileId=", cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3));

                fav = cursor.getString(cursor.getColumnIndex("favorite"));


            } while (cursor.moveToNext());
        }
        cursor.close();

        database.close();
        if((fav!="")&&Integer.parseInt(fav.trim())==1)
        {
            return true;
        }
        else
        {
            return false;
        }



    }
    //get  id corresponding to songName
    public String getId(String songName, String tableName) {

        String mobileId = "";
        String selectQuery = "SELECT  * FROM " + tableName + " where songName = '" + songName + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Log.i("mobileId=", cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3));

                mobileId = cursor.getString(cursor.getColumnIndex("id"));


            } while (cursor.moveToNext());
        }
        cursor.close();

        database.close();


        return mobileId.trim();
    }






    /**
     * Set favourite song into SQLite DB
     *
     *
     */
    public void setSongsFavoriteStatus(Beer olaData) {


        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update Item set favorite = '" + "1"
               + "' where globalId=" + "'" + olaData.getId() + "'";

        Log.i("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    public void removeSongsFavoriteStatus(Beer olaData) {


        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update Item set favorite = '" + "0"
                + "' where globalId=" + "'" + olaData.getId() + "'";

        Log.i("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }




    /**
     * Get list of Songs from SQLite DB as  List
     *
     *
     */
    public List<Beer>  getBeerOfPage(String tableName,int page) {
        Log.i("getSongOfPage =", page+" " );

        int start = (page-1)*ITEM_PER_PAGE;
        int itemToDisplay =ITEM_PER_PAGE;
        int totalTuple=numberOfTuple(tableName);
        if((start+itemToDisplay)>totalTuple)
        {
            itemToDisplay=totalTuple-start;
        }

        Log.i("start index  =", start+" " );
        Log.i("itemToDisplay index  =", itemToDisplay+" " );

        List<Beer> resultSongList  = new ArrayList<>();;

        String selectQuery = "SELECT  * FROM " + tableName+ "  LIMIT " + itemToDisplay + " OFFSET " + start;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Beer olaData = new Beer();
               // olaData.setId(Integer.parseInt( cursor.getString(0)));
                olaData.setName(( cursor.getString(1)));
                olaData.setAbv(( cursor.getString(2)));
                olaData.setIbu( cursor.getString(3));
                olaData.setStyle(( cursor.getString(4)));
                olaData.setOunces(Double.parseDouble( cursor.getString(5)));
                if(Integer.parseInt( cursor.getString(6))==1)
                {
                    olaData.setFavourite(true);
                }
                else
                {
                    olaData.setFavourite(false);
                }
                olaData.setId(Integer.parseInt( cursor.getString(7)));

                //log to see
                printOlaData(olaData);





                // Log.i("search=", cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3));
                resultSongList.add(olaData);
            } while (cursor.moveToNext());
        }
        cursor.close();


        database.close();
        return resultSongList;
    }
    /**
     * Get list of searched Songs from SQLite DB as  List
     *
     * @return List<OlaData>
     */
    public List<Beer>  getSearchedBeerByPage(String tableName,int page,String searchText) {
        Log.i("getSearchedSongByPage =", page+" " );

        int start = (page-1)*ITEM_PER_PAGE;
        int itemToDisplay =ITEM_PER_PAGE;
        int totalTuple=numberOfTupleInSearchResult(tableName,searchText);
        if((start+itemToDisplay)>totalTuple)
        {
            itemToDisplay=totalTuple-start;
        }

        Log.i("start index  =", start+" " );
        Log.i("itemToDisplay index  =", itemToDisplay+" " );

        List<Beer> resultSongList  = new ArrayList<>();
        if(itemToDisplay<=0)
        {
            return  resultSongList;
        }

        String selectQuery = "SELECT  * FROM " + tableName+ "  WHERE name LIKE '%" +searchText + "%' "+"  LIMIT " + itemToDisplay + " OFFSET " + start; ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                Beer olaData = new Beer();
               // olaData.setId(Integer.parseInt( cursor.getString(0)));
                olaData.setName(( cursor.getString(1)));
                olaData.setAbv(( cursor.getString(2)));
                olaData.setIbu( cursor.getString(3));
                olaData.setStyle(( cursor.getString(4)));
                olaData.setOunces(Double.parseDouble( cursor.getString(5)));
                if(Integer.parseInt( cursor.getString(6))==1)
                {
                    olaData.setFavourite(true);
                }
                else
                {
                    olaData.setFavourite(false);
                }
                olaData.setId(Integer.parseInt( cursor.getString(7)));







                // Log.i("search=", cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3));
                resultSongList.add(olaData);
            } while (cursor.moveToNext());
        }

cursor.close();
        database.close();
        return resultSongList;
    }


    /**
     * Get SQLite number of records
     *
     * @return
     */
    public int numberOfTuple(String TABLE_NAME) {


        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        int totalRecords = cursor.getCount();
        cursor.close();
        database.close();
        return totalRecords;
    }
   // return all favourite beer
    public List<Beer>  getFavTuple() {
        Log.i("get Fav ="," " );



        List<Beer> resultSongList  = new ArrayList<>();


        String TABLE_NAME="Item";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME+" where favorite = '" + "1" + "'" ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                Beer olaData = new Beer();
                // olaData.setId(Integer.parseInt( cursor.getString(0)));
                olaData.setName(( cursor.getString(1)));
                olaData.setAbv(( cursor.getString(2)));
                olaData.setIbu( cursor.getString(3));
                olaData.setStyle(( cursor.getString(4)));
                olaData.setOunces(Double.parseDouble( cursor.getString(5)));
                if(Integer.parseInt( cursor.getString(6))==1)
                {
                    olaData.setFavourite(true);
                }
                else
                {
                    olaData.setFavourite(false);
                }
                olaData.setId(Integer.parseInt( cursor.getString(7)));







                // Log.i("search=", cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3));
                resultSongList.add(olaData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return resultSongList;
    }

    // returns list of items in cart
    public List<CartItem>  getCartItems() {
        Log.i("get getCartItems ="," " );



        List<CartItem> resultList  = new ArrayList<>();


        String TABLE_NAME="Cart";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem();
                item.setLocalId(Integer.parseInt( cursor.getString(0)));
                item.setName(( cursor.getString(1)));
                item.setAbv(( cursor.getString(2)));
                item.setIbu( cursor.getString(3));
                item.setStyle(( cursor.getString(4)));
                item.setOunces(Double.parseDouble( cursor.getString(5)));
                item.setQuantity(Integer.parseInt( cursor.getString(6)));

                item.setId(Integer.parseInt( cursor.getString(7)));







                // Log.i("search=", cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3));
                resultList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return resultList;
    }

    /**
     * Get SQLite number of records in search
     *
     * @return
     */

    public int numberOfTupleInSearchResult(String TABLE_NAME,String searchText) {


        String selectQuery = "SELECT  * FROM " + TABLE_NAME+ "  WHERE name LIKE '%" +searchText + "%' " ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int totalRecords=cursor.getCount();
        cursor.close();
        database.close();
        return totalRecords;
    }

    public int deleteCartItem(int id) {


        String TABLE_NAME = "Cart";
        String selectQuery = "DELETE FROM " + TABLE_NAME + " WHERE id "  + "= '" + id + "'" ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int totalRecords=cursor.getCount();
        cursor.close();
        database.close();
        return totalRecords;
    }
    //give timestamp
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }




}