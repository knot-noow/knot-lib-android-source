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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.ui.fragment.GraphicFragment;

public class GraphicActivity extends AppCompatActivity {

    /**
     * UUID used to create the graphic
     */
    public static final String KEY_UUID = "key_uuid";
    private GraphicFragment graphicFragment;
    private Toolbar toolbar;
    private String uuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphic_activity);

        this.uuid = getIntent().getStringExtra(KEY_UUID);

        initFragment(savedInstanceState);
    }

    private void initFragment(@Nullable Bundle savedInstanceState) {
        // Init the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // configure the toolbar
        setSupportActionBar(toolbar);
        // disable the title of  Activity
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.profile_activity_label);


        this.graphicFragment = GraphicFragment.newInstance(this.uuid);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.content, graphicFragment, graphicFragment.getClass().toString());
        } else {
            fragmentTransaction.replace(R.id.content, graphicFragment, graphicFragment.getClass().toString());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
