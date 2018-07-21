package com.kushagra.beerorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.kushagra.beerorder.adapters.ViewPagerAdapter;
import com.kushagra.beerorder.fragments.AllBeerFragment;



import static com.kushagra.beerorder.Utils.Constants.ALL_BEER;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AllBeerFragment allBeerFragment;

    private int[] tabIcons = {
            R.drawable.beer,R.drawable.ic_favorite,R.drawable.recent_songs};


    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface kushagra = Typeface.createFromAsset(getAssets(), "fonts/angelina.TTF");

        mTitle.setTypeface(kushagra);
        mTitle.setText(ALL_BEER);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(ALL_BEER);


        viewPager = (ViewPager) findViewById(R.id.viewpager);



        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        allBeerFragment=new AllBeerFragment();


        setupViewPager(viewPager);


        //Setup Tabs
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    switch (position) {
                        case 0:
                            actionBar.setTitle(ALL_BEER);
                            mTitle.setText(ALL_BEER);
                          //  allSongsFragment.getWebServiceData();

                            break;

                    }
                }
            }
        });
        setupTabIcons();


    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(allBeerFragment, "ONE");

        viewPager.setAdapter(adapter);
    }



    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }




}
