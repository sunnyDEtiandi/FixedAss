package fixedass.xiangrong.com.fixedass.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.mpchartlib.animation.Easing;
import com.demo.mpchartlib.charts.PieChart;
import com.demo.mpchartlib.components.Description;
import com.demo.mpchartlib.components.Legend;
import com.demo.mpchartlib.data.Entry;
import com.demo.mpchartlib.data.PieData;
import com.demo.mpchartlib.data.PieDataSet;
import com.demo.mpchartlib.data.PieEntry;
import com.demo.mpchartlib.formatter.PercentFormatter;
import com.demo.mpchartlib.formatter.ValueFormatter;
import com.demo.mpchartlib.utils.ColorTemplate;
import com.demo.mpchartlib.utils.MPPointF;
import com.demo.mpchartlib.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.CountState;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;

/**
 * Created by Administrator on 2018/4/24.
 */

public class HomeFragment extends Fragment {
    private View view;
    private Activity activity;
    private PieChart chart;
    private List<CountState> stateList;
    private String ipStr;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (activity == null){
            activity = getActivity();
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        view = inflater.inflate(R.layout.activity_first, container, false);
        chart = (PieChart)view.findViewById(R.id.chart);

        initData();
        return view;
    }

    private void initData(){
        if (!Redirect.checkNetwork(activity)) {
            Toast toast = Toast.makeText(activity,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 创建子线程，分别进行Get和Post传输
            new Thread(new CountStateThread()).start();
        }
    }

    class CountStateThread implements Runnable{
        @Override
        public void run() {
            String path = "http://" + ipStr + "/FixedAssService/sto/countState";

            final String info = WebService.executeHttpGet(path);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    stateList = StrConvertObject.strConvertCountState(info);
                    initPieChat();
                }
            });
        }
    }

    private void initPieChat() {
        int sum = 0;
        for (CountState state:stateList){
            sum += state.getNum();
        }

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextSize(12f);//设置中间文字中大小
        chart.setCenterText(generateCenterText(sum));

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(52f);//设置内圆环半径
        chart.setTransparentCircleRadius(60f);

        chart.setDrawCenterText(true);

        Description des = new Description();
        des.setText("资产状态饼状图");
        chart.setDescription(des);

        //chart.animateXY(1000, 1000);//设置动画效果
        //chart.setDrawSliceText(false);//圆环上不绘制图例文字

        chart.animateY(1400, Easing.EaseInOutQuad);


        if (sum == 0) {
            chart.setData(generateEmptyPieData());
            chart.setHighlightPerTapEnabled(false);//点击不响应
            //legend.setEnabled(false);//图例隐藏
            return;
        }

        chart.setData(generatePieData(sum));

        Legend legend = chart.getLegend();//获取图例
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setXOffset(230f);
        legend.setYOffset(-20f);
//        legend.setEnabled(true);//图例显示

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);//点击响应

        chart.highlightValues(null);

        /*chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {//圆盘点击事件

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(activity, "" + e.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });*/

    }

    /**
     * 中间文字绘制
     *
     * @param sum 总数
     * @return
     */
    private SpannableString generateCenterText(int sum) {
        String total = Integer.toString(sum);
        SpannableString s = new SpannableString(total + "\n 资产总数（个）");
        s.setSpan(new RelativeSizeSpan(5f), 0, total.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(88, 146, 240)), 0, total.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(153, 153, 153)), total.length(), s.length(), 0);
        /*s.setSpan(new RelativeSizeSpan(1.7f), 0, total.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);*/
        return s;
    }

    /**
     * 图表数据设置
     */
    protected PieData generatePieData(int sum) {
        ArrayList<PieEntry> yVals = new ArrayList<>();

        for (int i=0; i<stateList.size(); i++){
            String result = "";
            double v = stateList.get(i).getNum() * 1.0 / sum * 100 ;
            float value = (float)v;
            /*DecimalFormat df = new DecimalFormat("0%");
            result = df.format(v);*/
            yVals.add(new PieEntry(value, stateList.get(i).getState()+" "+stateList.get(i).getNum()));
        }

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setDrawIcons(false);

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(5f);


        ArrayList<Integer> colors = new ArrayList<>();
        /*colors.add(Color.rgb(23, 213, 159));
        colors.add(Color.rgb(245, 166, 35));
        colors.add(Color.rgb(184, 233, 134));*/
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        pieDataSet.setColors(colors);//颜色设置

        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        return data;
    }

    /**
     * 空图表数据设置
     *
     * @return
     */
    protected PieData generateEmptyPieData() {
        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        xVals.add("无资产");
        yVals.add(new PieEntry((int) 1, 1));

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setValueFormatter(new ValueFormatter() {//圆环内文字设置为空
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        });

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(153, 153, 153));
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);

        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }
}
