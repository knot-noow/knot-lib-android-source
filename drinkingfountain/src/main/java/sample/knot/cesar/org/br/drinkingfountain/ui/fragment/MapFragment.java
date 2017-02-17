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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import java.util.ArrayList;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterBottle;
import sample.knot.cesar.org.br.drinkingfountain.view.KnotMap;

public class MapFragment extends Fragment {

    private Button mBtnChangeFloor;
    private KnotMap mKnotMapFirstFloor, mKnotMapSecondFloor;

    /**
     * Return a new instance of fragment
     *
     * @return MapFragment
     */
    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map, container, false);

        initFragment(view);

        return view;
    }

    private void initFragment(@NonNull View view) {
        mBtnChangeFloor = (Button) view.findViewById(R.id.btn_change_floor);

        mBtnChangeFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                changeFloor();
            }
        });

        tempMockItems(view);
    }

    /**
     * This method is used to change the map
     */
    private void changeFloor() {

        final int animationDuration = 1000;
        final int scaleIn = 0;
        final int scaleOut = 1;

        final String firstFloor = "1";
        final String groundFloor = "T";

        mBtnChangeFloor.animate().setDuration(animationDuration).scaleY(scaleIn).scaleX(scaleIn).setInterpolator(new BounceInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (mKnotMapFirstFloor.getVisibility() == View.VISIBLE) {
                    mKnotMapFirstFloor.setVisibility(View.GONE);
                    mKnotMapSecondFloor.setVisibility(View.VISIBLE);
                    mBtnChangeFloor.setText(firstFloor);
                } else {
                    mKnotMapSecondFloor.setVisibility(View.GONE);
                    mKnotMapFirstFloor.setVisibility(View.VISIBLE);
                    mBtnChangeFloor.setText(groundFloor);
                }

                mBtnChangeFloor.animate().setDuration(animationDuration).scaleX(scaleOut).scaleY(scaleOut).setInterpolator(new BounceInterpolator());
            }
        }).start();


    }

    private void tempMockItems(@NonNull View view) {

        final ArrayList<WaterBottle> listFirst = new ArrayList<>();
        WaterBottle w1 = new WaterBottle(20);
        w1.setMapPositionX(1580);
        w1.setMapPositionY(477);


        WaterBottle w2 = new WaterBottle(5);
        w2.setMapPositionX(1210);
        w2.setMapPositionY(244);

        WaterBottle w3 = new WaterBottle(50);
        w3.setMapPositionX(2403);
        w3.setMapPositionY(177);

        listFirst.add(w1);
        listFirst.add(w2);
        listFirst.add(w3);


        final ArrayList<WaterBottle> second = new ArrayList<>();
        WaterBottle w4 = new WaterBottle(100);
        w4.setMapPositionX(1000);
        w4.setMapPositionY(200);


        WaterBottle w5 = new WaterBottle(5);
        w5.setMapPositionX(861);
        w5.setMapPositionY(244);

        WaterBottle w6 = new WaterBottle(50);
        w6.setMapPositionX(1400);
        w6.setMapPositionY(60);

        second.add(w4);
        second.add(w5);
        second.add(w6);

        mKnotMapFirstFloor = (KnotMap) view.findViewById(R.id.map_first_floor);
        mKnotMapSecondFloor = (KnotMap) view.findViewById(R.id.map_second_floor);

        mKnotMapSecondFloor.fillMapWithWaterBottle(R.drawable.tir_second_floor, second, getActivity());
        mKnotMapFirstFloor.fillMapWithWaterBottle(R.drawable.tir_first_floor, listFirst, getActivity());
    }
}
