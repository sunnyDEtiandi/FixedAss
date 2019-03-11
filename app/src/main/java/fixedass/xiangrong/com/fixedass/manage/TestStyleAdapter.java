package fixedass.xiangrong.com.fixedass.manage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetTestQuery;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * @author Eileen
 * @create 2018/9/26
 * @Describe 页面转移单的适配器
 */
public class TestStyleAdapter extends BaseAdapter {
    private ArrayList<AssetTestQuery> testQueries;
    private Context context;
    private int fragid;
    private User user;

    //记录checkbox的状态
    HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public TestStyleAdapter(ArrayList<AssetTestQuery> testQueries, Context context, int fragid, User user) {
        this.testQueries = testQueries;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
    }

    @Override
    public int getCount() {
        return testQueries.size();
    }

    @Override
    public Object getItem(int position) {
        return testQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.layout_testquery, null);

        if(convertView!=null) {
            /*Button seeDetails = (Button) convertView.findViewById(R.id.seeDetails);*/
            ImageView seeDetails = (ImageView) convertView.findViewById(R.id.seeDetails);

            EditText testCode = (EditText)convertView.findViewById(R.id.testCode);          //检验单号
            testCode.setText(testQueries.get(position).getOperbillCode());

            EditText testState = (EditText) convertView.findViewById(R.id.testState);       //检验状态
            int state = testQueries.get(position).getAssetOperate().getState();
            String str="";
            List<Integer> stateList = new ArrayList<>();
            for (int i=-1;i<12;i++){
                if (i==6){
                    continue;
                }
                stateList.add(i);
            }
            String [] stateValues = new String[]{"已删除","未报审","审核中","未通过","审核通过","未检验","部分检验","已完成","导入待报审","导入待审核","导入未通过","导入新增"};
            for (int i=0;i<stateList.size();i++){
                if (stateList.get(i)==state){
                    str = stateValues[i];
                    break;
                }
            }
            testState.setText(str);

            EditText creator = (EditText)convertView.findViewById(R.id.creator);          //创建人
            creator.setText(testQueries.get(position).getAssetOperate().getUser().getUserName());

            EditText createDate= (EditText)convertView.findViewById(R.id.createDate);     //创建日期
            createDate.setText(testQueries.get(position).getAssetOperate().getCreatedate());

            EditText operNote = (EditText)convertView.findViewById(R.id.operNote);    //备注
            operNote.setText(testQueries.get(position).getAssetOperate().getOperNote());

            CheckBox selCode = (CheckBox)convertView.findViewById(R.id.selCode);
            selCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selCheck.put(position, isChecked);
                    } else {
                        selCheck.remove(position);
                    }
                }
            });
            selCode.setChecked((selCheck.get(position) == null ? false : true));

            /*查看详情*/
            seeDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //--表格展示-跳转新的界面
                    AssetTestQuery testQuery = testQueries.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("fragid",fragid);
                    bundle.putSerializable("user",user);
                    bundle.putSerializable("testQuery", testQuery);

                    Redirect.redirect(context, TestListActivity.class, bundle);
                }
            });
        }
        return convertView;
    }
}
