/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
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

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import sample.knot.cesar.org.br.drinkingfountain.R;

public class GraphicFragment extends Fragment{

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

        View view = inflater.inflate(R.layout.graphic_fragment, null);

        initView(view);
        InitUUID(savedInstanceState);

        return view;
    }

    private void InitUUID(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.uuid = savedInstanceState.getString(KEY_UUID);
        } else{
            this.uuid = getArguments().getString(KEY_UUID);
        }
    }

    private void initView(@NonNull View view) {
        chart = (ColumnChartView) view.findViewById(R.id.chart);
        previewChart = (PreviewColumnChartView) view.findViewById(R.id.chart_preview);

        generateDefaultData();

        chart.setColumnChartData(data);

        // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
        // zoom/scroll is unnecessary.
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);

        previewX(false);
    }

    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = 50;
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            columns.add(new Column(values));
        }

        data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis());
        data.setAxisYLeft(new Axis().setHasLines(true));

        // prepare preview data, is better to use separate deep copy for preview chart.
        // set color to grey to make preview area more visible.
        previewData = new ColumnChartData(data);
        for (Column column : previewData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
            }
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
}
