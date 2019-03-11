package fixedass.xiangrong.com.fixedass.manage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetFixQuery;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * @author Eileen
 * @create 2018/9/26
 * @Describe 页面转移单的适配器
 */
public class FixStyleAdapter extends BaseAdapter {
    private ArrayList<AssetFixQuery> fixQueries;
    private Context context;
    private int fragid;
    private User user;

    //记录checkbox的状态
    HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public FixStyleAdapter(ArrayList<AssetFixQuery> fixQueries, Context context, int fragid, User user) {
        this.fixQueries = fixQueries;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
    }

    @Override
    public int getCount() {
        return fixQueries.size();
    }

    @Override
    public Object getItem(int position) {
        return fixQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.layout_fixquery, null);

        if(convertView!=null) {
            /*Button seeDetails = (Button) convertView.findViewById(R.id.seeDetails);*/
            ImageView seeDetails = (ImageView) convertView.findViewById(R.id.seeDetails);

            EditText fixCode = (EditText)convertView.findViewById(R.id.fixCode);          //维修单号
            fixCode.setText(fixQueries.get(position).getOperbillCode());

            EditText fixState = (EditText) convertView.findViewById(R.id.fixState);       //维修状态
            int state = fixQueries.get(position).getAssetOperate().getState();
            String str="";
            List<Integer> stateList = new ArrayList<>();
            for (int i=-1;i<12;i++){
                if (i==6){
                    continue;
                }
                stateList.add(i);
            }
            String [] stateValues = new String[]{"已删除","未报审","审核中","未通过","审核通过","未维修","部分维修","已完成","导入待报审","导入待审核","导入未通过","导入新增"};
            for (int i=0;i<stateList.size();i++){
                if (stateList.get(i)==state){
                    str = stateValues[i];
                    break;
                }
            }
            fixState.setText(str);

            EditText creator = (EditText)convertView.findViewById(R.id.creator);          //创建人
            creator.setText(fixQueries.get(position).getAssetOperate().getUser().getUserName());

            EditText createDate= (EditText)convertView.findViewById(R.id.createDate);     //创建日期
            createDate.setText(fixQueries.get(position).getAssetOperate().getCreatedate());

            EditText operNote = (EditText)convertView.findViewById(R.id.operNote);    //备注
            operNote.setText(fixQueries.get(position).getAssetOperate().getOperNote());

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
                    AssetFixQuery fixQuery = fixQueries.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("fragid",fragid);
                    bundle.putSerializable("user",user);
                    bundle.putSerializable("fixQuery", fixQuery);

                    Redirect.redirect(context, FixListActivity.class, bundle);
                }
            });
        }
        return convertView;
    }
}
