package com.demo.mpchartlib.interfaces.dataprovider;

import com.demo.mpchartlib.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
