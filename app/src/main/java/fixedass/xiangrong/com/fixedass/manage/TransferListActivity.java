package fixedass.xiangrong.com.fixedass.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetChange;
import fixedass.xiangrong.com.fixedass.bean.AssetChangeQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * Created by Administrator on 2018/6/12.
 * 转移单详情
 */

public class TransferListActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    private ListViewEx lvx;

    //资产编码，资产名称，资产状态，转出日期，原使用人，新使用人，新使用公司，新使用部门，新保管员，新地址
    /*private static String BARCODE = "barCode";                  //资产编码
    private static String ASSNAME = "assName";                  //资产名称
    private static String STATE = "state";                      //资产状态
    private static String CHANGEDATE = "changeDate";            //转出日期
    private static String OLDPEOPLE = "oldPeople";              //原使用人
    private static String NEWPEOPLE = "newPeople";              //新使用人
    private static String NEWGROUP = "newGroup";                //新使用集团
    private static String NEWCOMPANY = "newCompany";            //新使用公司
    private static String NEWDEPT = "newDept";                   //新使用部门
    private static String CAREMAN = "careMan";                  //保管员
    private static String NEWPLACE = "newPlace";                //地址*/

    private static String[] row = {"barCode","assName","state","changeDate","oldPeople","newPeople","newGroup","newCompany","newDept",
            "careMan","newPlace"};
    private static String[] rowName = {"资产编码","资产名称","资产状态","转出日期",
            "原使用人","新使用人","新使用集团","新使用公司","新使用部门","保管员","存放地址"};

    private List<Map<String, Object>> infoList = null;
    private AssetChangeQuery changeQuery;
    private int fragid;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_list);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        changeQuery = (AssetChangeQuery)bundle.getSerializable("changeQuery");

        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        initView();
        initEvent();
        initContentDataList();
    }

    private void initView(){
        /*btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit = (Button)findViewById(R.id.btnQuit);*/
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.assetTransferList);

        //初始化表格
        //资产编码，资产名称，资产状态，转出日期，原使用人，新使用人，新使用公司，新使用部门，新保管员，新地址
        initListViewHead(R.id.tv_list_table_transfer_tvhead1, false, rowName[0]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead2, false, rowName[1]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead3, false, rowName[2]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead4, false, rowName[3]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead5, false, rowName[4]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead6, false, rowName[5]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead7, false, rowName[6]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead8, false, rowName[7]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead9, false, rowName[8]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead10, false, rowName[9]);
        initListViewHead(R.id.tv_list_table_transfer_tvhead11, false, rowName[10]);

        lvx = (ListViewEx) this.findViewById(R.id.lv_table_lvTransferList);

        lvx.inital(R.layout.list_table_transfer, row, new int[] {
                R.id.tv_list_table_transfer_tvhead1,
                R.id.tv_list_table_transfer_tvhead2,
                R.id.tv_list_table_transfer_tvhead3,
                R.id.tv_list_table_transfer_tvhead4,
                R.id.tv_list_table_transfer_tvhead5,
                R.id.tv_list_table_transfer_tvhead6,
                R.id.tv_list_table_transfer_tvhead7,
                R.id.tv_list_table_transfer_tvhead8,
                R.id.tv_list_table_transfer_tvhead9,
                R.id.tv_list_table_transfer_tvhead10,
                R.id.tv_list_table_transfer_tvhead11,
        });
    }
    int iListHead = 0;
    private void initListViewHead(int id, boolean isCheckBox, String text) {
        if (iListHead == 0) {
            iListHead = getResources().getColor(R.color.list_head);
        }

        if (!isCheckBox) {
            TextView view = (TextView) findViewById(id);
            view.setText(text);
            view.setBackgroundColor(iListHead);
            view.setTextAppearance(this, R.style.ListHeadText);
            view.setGravity(Gravity.CENTER);
        } else {
            CheckBox view = (CheckBox) findViewById(id);
            view.setText(text);
            view.setBackgroundColor(iListHead);
            view.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void initEvent() {
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        switch (v.getId()) {
            case R.id.btnBack:
                Redirect.redirect(TransferListActivity.this, TransferActivity.class, bundle);
                break;
            case R.id.btnQuit:
                Redirect.quit(TransferListActivity.this);
                break;
        }
    }

    /**
     * 初始化content数据
     */
    private void initContentDataList() {
        infoList = new ArrayList<Map<String, Object>>();
        if(changeQuery!=null){

            List<AssetChange> changeList = changeQuery.getAssetOperate().getList();
            for (int i=0;i<changeList.size();i++) {
                AssetChange assetChange = changeList.get(i);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put(row[0], assetChange.getBarCode());
                AssetStorage storage = assetChange.getStorage();
                map.put(row[1], storage.getAssName());
                Integer isReturn = changeQuery.getAssetOperate().getState();
                String str = "";
                switch (isReturn){
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
                    case 6:
                        str = "待确认";
                        break;
                    case 7:
                        str = "已完成";
                        break;
                    case 11:
                        str = "转移中";
                        break;
                }
                map.put(row[2], str);
                //资产编码，资产名称，资产状态，转出日期，原使用人，新使用人，新使用公司，新使用部门，新保管员，新地址
                map.put(row[3], assetChange.getChangeDate());
                map.put(row[4], assetChange.getOldPeopleObj().getpName());
                map.put(row[5], assetChange.getNewDeptPeople()==null?"":assetChange.getNewDeptPeople().getpName());
                map.put(row[6], assetChange.getNewGroupObj()==null?"":assetChange.getNewGroupObj().getDeptName());
                map.put(row[7], assetChange.getNewCompanyObj()==null?"":assetChange.getNewCompanyObj().getDeptName());
                map.put(row[8], assetChange.getNewDeptObj()==null?"":assetChange.getNewDeptObj().getDeptName());
                map.put(row[9], assetChange.getCareManObj()==null?"":assetChange.getCareManObj().getpName());
                map.put(row[10], assetChange.getNewPlace());

                infoList.add(map);
            }
            lvx.add(infoList);
        }
    }
}
