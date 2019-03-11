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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetChangeQuery;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * Created by Administrator on 2018/5/3.
 * 页面转移单的适配器
 */

public class TransferStyleAdapter extends BaseAdapter {
    private ArrayList<AssetChangeQuery> changeQueries;
    private Context context;
    private int fragid;
    private User user;

    //记录checkbox的状态
    HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public TransferStyleAdapter(ArrayList<AssetChangeQuery> changeQueries, Context context, int fragid, User user) {
        this.changeQueries = changeQueries;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
    }

    @Override
    public int getCount() {
        return changeQueries.size();
    }

    @Override
    public Object getItem(int position) {
        return changeQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.layout_transferquery, null);

        if(convertView!=null) {
            final ImageView down = (ImageView) convertView.findViewById(R.id.down);
            final ImageView up = (ImageView) convertView.findViewById(R.id.up);
            /*Button seeDetails = (Button) convertView.findViewById(R.id.seeDetails);*/
            ImageView seeDetails = (ImageView) convertView.findViewById(R.id.seeDetails);

            EditText transferCode = (EditText)convertView.findViewById(R.id.transferCode);          //转移单号
            EditText transferState = (EditText) convertView.findViewById(R.id.transferState);       //状态
            EditText changeDate = (EditText)convertView.findViewById(R.id.changeDate);              //转出时间
            //EditText oldGroup= (EditText)convertView.findViewById(R.id.oldGroup);                   //转出集团

            final LinearLayout setOldGroup = (LinearLayout)convertView.findViewById(R.id.setOldCompany);  //转出单位
            EditText oldCompany = (EditText)convertView.findViewById(R.id.oldCompany);

            final LinearLayout setOldCompany = (LinearLayout)convertView.findViewById(R.id.setOldCompany);  //转出单位
            EditText oleCompany = (EditText)convertView.findViewById(R.id.oldCompany);

            final LinearLayout setOldDepartment = (LinearLayout)convertView.findViewById(R.id.setOldDepartment);    //转出部门
            EditText oldDepartment = (EditText)convertView.findViewById(R.id.oldDepartment);

            final LinearLayout setOldPeople = (LinearLayout)convertView.findViewById(R.id.setOldPeople);       //原使用人
            EditText oldPeople = (EditText)convertView.findViewById(R.id.oldPeople);

            final LinearLayout setOldPlace = (LinearLayout)convertView.findViewById(R.id.setOldPlace);   //原地址
            EditText oldPlace = (EditText)convertView.findViewById(R.id.oldPlace);

            /*final LinearLayout setNewGroup = (LinearLayout)convertView.findViewById(R.id.setNewGroup);      //转入集团
            EditText newGroup = (EditText)convertView.findViewById(R.id.newGroup);*/

            final LinearLayout setNewGompany = (LinearLayout)convertView.findViewById(R.id.setNewGompany);      //转入公司
            EditText newGompany = (EditText)convertView.findViewById(R.id.newGompany);

            final LinearLayout setNewDepartment = (LinearLayout)convertView.findViewById(R.id.setNewDepartment);      //转入部门
            EditText newDepartment = (EditText)convertView.findViewById(R.id.newDepartment);

            final LinearLayout setNewPeople = (LinearLayout)convertView.findViewById(R.id.setNewPeople);      //新使用人
            EditText newPeople = (EditText)convertView.findViewById(R.id.newPeople);

            final LinearLayout setNewPlace = (LinearLayout)convertView.findViewById(R.id.setNewPlace);      //新地址
            EditText newPlace = (EditText)convertView.findViewById(R.id.newPlace);

            final LinearLayout setCreateMan = (LinearLayout)convertView.findViewById(R.id.setCreateMan);      //操作人
            EditText createMan = (EditText)convertView.findViewById(R.id.createMan);

            final LinearLayout setCreateDate = (LinearLayout)convertView.findViewById(R.id.setCreateDate);      //操作时间
            EditText createDate = (EditText)convertView.findViewById(R.id.createDate);

            transferCode.setText(changeQueries.get(position).getOperbillCode());
            int state = changeQueries.get(position).getAssetOperate().getState();
            String str="";
            switch (state){
                case -1:
                    str = "已删除";
                    break;
                case 0:
                    str = "未报审";
                    break;
                case 1:
                    str = "审核中";
                    break;
                case 2:
                    str = "未通过";
                    break;
                case 3:
                    str = "审核通过";
                    break;
                case 6:
                    str = "待确认";
                    break;
                case 7:
                    str = "已完成";
                    break;
                case 11:
                    str = "导入新增";
                    break;
            }
            transferState.setText(str);

            changeDate.setText(changeQueries.get(position).getChangeDate());
            //oldGroup.setText(changeQueries.get(position).getOldGroupObj()==null?changeQueries.get(position).getOldGroup():changeQueries.get(position).getOldGroupObj().getDeptName());
            oldCompany.setText(changeQueries.get(position).getOldCompanyObj().getDeptName());
            oldDepartment.setText(changeQueries.get(position).getOldDeptObj().getDeptName());
            oldPeople.setText(changeQueries.get(position).getOldPeopleObj().getpName());
            oldPlace.setText(changeQueries.get(position).getOldPlace());
            //newGroup.setText(changeQueries.get(position).getNewGroupObj()==null?changeQueries.get(position).getNewGroup():changeQueries.get(position).getNewGroupObj().getDeptName());
            newGompany.setText(changeQueries.get(position).getNewCompanyObj()==null?"":changeQueries.get(position).getNewCompanyObj().getDeptName());
            newDepartment.setText(changeQueries.get(position).getNewDeptObj()==null?"":changeQueries.get(position).getNewDeptObj().getDeptName());
            newPeople.setText(changeQueries.get(position).getNewDeptPeople()==null?"":changeQueries.get(position).getNewDeptPeople().getpName());
            newPlace.setText(changeQueries.get(position).getNewPlace());
            createMan.setText(changeQueries.get(position).getAssetOperate().getUser().getUserName());
            createDate.setText(changeQueries.get(position).getAssetOperate().getCreatedate());

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
                    AssetChangeQuery changeQuery = changeQueries.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("fragid",fragid);
                    bundle.putSerializable("user",user);
                    bundle.putSerializable("changeQuery", changeQuery);

                    Redirect.redirect(context, TransferListActivity.class, bundle);
                }
            });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOldGroup.setVisibility(View.VISIBLE);
                    setOldCompany.setVisibility(View.VISIBLE);
                    setOldDepartment.setVisibility(View.VISIBLE);
                    setOldPeople.setVisibility(View.VISIBLE);
                    setOldPlace.setVisibility(View.VISIBLE);
                    //setNewGroup.setVisibility(View.VISIBLE);
                    setNewGompany.setVisibility(View.VISIBLE);
                    setNewDepartment.setVisibility(View.VISIBLE);
                    setNewPeople.setVisibility(View.VISIBLE);
                    setNewPlace.setVisibility(View.VISIBLE);
                    setCreateMan.setVisibility(View.VISIBLE);
                    setCreateDate.setVisibility(View.VISIBLE);
                    down.setVisibility(View.GONE);
                    up.setVisibility(View.VISIBLE);
                }
            });
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOldGroup.setVisibility(View.GONE);
                    setOldCompany.setVisibility(View.GONE);
                    setOldDepartment.setVisibility(View.GONE);
                    setOldPeople.setVisibility(View.GONE);
                    setOldPlace.setVisibility(View.GONE);
                    //setNewGroup.setVisibility(View.GONE);
                    setNewGompany.setVisibility(View.GONE);
                    setNewDepartment.setVisibility(View.GONE);
                    setNewPeople.setVisibility(View.GONE);
                    setNewPlace.setVisibility(View.GONE);
                    setCreateMan.setVisibility(View.GONE);
                    setCreateDate.setVisibility(View.GONE);
                    down.setVisibility(View.VISIBLE);
                    up.setVisibility(View.GONE);
                }
            });

        }
        return convertView;
    }
}
