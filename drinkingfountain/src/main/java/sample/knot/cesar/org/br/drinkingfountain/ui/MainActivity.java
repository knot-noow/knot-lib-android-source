/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.ui.fragment.DrinkingFountainFragment;
import sample.knot.cesar.org.br.drinkingfountain.ui.fragment.MapFragment;
import sample.knot.cesar.org.br.drinkingfountain.util.Stub;
import sample.knot.cesar.org.br.drinkingfountain.view.CustomViewPager;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;

    private static final int INDEX_DRINKING_FOUNTAINT = 0;
    private static final int INDEX_MAP_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO - use this block of code to test with data
        initView();
    }

    private void initView() {
        this.tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        this.viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // configure the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.main_activity_label);

        // init the PageView
        this.viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_about:
                handleAboutMenu();
                break;
            case R.id.menu_settings:
                handleSettingsMenu();
                break;
            default:
                // ignore the click
                break;
        }
        return true;
    }

    private void handleAboutMenu() {
        // TODO - finish this code later
    }

    private void handleSettingsMenu() {
        Intent it = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(it);
    }



    ///////////////////////////////////////////////////////////////////////////
    // Adapter
    ///////////////////////////////////////////////////////////////////////////
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private int[] mTitles = new int[]{
                R.string.title_drinking_fountain,
                R.string.title_map
        };

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // configure the fragments of ViewPager
            Fragment fragment = null;
            if (position == INDEX_DRINKING_FOUNTAINT) {
                fragment = DrinkingFountainFragment.newInstance();
            } else if (position == INDEX_MAP_FRAGMENT) {
                fragment = MapFragment.newInstance();
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(mTitles[position]);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }
}
