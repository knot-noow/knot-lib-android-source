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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.DrinkFountainDevice;
import sample.knot.cesar.org.br.drinkingfountain.model.GraphicDate;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;
import sample.knot.cesar.org.br.drinkingfountain.util.Util;
import sample.knot.cesar.org.br.drinkingfountain.view.WaterBottleView;

public class GraphicFragment extends Fragment {

    private static final String KEY_UUID = "key_uuid";
    private static final String SPACE = " ";
    private static final String LITERS = " L";
    private static final String SEPARATOR = ": ";

    private static final int LEVEL_FULL = 21;

    // members
    private ColumnChartData mColumnData;
    private ColumnChartView mChart;
    private ColumnChartData previewData;
    private WaterBottleView mWaterBottleView;
    private DrinkFountainDevice mDrinkFountainDevice;

    // uuid
    private String uuid;

    public static GraphicFragment newInstance(@NonNull String uuid) {
        GraphicFragment graphicFragment = new GraphicFragment();

        // init the required parameters
        Bundle bundle = new Bundle();
        bundle.putString(KEY_UUID, uuid);

        // set the required parameters into the current fragment
        graphicFragment.setArguments(bundle);

        return graphicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graphic_fragment, container, false);

        InitUUID(savedInstanceState);
        initView(view);

        return view;
    }

    private void InitUUID(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.uuid = savedInstanceState.getString(KEY_UUID);
        } else {
            this.uuid = getArguments().getString(KEY_UUID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_UUID, this.uuid);
        // save the stream
        super.onSaveInstanceState(outState);
    }

    private void initView(@NonNull View view) {
        mChart = (ColumnChartView) view.findViewById(R.id.chart);

        mWaterBottleView = (WaterBottleView) view.findViewById(R.id.water_bottle_graphic);
        mWaterBottleView.disableTextInformation();


        if (uuid != null) {
            mDrinkFountainDevice = FacadeDatabase.getInstance().getDrinkFountainByUUid(uuid);

            if (mDrinkFountainDevice != null) {

                String floor = "";
                if (mDrinkFountainDevice.getFloor() == DrinkFountainDevice.GROUND_FLOOR) {
                    floor = getString(R.string.ground_floor);
                } else {
                    floor = getString(R.string.first_floor);
                }

                if(mDrinkFountainDevice.getDescription()!=null){
                    ((TextView) view.findViewById(R.id.water_graphic_title)).setText(mDrinkFountainDevice.getDescription());
                    ((TextView) view.findViewById(R.id.water_graphic_description)).setText(mDrinkFountainDevice.getDescription() + SEPARATOR + floor);
                }


                WaterLevelData waterLevelData = FacadeDatabase.getInstance().getCurrentLevelByDeviceUUID(uuid);
                if (waterLevelData != null) {
                    mWaterBottleView.setWaterHeight(waterLevelData.getCurrentValue());

                    String waterReceived = getString(R.string.graphic_water_level) + SPACE + (int) waterLevelData.getCurrentValue() + LITERS;
                    ((TextView) view.findViewById(R.id.water_graphic_rest)).setText(waterReceived);
                }

            }

        }

        // Generate data for previewed mChart and copy of that data for preview mChart.
        generateDefaultData();

        mChart.setColumnChartData(mColumnData);
        // Disable zoom/scroll for previewed mChart, visible mChart ranges depends on preview mChart viewport so
        // zoom/scroll is unnecessary.
        mChart.setZoomEnabled(false);
        mChart.setScrollEnabled(false);
        mChart.setOnValueTouchListener(new ValueTouchListener());

    }

    private void generateDefaultData() {
        if (!TextUtils.isEmpty(this.uuid)) {
            final List<WaterLevelData> deviceHistory = FacadeDatabase.getInstance().getDeviceHistory(this.uuid);

            if(deviceHistory!=null && !deviceHistory.isEmpty()){
                mChart.setVisibility(View.VISIBLE);

                List<GraphicDate> listOfItems = graphAdjustments(deviceHistory);
                // Sort the list by timestampt
                Collections.sort(listOfItems, new OrderListByTimestamp());

                final int numSubcolumns = 1;
                final int numColumns = listOfItems.size();
                final List<Column> columns = new ArrayList<Column>();
                List<SubcolumnValue> values;
                GraphicDate buffer;

                // add the columns
                for (int count = 0; count < numColumns; ++count) {
                    values = new ArrayList<SubcolumnValue>();
                    buffer = listOfItems.get(count);
                    // add the subcolumns
                    for (int countSubColumns = 0; countSubColumns < numSubcolumns; ++countSubColumns) {
                        final SubcolumnValue subcolumnValue = new SubcolumnValue(buffer.getValue(), ChartUtils.pickColor());
                        values.add(subcolumnValue);
                    }
                    columns.add(new Column(values));
                }
                mColumnData = new ColumnChartData(columns);

                // generate the axisX
                List<AxisValue> axisValues = configureAxisX(listOfItems);

                // configure the axisX
                Axis axisX = new Axis();
                axisX.setAutoGenerated(false);
                axisX.setValues(axisValues);
                axisX.setName(getString(R.string.graphic_use));

                // configure the axisY
                Axis axisY = new Axis().setHasLines(true);
                axisY.setName(getString(R.string.graphic_liters));
                axisY.setHasLines(true);

                // insert the axis into the data
                mColumnData.setAxisXBottom(axisX);
                mColumnData.setAxisYLeft(axisY);

                // create a deep copy of data on preview mChart
                previewData = new ColumnChartData(mColumnData);
                for (Column column : previewData.getColumns()) {
                    for (SubcolumnValue value : column.getValues()) {
                        value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
                    }
                }

            }else{
                mChart.setVisibility(View.INVISIBLE);
            }

        }
    }

    private List<AxisValue> configureAxisX(@NonNull List<GraphicDate> list) {
        // sort the given list of WaterLevelData
        final List<AxisValue> axisValues = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();
        float lastCurrentTime = 0.0f;
        int currentDaySelected, currentMonthSelected;
        AxisValue axisValue;
        String timestampStr;
        String dayStr;

        for (int count = 0; count < list.size(); count++) {
            timestampStr = list.get(count).getDateDescription();
            if (!TextUtils.isEmpty(timestampStr) && !timestampStr.equalsIgnoreCase("0")) {
                calendar.setTimeInMillis(Long.parseLong(timestampStr));
                currentDaySelected = calendar.get(Calendar.DAY_OF_MONTH);
                currentMonthSelected = calendar.get(Calendar.MONTH) + 1;
                if (lastCurrentTime == 0 || lastCurrentTime != currentDaySelected) {
                    // configure the axis
                    axisValue = new AxisValue(count);
                    dayStr = String.valueOf(currentDaySelected);
                    dayStr = dayStr + "/" + currentMonthSelected;
                    axisValue.setLabel(dayStr);

                    // add the axis into the list
                    axisValues.add(axisValue);

                    lastCurrentTime = currentDaySelected;
                }
            }
        }
        return axisValues;
    }

    public List<GraphicDate> graphAdjustments(List<WaterLevelData> historicalList) {

        HashMap<Integer, GraphicDate> days = new HashMap<>();
        List<GraphicDate> graphicDates = new ArrayList<>();
        GraphicDate graphicDate;


        for (WaterLevelData waterLevelData : historicalList) {
            int key = Util.getDateByTime(waterLevelData.getTimestamp());
            int count = LEVEL_FULL - (int) waterLevelData.getCurrentValue();
            if (days.containsKey(key)) {
                graphicDate = days.get(key);
                count = graphicDate.getValue();
                days.remove(key);
                count++;
            } else {
                graphicDate = new GraphicDate(key, waterLevelData.getTimestamp(), count);
            }

            graphicDate.setValue(count);
            days.put(key, graphicDate);
        }

        Iterator it = days.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            graphicDates.add((GraphicDate) pair.getValue());
            it.remove();
        }

        return graphicDates;

    }

    private static class OrderListByTimestamp implements Comparator<GraphicDate> {

        @Override
        public int compare(GraphicDate GraphicDateFirst, GraphicDate GraphicDateSecond) {
            if (GraphicDateFirst != null && GraphicDateSecond != null) {
                return Integer.compare(GraphicDateFirst.getOrder(), GraphicDateSecond.getOrder());
            }
            return 0;
        }

    }

    /**
     * Viewport listener for preview mChart(lower one). in {@link #onViewportChanged(Viewport)} method change
     * viewport of upper mChart.
     */
    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            mChart.setCurrentViewport(newViewport);
        }
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            final String waterReceived = getString(R.string.graphic_use_of_day) + SPACE + (int) value.getValue() + LITERS;
            Snackbar.make(getView(), waterReceived, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }

    }
}
