package sample.knot.cesar.org.br.drinkingfountain;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import sample.knot.cesar.org.br.drinkingfountain.model.WaterBottle;
import sample.knot.cesar.org.br.drinkingfountain.view.KnotMap;

public class MapActivity extends AppCompatActivity {


    Button mBtnChangeFloor;
    KnotMap mKnotMapFirstFloor, mKnotMapSecondFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mBtnChangeFloor = (Button)findViewById(R.id.btn_change_floor);

        mBtnChangeFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKnotMapFirstFloor.getVisibility() == View.VISIBLE){
                    mKnotMapFirstFloor.setVisibility(View.GONE);
                    mKnotMapSecondFloor.setVisibility(View.VISIBLE);
                }else{
                    mKnotMapSecondFloor.setVisibility(View.GONE);
                    mKnotMapFirstFloor.setVisibility(View.VISIBLE);
                }
            }
        });

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

        mKnotMapFirstFloor = (KnotMap)findViewById(R.id.map_first_floor);
        mKnotMapSecondFloor = (KnotMap)findViewById(R.id.map_second_floor);

        mKnotMapSecondFloor.fillMapWithWaterBottle(R.drawable.tir_second_floor,second,MapActivity.this);
        mKnotMapFirstFloor.fillMapWithWaterBottle(R.drawable.tir_first_floor,listFirst,MapActivity.this);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);


    }
}