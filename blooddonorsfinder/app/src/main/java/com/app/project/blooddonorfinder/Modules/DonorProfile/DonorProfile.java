package com.app.project.blooddonorfinder.Modules.DonorProfile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.project.blooddonorfinder.Adapters.DonorDetailsAdapter;
import com.app.project.blooddonorfinder.Modules.Login.LoginActivity;
import com.app.project.blooddonorfinder.PushNotification.GCMHelper;
import com.app.project.blooddonorfinder.R;
import com.app.project.blooddonorfinder.Utils.DBBackup;
import com.app.project.blooddonorfinder.views.SlidingTabLayout;

import java.io.IOException;
import java.util.Stack;

public class DonorProfile extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    ViewPager mViewPager;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    SlidingTabLayout tabs;
    CharSequence titles[] = {"Profile", "Incoming Request", "Served Request"};
    int action = -1;
    private int pageSelected = 0;
    Stack<Integer> pageHistory;

    boolean saveToHistory;
    private int currentPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DonorDetailsAdapter donorDetailsAdapter = new DonorDetailsAdapter(getSupportFragmentManager(), titles, titles.length);
        mViewPager.setAdapter(donorDetailsAdapter);
        mViewPager.setOffscreenPageLimit(3);

        // load saved navigation state if present
        if (null == savedInstanceState) {
            mNavItemId = R.id.drawer_item_1;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        // listen for navigation events
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // select the correct nav menu item
        navigationView.getMenu().findItem(mNavItemId).setChecked(true);

        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
                R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);

        tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }


        });


        tabs.setIsSelectable(true);
        pageHistory=new Stack<>();


        tabs.setViewPager(mViewPager);
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pageSelected = position;
                mViewPager.setCurrentItem(pageSelected);
                if(saveToHistory)
                {
                    pageHistory.push(Integer.valueOf(currentPage));
                    currentPage=pageSelected;
                }
            }



        };
        saveToHistory = true;
        mViewPager.setCurrentItem(pageSelected);
        tabs.setOnPageChangeListener(pageChangeListener);

        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GCMHelper(DonorProfile.this).checkAndRegister();

        Bundle inputBundle = getIntent().getExtras();
        if (inputBundle != null) {
            if (inputBundle.get("action") != null) {
                action = inputBundle.getInt("action");
            }
        }

        if (action != -1)
            mViewPager.setCurrentItem(0);

    }


    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        if (mViewPager != null)
            switch (itemId) {
                case R.id.drawer_item_1:
                    mViewPager.setCurrentItem(0);
                    mDrawerToggle.syncState();
                    break;
                case R.id.drawer_item_2:
                    mViewPager.setCurrentItem(1);
                    mDrawerToggle.syncState();
                    break;
                case R.id.drawer_item_3:
                    mViewPager.setCurrentItem(2);
                    mDrawerToggle.syncState();
                    break;
            }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.action_logout:
                SharedPreferences appPreferences = getSharedPreferences(getString(R.string.app_preference), MODE_PRIVATE);
                appPreferences.edit().putBoolean(getString(R.string.isLoggedIn), false).clear().commit();
                Intent loginPage = new Intent(DonorProfile.this, LoginActivity.class);
                startActivity(loginPage);
                finish();
                break;

        }
        if (item.getItemId() == android.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if(pageHistory.empty())
            super.onBackPressed();
        else {
            saveToHistory = false;
            mViewPager.setCurrentItem(pageHistory.pop().intValue());
            saveToHistory = true;
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donor_profile, menu);
        return true;
    }
}
