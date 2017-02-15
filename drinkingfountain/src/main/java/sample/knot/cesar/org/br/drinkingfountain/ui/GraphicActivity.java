/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.ui.fragment.GraphicFragment;

public class GraphicActivity extends AppCompatActivity {

    private GraphicFragment graphicFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphic_activity);

       // initFragment(savedInstanceState);
    }

    private void initFragment(@Nullable Bundle savedInstanceState) {
        this.graphicFragment = GraphicFragment.newInstance("uuid");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment buffer = fragmentManager.findFragmentByTag(GraphicFragment.class.getName());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.content, graphicFragment, graphicFragment.getClass().toString());
        } else {
            fragmentTransaction.replace(R.id.content, graphicFragment, graphicFragment.getClass().toString());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
