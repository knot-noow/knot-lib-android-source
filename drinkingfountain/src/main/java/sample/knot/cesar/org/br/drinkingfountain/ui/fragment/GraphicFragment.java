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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
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
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.database.FacadeDatabase;
import sample.knot.cesar.org.br.drinkingfountain.model.WaterLevelData;

public class GraphicFragment extends Fragment {

    private static final String KEY_UUID = "key_uuid";

    // members
    private ColumnChartData data;
    private ColumnChartView chart;
    private PreviewColumnChartView previewChart;
    private ColumnChartData previewData;

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
        chart = (ColumnChartView) view.findViewById(R.id.chart);
        previewChart = (PreviewColumnChartView) view.findViewById(R.id.chart_preview);

        // Generate data for previewed chart and copy of that data for preview chart.
        generateDefaultData();

        chart.setColumnChartData(data);
        // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
        // zoom/scroll is unnecessary.
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);
        chart.setOnValueTouchListener(new ValueTouchListener());

        previewChart.setColumnChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());

        previewX(false);
    }

    private void generateDefaultData() {
        if (!TextUtils.isEmpty(this.uuid)) {
            final List<WaterLevelData> deviceHistory = FacadeDatabase.getInstance().getDeviceHistory(this.uuid);
            final int numSubcolumns = 1;
            final int numColumns = deviceHistory.size();
            final List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            float currentWaterLevel = 0.0f;
            WaterLevelData buffer;

            // add the columns
            for (int count = 0; count < numColumns; ++count) {
                values = new ArrayList<SubcolumnValue>();
                buffer = deviceHistory.get(count);
                // add the subcolumns
                for (int countSubColumns = 0; countSubColumns < numSubcolumns; ++countSubColumns) {
                    final SubcolumnValue subcolumnValue = new SubcolumnValue(buffer.getCurrentValue(), ChartUtils.pickColor());
                    values.add(subcolumnValue);
                }
                columns.add(new Column(values));
            }
            data = new ColumnChartData(columns);

            // generate the axisX
            List<AxisValue> axisValues = configureAxisX(deviceHistory);

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
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);

            // create a deep copy of data on preview chart
            previewData = new ColumnChartData(data);
            for (Column column : previewData.getColumns()) {
                for (SubcolumnValue value : column.getValues()) {
                    value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
                }
            }
        }
    }

    private List<AxisValue> configureAxisX(@NonNull List<WaterLevelData> list) {
        // sort the given list of WaterLevelData
        Collections.sort(list, new OrderWaterLevelData());
        final List<AxisValue> axisValues = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();
        float lastCurrentTime = 0.0f;
        int currentTimeSelected;
        int countAxis = 1;
        AxisValue axisValue;
        String timestampStr;
        String dayStr;

        for (int count = 0; count < list.size(); count++) {
            timestampStr = list.get(count).getTimestamp();
            if (!TextUtils.isEmpty(timestampStr) && !timestampStr.equalsIgnoreCase("0")) {
                calendar.setTimeInMillis(Long.parseLong(timestampStr));
                currentTimeSelected = calendar.get(Calendar.DAY_OF_MONTH);
                if (lastCurrentTime == 0 || lastCurrentTime != currentTimeSelected) {
                    // configure the axis
                    axisValue = new AxisValue(count);
                    dayStr = String.valueOf(currentTimeSelected);
                    axisValue.setLabel(dayStr);

                    // add the axis into the list
                    axisValues.add(axisValue);

                    lastCurrentTime = currentTimeSelected;
                    countAxis++;
                }
            }
        }
        return axisValues;
    }


    private static class OrderWaterLevelData implements Comparator<WaterLevelData> {

        @Override
        public int compare(WaterLevelData waterLevelData, WaterLevelData t1) {
            float timeStampOrig;
            float timeStampRef;
            if (waterLevelData != null && t1 != null
                    && !TextUtils.isEmpty(waterLevelData.getTimestamp()) && !TextUtils.isEmpty(t1.getTimestamp())) {

                // convert the times
                timeStampOrig = Float.parseFloat(waterLevelData.getTimestamp());
                timeStampRef = Float.parseFloat(t1.getTimestamp());

                // check the datas
                if (timeStampOrig > timeStampRef) {
                    return 1;
                }
            }
            return 0;
        }
    }

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    /**
     * Viewport listener for preview chart(lower one). in {@link #onViewportChanged(Viewport)} method change
     * viewport of upper chart.
     */
    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            chart.setCurrentViewport(newViewport);
        }
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Snackbar.make(getView(), getString(R.string.graphic_use_of_day) + (int) value.getValue() + " ml", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
