package com.demo.mpchartlib.interfaces.dataprovider;

import com.demo.mpchartlib.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
