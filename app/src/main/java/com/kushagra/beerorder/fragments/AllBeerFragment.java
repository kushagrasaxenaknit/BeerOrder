package com.kushagra.beerorder.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kushagra.beerorder.MyCart;
import com.kushagra.beerorder.R;
import com.kushagra.beerorder.Utils.CheckInternetConnection;
import com.kushagra.beerorder.Utils.Helper;
import com.kushagra.beerorder.Utils.SearchInList;
import com.kushagra.beerorder.adapters.SongsPaginationDataAdapter;
import com.kushagra.beerorder.backgroundTask.BackGroundTaskLoadJson;
import com.kushagra.beerorder.dataModel.Beer;
import com.kushagra.beerorder.interfaceToImplement.LinearLayoutManagerWrapper;
import com.kushagra.beerorder.interfaceToImplement.OnLoadMoreListener;
import com.kushagra.beerorder.interfaceToImplement.OnTaskCompleted;
import com.kushagra.beerorder.interfaceToImplement.PaginationAdapterCallback;
import com.kushagra.beerorder.localDatabase.DatabaseControllerAllQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;



import retrofit2.Call;

import static com.kushagra.beerorder.Utils.Constants.ITEM_PER_PAGE;
import static com.kushagra.beerorder.Utils.Constants.TABLENAME;
import static com.kushagra.beerorder.Utils.Constants.printOlaDataList;


/**
 * Created by Kushagra Saxena on 25/03/2018.
 */


public class AllBeerFragment extends Fragment  implements OnTaskCompleted,PaginationAdapterCallback {
    private static final String TAG = "AllBeerFragment";




    private RecyclerView mRecyclerView;
    private SongsPaginationDataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    // to keep track which pages loaded and next pages to load
    private  int pageNumber;
    private Context context;
    private List<Beer> beerList;
    private List<Beer> allBeerList,orignalList;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    private boolean isSearchQuery=false;
    private String searchQuery="";
    Helper helper;
    // Spinner element
    Spinner sortSpinner ;
    // Spinner element
    Spinner filterSpinner ;

//    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    // DB Class to perform DB related operations
    DatabaseControllerAllQuery databaseControllerAllQuery ;
    protected Handler handler;
    CheckInternetConnection checkInternetConnection;
    SwipeRefreshLayout pullToRefresh;
    public AllBeerFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//have this line of code to have options menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.all_beer_fragment, container, false);

        //Context
        context = getContext();
        databaseControllerAllQuery = new DatabaseControllerAllQuery(context);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_songs_recycler);

        sortSpinner = (Spinner) rootView.findViewById(R.id.spinner1);
        filterSpinner = (Spinner) rootView.findViewById(R.id.spinner2);


        pageNumber = 1;
        beerList = new ArrayList<Beer>();
        allBeerList = new ArrayList<Beer>();

        handler = new Handler();
        helper = new Helper();
        checkInternetConnection=new CheckInternetConnection();

        progressBar = (ProgressBar) rootView.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);
        txtError = (TextView) rootView.findViewById(R.id.error_txt_cause);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);
         pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getWebServiceData(); // your code

            }
        });


        mLayoutManager = new LinearLayoutManager(context);


        // use a linear layout manager
       // mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(context,LinearLayoutManager.VERTICAL,false));

        // mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        // create an Object for Adapter
        mAdapter = new SongsPaginationDataAdapter(beerList, mRecyclerView,context);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);;



        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebServiceData();
            }
        });


        getWebServiceData();

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    beerList.add(null);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemInserted(beerList.size() - 1);

                        }
                    });


                    Log.i("load more fired = ", pageNumber + "");
                  ++pageNumber;
                  if(isSearchQuery)
                  {
                      getSearchedBeerFromSql(searchQuery);
                  }
                  else
                  {
                      if(checkInternetConnection.isNetworkConnected(context))
                      {
                          getAllBeerFromListByPage();
                      }
                      else
                      {
                          getAllBeerFromSql();
                      }

                  }




            }
        });

        return rootView;
    }
    public void initializeSpinners() {

        // Spinner Drop down elements
        List<String> list = new ArrayList<String>();
        list.add("- SORT -");
        list.add("Alcohol Content - low to high");
        list.add("Alcohol Content - high to low");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(dataAdapter);
        sortSpinner.setPrompt("SORT BY");



        List<String> temp = helper.getFilterOptions(allBeerList);


        // Spinner Drop down elements
        List<String> list1 = new ArrayList<String>();
        list1.add("- FILTER -");
        list1.add("FAVOURITE");
        list1.addAll(temp);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(dataAdapter1);
        filterSpinner.setPrompt("FILTERS");
        

        setListenerToSpinners();
    }

    private void setListenerToSpinners() {
        // Spinner click listener
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
// On selecting a spinner item
                String item = adapterView.getItemAtPosition(i).toString();

                // Showing selected spinner item

               allBeerList= helper.applySort(allBeerList,i);
               clearPreviousResults();
               getAllBeerFromListByPage();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Showing selected spinner item
                Toast.makeText(context, "no Selected: " , Toast.LENGTH_LONG).show();

            }
        });
        // Spinner click listener
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
// On selecting a spinner item
                String item = adapterView.getItemAtPosition(i).toString();

                // Showing selected spinner item

                if(i==1) //show favourites
                {
                    allBeerList= helper.showFav(context);
                    clearPreviousResults();
                    getAllBeerFromListByPage();
                }
                else   if(i!=0)//filter by style
                {
                    allBeerList= helper.applyFilter(orignalList,item);
                    clearPreviousResults();
                    getAllBeerFromListByPage();

                }

                else
                {
                    allBeerList= orignalList;
                    clearPreviousResults();
                    getAllBeerFromListByPage();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Showing selected spinner item
                Toast.makeText(context, " no mSelected: " , Toast.LENGTH_LONG).show();

            }
        });

        //initializeSpinners();
    }

    public void getWebServiceData() {
        clearPreviousResults();
        if(checkInternetConnection.isNetworkConnected(context))
        {
            // To ensure list is visible when retry button in error view is clicked
            hideErrorView();
            BackGroundTaskLoadJson backGroundTask = new BackGroundTaskLoadJson(context, this,this);
            backGroundTask.loadDataIntoList();

        }
        else
        {

            // Call setRefreshing(false) when the list has been refreshed.
            pullToRefresh.setRefreshing(false);
            if(databaseControllerAllQuery.numberOfTuple(TABLENAME)>0) {

                hideErrorView();
//after hide error view set visibility gone
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Showing Offline data", Toast.LENGTH_SHORT).show();

                getAllBeerFromSql();
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                showErrorView(getResources().getString(R.string.error_msg_no_internet));
            }

        }


    }

    private void getAllBeerFromSql() {
                List<Beer>  response= databaseControllerAllQuery.getBeerOfPage(TABLENAME, pageNumber);
        int size=databaseControllerAllQuery.numberOfTuple(TABLENAME);
        loadPage(response,size);
    }


    @Override
    public void onTaskCompleted(List<Beer>  response) {

        allBeerList=response;
        orignalList=allBeerList;

        // Call setRefreshing(false) when the list has been refreshed.
        pullToRefresh.setRefreshing(false);
        Log.i("AllSongsFragment = ", "onTaskCompleted");

hideErrorView();
//after hide error view set visibility gone
        progressBar.setVisibility(View.GONE);

        //First insert list then do anything
      //  insertToSql(response);  insert by reading ITEM_PER_PAGE

        initializeSpinners();



        getAllBeerFromListByPage();

    }
    public void insertToSql(List<Beer>  response) {
        for(Beer olaData: response)
        {
            databaseControllerAllQuery.insertBeer(olaData,TABLENAME);

        }


    }
    public void getAllBeerFromListByPage() {




        int start = (pageNumber-1)*ITEM_PER_PAGE;
        int itemToDisplay =ITEM_PER_PAGE;
        int size=allBeerList.size();
        int totalTuple=size;
        if((start+itemToDisplay)>totalTuple)
        {
            itemToDisplay=totalTuple-start;
        }
        Log.i("search",start+"  "+itemToDisplay+"  "+allBeerList.size());
        List<Beer> response = new ArrayList<Beer>();;
        if(itemToDisplay>0) {
            //Sublist to List
            response = allBeerList.subList(start, start + itemToDisplay);//sublist to display and store


            insertToSql(response);// inserting ITEM_PER_PAGE into local database
        }

        loadPage(response,size);
    }
    public void getSearchedBeerFromSql(String searchQuery) {


        if(allBeerList.size()>0) // i.e. we have loaded list from internet
        {
            SearchInList searchInList =new SearchInList();
            List<Beer>  response= searchInList.searchResult(searchQuery,allBeerList);

            loadPage(response,response.size());
        }
        else
        {
            List<Beer>  response= databaseControllerAllQuery.getSearchedBeerByPage(TABLENAME, pageNumber,searchQuery);
            int size=databaseControllerAllQuery.numberOfTupleInSearchResult(TABLENAME,searchQuery);

            loadPage(response,size);

        }

    }

        public void loadPage(List<Beer>  response,int size) {

            if (pageNumber > 1) {

                beerList.remove(beerList.size() - 1);


            handler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyItemRemoved(beerList.size());
                }
            });

        }



//        // adding HashList to ArrayList
       // songsList.addAll(response);

if(response.size()==0||beerList.size()>=size)
{

    mAdapter.setEndReachedTrue();
}
else {
    for (Beer olaData : response)
    //  for(OlaData olaData:response)
    {
        beerList.add(olaData);
       // Log.i("adding = ", olaData.getName()+"");
        handler.post(new Runnable() {
            @Override
            public void run() {

                mAdapter.notifyItemInserted(beerList.size());


            }
        });
    }


    mAdapter.setLoaded();

}//else
            //again to not call load more
            if(beerList.size()>=size)
            {
                mAdapter.setEndReachedTrue();
            }

    }

    @Override
    public void retryPageLoad() {
       getWebServiceData();

    }



    public void showErrorView(String throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText((throwable));
        }
    }



    // Helpers -------------------------------------------------------------------------------------


    public void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
//Search part

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.search_menu, menu);
       super.onCreateOptionsMenu(menu,inflater);
       MenuItem search = menu.findItem(R.id.search);
       MenuItem showCart = menu.findItem(R.id.showCart);
       showCart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {
               Intent intent= new Intent(context,MyCart.class);
               startActivity(intent);
               return false;
           }
       });

       SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
       search(searchView);
   }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(context,"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void clearPreviousResults() {

        beerList.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.resetEndReached();
        pageNumber=1;
    }
    private void search(final SearchView searchView) {

        searchView.setQueryHint("Search Any Beer");
        EditText searchText=((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));

        searchText.setHintTextColor(getResources().getColor(R.color.black_overlay));
        Typeface kushagra = Typeface.createFromAsset(context.getAssets(), "fonts/angelina.TTF");

        searchText.setTypeface(kushagra);
        searchText.setTextSize(28);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clearPreviousResults();
                isSearchQuery=true;
                searchQuery=query;
                getSearchedBeerFromSql(query);

                //getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                clearPreviousResults();

                if(newText!=null&&newText.trim().length()==0)
                {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);;


                    isSearchQuery=false;
                    if(checkInternetConnection.isNetworkConnected(context))
                    {
                        getAllBeerFromListByPage();
                    }
                    else
                    {
                        getAllBeerFromSql();
                    }

                }
                else {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);;

                    isSearchQuery=true;
                    searchQuery=newText;
                    getSearchedBeerFromSql(newText);

                }

                return true;
            }
        });
    }


}
