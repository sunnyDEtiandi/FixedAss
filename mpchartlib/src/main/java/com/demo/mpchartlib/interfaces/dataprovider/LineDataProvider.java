package com.demo.mpchartlib.interfaces.dataprovider;

import com.demo.mpchartlib.components.YAxis;
import com.demo.mpchartlib.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
