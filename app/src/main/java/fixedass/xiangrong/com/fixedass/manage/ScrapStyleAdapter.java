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
import fixedass.xiangrong.com.fixedass.bean.AssetFixQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetScrapQuery;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * @author Eileen
 * @create 2018/9/26
 * @Describe 页面转移单的适配器
 */
public class ScrapStyleAdapter extends BaseAdapter {
    private ArrayList<AssetScrapQuery> scrapQueries;
    private Context context;
    private int fragid;
    private User user;

    //记录checkbox的状态
    HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public ScrapStyleAdapter(ArrayList<AssetScrapQuery> scrapQueries, Context context, int fragid, User user) {
        this.scrapQueries = scrapQueries;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
    }

    @Override
    public int getCount() {
        return scrapQueries.size();
    }

    @Override
    public Object getItem(int position) {
        return scrapQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.layout_scrapquery, null);

        if(convertView!=null) {
            /*Button seeDetails = (Button) convertView.findViewById(R.id.seeDetails);*/
            ImageView seeDetails = (ImageView) convertView.findViewById(R.id.seeDetails);

            EditText scrapCode = (EditText)convertView.findViewById(R.id.scrapCode);          //维修单号
            scrapCode.setText(scrapQueries.get(position).getOperbillCode());

            EditText scrapState = (EditText) convertView.findViewById(R.id.scrapState);       //维修状态
            String str="";
            List<Integer> stateList = new ArrayList<>();
            for (int i=-1;i<12;i++){
                if (i==6||i==4||i==5){
                    continue;
                }
                stateList.add(i);
            }
            String [] stateValues = new String[]{"已删除","未报审","审核中","未通过","审核通过","已完成","导入待报审","导入待审核","导入未通过","导入新增"};
            int state = scrapQueries.get(position).getAssetOperate().getState();
            for (int i=0;i<stateList.size();i++){
                if (stateList.get(i)==state){
                    str = stateValues[i];
                    break;
                }
            }
            scrapState.setText(str);

            EditText creator = (EditText)convertView.findViewById(R.id.creator);          //创建人
            creator.setText(scrapQueries.get(position).getAssetOperate().getUser().getUserName());

            EditText createDate= (EditText)convertView.findViewById(R.id.createDate);     //创建日期
            createDate.setText(scrapQueries.get(position).getAssetOperate().getCreatedate());

            EditText operNote = (EditText)convertView.findViewById(R.id.operNote);    //备注
            operNote.setText(scrapQueries.get(position).getAssetOperate().getOperNote());

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
                    AssetScrapQuery scrapQuery = scrapQueries.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("fragid",fragid);
                    bundle.putSerializable("user",user);
                    bundle.putSerializable("scrapQuery", scrapQuery);

                    Redirect.redirect(context, ScrapListActivity.class, bundle);
                }
            });
        }
        return convertView;
    }
}
