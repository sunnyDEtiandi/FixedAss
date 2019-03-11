package fixedass.xiangrong.com.fixedass.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrow;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrowQuery;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * @author Eileen
 * @create 2018/5/3
 * @Describe 借用单显示的适配器
 */
public class BorrowStyleAdapter extends BaseAdapter {
    private ArrayList<AssetBorrowQuery> borrowQueries;
    private Context context;
    private int fragid;
    private User user;

    //记录checkbox的状态
    HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public BorrowStyleAdapter(ArrayList<AssetBorrowQuery> borrowQueries, Context context, int fragid, User user) {
        this.borrowQueries = borrowQueries;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
    }

    @Override
    public int getCount() {
        return borrowQueries.size();
    }

    @Override
    public Object getItem(int position) {
        return borrowQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.layout_borrowquery, null);

        if(convertView!=null) {
            final ImageView down = (ImageView) convertView.findViewById(R.id.down);
            final ImageView up = (ImageView) convertView.findViewById(R.id.up);
            /*Button seeDetails = (Button) convertView.findViewById(R.id.seeDetails);*/
            ImageView seeDetails = (ImageView) convertView.findViewById(R.id.seeDetails);

            EditText borrowCode = (EditText)convertView.findViewById(R.id.borrowCode);
            EditText borrowPeople = (EditText) convertView.findViewById(R.id.borrowPeople);
            EditText borrowState = (EditText)convertView.findViewById(R.id.borrowState);
            EditText borrowDate= (EditText)convertView.findViewById(R.id.borrowDate);

            final LinearLayout setBorrowDays = (LinearLayout)convertView.findViewById(R.id.setBorrowDays);
            EditText borrowDays = (EditText)convertView.findViewById(R.id.borrowDays);

            final LinearLayout setReturnDate = (LinearLayout)convertView.findViewById(R.id.setReturnDate);
            EditText returnDate = (EditText)convertView.findViewById(R.id.returnDate);

            /*final LinearLayout setBorrowGroup = (LinearLayout)convertView.findViewById(R.id.setBorrowGroup);
            EditText borrowGroup = (EditText)convertView.findViewById(R.id.borrowGroup);*/

            final LinearLayout setBorrowCompany = (LinearLayout)convertView.findViewById(R.id.setBorrowCompany);
            EditText borrowCompany = (EditText)convertView.findViewById(R.id.borrowCompany);

            final LinearLayout setBorrowDepatment = (LinearLayout)convertView.findViewById(R.id.setBorrowDepatment);
            EditText borrowDeptment = (EditText)convertView.findViewById(R.id.borrowDepatment);

            final LinearLayout setCreatePeople = (LinearLayout)convertView.findViewById(R.id.setCreatePeople);
            EditText createPeople = (EditText)convertView.findViewById(R.id.createPeople);

            final LinearLayout setCreateDate = (LinearLayout)convertView.findViewById(R.id.setCreateDate);
            EditText createDate = (EditText)convertView.findViewById(R.id.createDate);

            borrowCode.setText(borrowQueries.get(position).getOperbillCode());
            if (borrowQueries.get(position).getDeptPeople()!=null){
                System.out.println("===DeptPeople===="+borrowQueries.get(position).getDeptPeople());
                borrowPeople.setText(borrowQueries.get(position).getDeptPeople().getpName());
            }

            int state = borrowQueries.get(position).getAssetOperate().getState();
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
                case 4:
                    str = "未归还";
                    break;
                case 5:
                    str = "部分归还";
                    break;
                case 7:
                    str = "已完成";
                    break;
                case 8:
                    str = "导入待报审";
                    break;
                case 9:
                    str = "导入待审核";
                    break;
                case 10:
                    str = "导入未通过";
                    break;
                case 11:
                    str = "导入新增";
                    break;
            }
            borrowState.setText(str);

            borrowDate.setText(borrowQueries.get(position).getBorrowDate());

            String days = borrowQueries.get(position).getBorrowDays()==null?"0":borrowQueries.get(position).getBorrowDays()+"";
            borrowDays.setText(days);
            returnDate.setText(borrowQueries.get(position).getReturnDate());
            //borrowGroup.setText(borrowQueries.get(position).getBorrowGroupName());
            borrowCompany.setText(borrowQueries.get(position).getBorrowCompanyName());
            borrowDeptment.setText(borrowQueries.get(position).getBorrowDeptName());
            createPeople.setText(borrowQueries.get(position).getAssetOperate().getUser().getUserName());

            String createdate = borrowQueries.get(position).getAssetOperate().getCreatedate();
            String createTime = borrowQueries.get(position).getAssetOperate().getCreatetime();

            createDate.setText(borrowQueries.get(position).getAssetOperate().getCreatedate());

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
                    AssetBorrowQuery assetBorrowQuery = borrowQueries.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("fragid",fragid);
                    bundle.putSerializable("user",user);
                    bundle.putSerializable("assetBorrowQuery", assetBorrowQuery);

                    Redirect.redirect(context, BorrowListActivity.class, bundle);
                }
            });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBorrowDays.setVisibility(View.VISIBLE);
                    setReturnDate.setVisibility(View.VISIBLE);
                    //setBorrowGroup.setVisibility(View.VISIBLE);
                    setBorrowCompany.setVisibility(View.VISIBLE);
                    setBorrowDepatment.setVisibility(View.VISIBLE);
                    setCreatePeople.setVisibility(View.VISIBLE);
                    setCreateDate.setVisibility(View.VISIBLE);
                    down.setVisibility(View.GONE);
                    up.setVisibility(View.VISIBLE);
                }
            });
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBorrowDays.setVisibility(View.GONE);
                    setReturnDate.setVisibility(View.GONE);
                    //setBorrowGroup.setVisibility(View.GONE);
                    setBorrowCompany.setVisibility(View.GONE);
                    setBorrowDepatment.setVisibility(View.GONE);
                    setCreatePeople.setVisibility(View.GONE);
                    setCreateDate.setVisibility(View.GONE);
                    down.setVisibility(View.VISIBLE);
                    up.setVisibility(View.GONE);
                }
            });

        }
        return convertView;
    }
}
