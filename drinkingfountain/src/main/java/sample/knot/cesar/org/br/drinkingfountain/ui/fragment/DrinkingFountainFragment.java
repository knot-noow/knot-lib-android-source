/*
 * Copyright (c) 2017, CESAR.
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.adapter.DrinkFountainGridAdapter;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;

public class DrinkingFountainFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private static final int COLUMN = 2;

    /**
     * Return a new instance of fragment
     * @return DrinkingFountaintFragment
     */
    public static DrinkingFountainFragment newInstance() {
        return new DrinkingFountainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_grid, container, false);

        initFragment(view);

        return view;

    }


    private void initFragment(@NonNull View view){

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);


        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), COLUMN);
        mRecyclerView.setLayoutManager(mLayoutManager);
        
        List<DrinkFountainDevice> list = FacadeDatabase.getInstance().getAllDrinkFountain();

        DrinkFountainGridAdapter mAdapter = new DrinkFountainGridAdapter(getContext(), list);
        mRecyclerView.setAdapter(mAdapter);
    }
}
