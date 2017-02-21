package sample.knot.cesar.org.br.drinkingfountain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.view.WaterBottleView;

/**
 * Created by usuario on 20/01/17.
 */

public class DrinkFountainGridAdapter extends RecyclerView.Adapter<DrinkFountainGridAdapter.WaterViewHolder> {

    private Context mContext;
    private List<DrinkFountainDevice> mListOfWaterBottle;
    private FacadeDatabase mDataBase;

    public DrinkFountainGridAdapter(Context context, List<DrinkFountainDevice> list) {
        mContext = context;
        mListOfWaterBottle = list;
        mDataBase = FacadeDatabase.getInstance();

    }

    @Override
    public WaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.water_card_view, parent, false);

        return new WaterViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(WaterViewHolder holder, int position) {
        DrinkFountainDevice drinkFountainDevice = mListOfWaterBottle.get(position);

        WaterLevelData waterLevelData = mDataBase.getCurrentLevelByDeviceUUID(drinkFountainDevice.getUuid());

        holder.mWaterBottleView.setWaterHeight(waterLevelData.getCurrentValue());
        holder.mTitle.setText(drinkFountainDevice.getDescription());

        String floor="";
        if(drinkFountainDevice.getFloor() == DrinkFountainDevice.GROUND_FLOOR){
            floor = mContext.getResources().getString(R.string.ground_floor);
        }else{
            floor = mContext.getResources().getString(R.string.first_floor);
        }

        holder.mDesciption.setText(drinkFountainDevice.getDescription() + ": " +floor);
        holder.mWaterBottleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(((WaterGridActivity)mContext), Pair.create(view, "teste")).toBundle();

//                mContext.startActivity(new Intent(((WaterGridActivity)mContext), MainActivity.class), bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListOfWaterBottle.size();
    }

    public class WaterViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mDesciption;
        private WaterBottleView mWaterBottleView;

        public WaterViewHolder(View view) {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.water_card_title);
            mDesciption = (TextView) view.findViewById(R.id.water_card_description);

            mWaterBottleView = (WaterBottleView) view.findViewById(R.id.water_bottle);
        }

    }
}
