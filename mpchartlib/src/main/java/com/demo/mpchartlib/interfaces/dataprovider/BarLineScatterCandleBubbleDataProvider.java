package com.demo.mpchartlib.interfaces.dataprovider;


import com.demo.mpchartlib.components.YAxis;
import com.demo.mpchartlib.data.BarLineScatterCandleBubbleData;
import com.demo.mpchartlib.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
