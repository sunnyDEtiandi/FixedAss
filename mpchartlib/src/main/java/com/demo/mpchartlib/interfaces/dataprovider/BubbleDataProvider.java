package com.demo.mpchartlib.interfaces.dataprovider;

import com.demo.mpchartlib.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
